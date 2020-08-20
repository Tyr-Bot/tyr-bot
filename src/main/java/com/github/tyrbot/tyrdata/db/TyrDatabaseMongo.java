package com.github.tyrbot.tyrdata.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.tyrbot.tyrdata.TyrDatabase;
import com.github.tyrbot.tyrdata.models.channels.TyrChannelData;
import com.github.tyrbot.tyrdata.models.channels.TyrChannelDataDelta;
import com.github.tyrbot.tyrdata.models.channels.commands.TyrCommand;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;

public class TyrDatabaseMongo extends TyrDatabase {

    private static final String DATABASE_NAME = "tyrBotDB";
    private static final String CHANNEL_DATA_COLLECTION_NAME = "channelData";

    private static final String CHANNEL_NAME_FIELD = "channelName";
    private static final String COMMAND_PREFIX_FIELD = "commandPrefix";

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    private final MongoCollection<Document> channelDataCollection;

    public TyrDatabaseMongo(final String host, final int port) {
        System.out.println("Connecting to MongoDB instance...");
        this.mongoClient = MongoClients.create(String.format("mongodb://%s:%d", host, port));
        this.mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);

        this.channelDataCollection = mongoDatabase.getCollection(CHANNEL_DATA_COLLECTION_NAME);
        System.out.println("Established connection to MongoDB instance.");
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    @Override
    public TyrChannelData getChannelData(String channelName) throws IllegalStateException {
        Document foundDocument = channelDataCollection.find(Filters.eq(CHANNEL_NAME_FIELD, channelName)).first();

        if (foundDocument == null) {
            throw new IllegalStateException(String.format("No document with channelName %s was found!", channelName));
        }

        return new TyrChannelData(channelName, foundDocument.getString(COMMAND_PREFIX_FIELD),
                new HashSet<>(foundDocument.getList("disabledCommands", TyrCommand.class, List.of())));
    }

    @Override
    public void initializeChannelDataIfEmpty(String channelName) {
        if (channelDataCollection.find(Filters.eq(CHANNEL_NAME_FIELD, channelName)).first() != null) {
            return;
        }

        TyrChannelData defaultData = TyrChannelData.getDefault(channelName);
        Document document = new Document(defaultData.getFieldMap());

        try {
            InsertOneResult result = channelDataCollection.insertOne(document);
            System.out.println("Inserted in mongodb: " + result);
        } catch (MongoException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateChannelData(TyrChannelDataDelta dataDelta) throws IllegalArgumentException {
        if (!dataDelta.hasAnyChange()) {
            throw new IllegalArgumentException("An empty dataDelta shouldn't be supplied to the update function!");
        }

        TyrChannelData currentData = getChannelData(dataDelta.channelName);

        Bson updateExpression = Updates.setOnInsert(COMMAND_PREFIX_FIELD, "!");

        if (dataDelta.commandPrefixChanged) {
            updateExpression = Updates.combine(updateExpression,
                    Updates.set(COMMAND_PREFIX_FIELD, dataDelta.commandPrefix.get()));
        }

        if (dataDelta.commandsChanged) {
            Set<TyrCommand> desiredState = currentData.commands;
            dataDelta.removeCommands.ifPresent(desiredState::removeAll);
            dataDelta.addCommands.ifPresent(desiredState::addAll);

            updateExpression = Updates.combine(updateExpression, Updates.set("commands", desiredState));
        }

        try {
            UpdateResult updateResult = channelDataCollection
                    .updateOne(Filters.eq(CHANNEL_NAME_FIELD, dataDelta.channelName), updateExpression);

            System.out.println("Updated in mongodb: " + updateResult);
        } catch (MongoException ex) {
            System.err.println("Error on mongoDB update operation: " + ex.getMessage());
        }
    }
}