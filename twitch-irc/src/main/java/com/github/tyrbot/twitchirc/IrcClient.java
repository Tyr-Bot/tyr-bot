package com.github.tyrbot.twitchirc;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.github.tyrbot.twitchdatamodels.irc.IrcChannel;
import com.github.tyrbot.twitchdatamodels.irc.messages.IrcMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.control.PingMessage;

public class IrcClient implements WebSocket.Listener {

    private static final String PASS_COMMAND_FORMAT = "PASS oauth:%s";
    private static final String NICK_COMMAND_FORMAT = "NICK %s";
    private static final String JOIN_COMMAND_FORMAT = "JOIN %s";
    private static final String PART_COMMAND_FORMAT = "PART %s";
    private static final String PRIVMSG_COMMAND_FORMAT = "PRIVMSG %s :%s";
    private static final String REQUIRE_CAPABILITIES_COMMAND = "CAP REQ :twitch.tv/tags twitch.tv/commands twitch.tv/membership";
    private static final String PONG_COMMAND = "PONG :tmi.twitch.tv";

    private final String token;
    private final String username;
    private final WebSocket socket;
    private final MessagePublisher messagePublisher;

    private StringBuilder recievedDataStringBuffer = new StringBuilder();

    protected IrcClient(String token, String username, String connectionUri)
            throws URISyntaxException, InterruptedException, ExecutionException {
        this.token = token;
        this.username = username.toLowerCase();
        this.socket = HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(new URI(connectionUri), this).get();
        this.messagePublisher = new MessagePublisher();

        connect();
    }

    public void connect() {
        try {
            sendCommand(String.format(PASS_COMMAND_FORMAT, token)).get();
            sendCommand(String.format(NICK_COMMAND_FORMAT, username)).get();
            sendCommand(REQUIRE_CAPABILITIES_COMMAND).get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    public CompletableFuture<Boolean> sendCommand(String command) {
        //TODO: remove
        System.out.println(">>" + command);
        return socket.sendText(command, true).handleAsync((ws, ex) -> {
            return new CompletableFuture<>().complete(ex != null);
        });
    }

    public CompletableFuture<Void> close() {
        return socket.sendClose(WebSocket.NORMAL_CLOSURE, "").thenAccept(WebSocket::abort);
    }

    public CompletableFuture<Boolean> joinChannel(IrcChannel channel) {
        return sendCommand(String.format(JOIN_COMMAND_FORMAT, channel.getChannelReference()));
    }

    public CompletableFuture<Boolean> partChannel(IrcChannel channel) {
        return sendCommand(String.format(PART_COMMAND_FORMAT, channel.getChannelReference()));
    }

    public CompletableFuture<Boolean> sendMessage(IrcChannel channel, String message) {
        return sendCommand(String.format(PRIVMSG_COMMAND_FORMAT, channel.getChannelReference(), message));
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        webSocket.request(1);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        error.printStackTrace();
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        recievedDataStringBuffer.append(data);
        if (last) {
            processMessage(recievedDataStringBuffer.toString());
            recievedDataStringBuffer = new StringBuilder();
        }
        webSocket.request(1);
        return null;
    }

    public void processMessage(String message) {
        for (String splitMessage : message.split("\n")) {
            //TODO: remove
            System.out.println("<<" + splitMessage);
            IrcMessage ircMessage = MessageParser.parseIrcMessage(splitMessage);
            System.out.println(ircMessage.getClass().getSimpleName());
            handleMessageInternal(ircMessage);
            messagePublisher.publishMessage(ircMessage);
        }
    }

    public void handleMessageInternal(IrcMessage message) {
        if (message instanceof PingMessage) {
            try {
                sendCommand(PONG_COMMAND).get();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void registerSubscriber(Consumer<IrcMessage> subscriber, SubscriptionType... eventSubscribtions) {
        messagePublisher.registerSubscriber(subscriber, eventSubscribtions);
    }
}
