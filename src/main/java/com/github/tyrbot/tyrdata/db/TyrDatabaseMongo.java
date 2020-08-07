package com.github.tyrbot.tyrdata.db;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.tyrbot.tyrdata.TyrDatabase;
import com.github.tyrbot.tyrdata.models.channels.TyrChannelData;
import com.github.tyrbot.tyrdata.models.channels.TyrChannelDataDelta;
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

//TODO: TESTING
public class TyrDatabaseMongo extends TyrDatabase {

    private final static String DATABASE_NAME = "tyrBotDB";
    private final static String CHANNEL_DATA_COLLECTION_NAME = "channelData";

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    private final MongoCollection<Document> channelDataCollection;

    public TyrDatabaseMongo(final String host, final int port) {
        this.mongoClient = MongoClients.create(String.format("mongodb://%s:%d", host, port));
        this.mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);

        this.channelDataCollection = mongoDatabase.getCollection(CHANNEL_DATA_COLLECTION_NAME);
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    @Override
    public TyrChannelData getChannelData(String channelName) throws IllegalStateException {
        Document foundDocument = channelDataCollection.find(Filters.eq("channelName", channelName)).first();

        if (foundDocument == null) {
            throw new IllegalStateException(String.format("No document with channelName %s was found!", channelName));
        }

        TyrChannelData result = new TyrChannelData(channelName, foundDocument.getString("commandPrefix"),
                new HashSet<String>(foundDocument.getList("disabledCommands", String.class, List.of())),
                foundDocument.get("customCommands", Document.class).entrySet().stream().collect(
                        Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue().toString())));
        return result;
    }

    @Override
    public void initializeChannelDataIfEmpty(String channelName) {
        if (channelDataCollection.find(Filters.eq("channelName", channelName)).first() != null) {
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

        TyrChannelData currentData = getChannelData(dataDelta.getChannelName());

        Bson updateExpression = Updates.setOnInsert("commandPrefix", "!");

        if (dataDelta.isCommandPrefixChanged()) {
            updateExpression = Updates.combine(updateExpression,
                    Updates.set("commandPrefix", dataDelta.getCommandPrefix().get()));
        }

        if (dataDelta.isDisabledCommandsChanged()) {
            Set<String> desiredState = currentData.getDisabledCommands();
            dataDelta.getRemoveDisabledCommands().ifPresent(desiredState::removeAll);
            dataDelta.getAddDisabledCommands().ifPresent(desiredState::addAll);

            updateExpression = Updates.combine(updateExpression, Updates.set("disabledCommands", desiredState));
        }

        if (dataDelta.isCustomCommandsChanged()) {
            Map<String, String> desiredState = currentData.getCustomCommands();
            dataDelta.getRemoveCustomCommands().ifPresent(removeTargets -> removeTargets.forEach(desiredState::remove));
            dataDelta.getAddCustomCommands().ifPresent(desiredState::putAll);

            updateExpression = Updates.combine(updateExpression, Updates.set("customCommands", desiredState));
        }

        try {
            UpdateResult updateResult = channelDataCollection
                    .updateOne(Filters.eq("channelName", dataDelta.getChannelName()), updateExpression);

            System.out.println("Updated in mongodb: " + updateResult);
        } catch (MongoException ex) {
            System.err.println("Error on mongoDB update operation: " + ex.getMessage());
        }
    }
}