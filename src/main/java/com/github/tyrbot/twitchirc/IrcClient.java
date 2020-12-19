package com.github.tyrbot.twitchirc;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class IrcClient implements WebSocket.Listener {

    private final String token;
    private final String username;
    private final WebSocket socket;

    private StringBuilder recievedDataStringBuffer = new StringBuilder();

    protected IrcClient(String token, String username, String connectionUri)
            throws URISyntaxException, InterruptedException, ExecutionException {
        this.token = token;
        this.username = username;
        this.socket = HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(new URI(connectionUri), this).get();
    }

    public CompletableFuture<Boolean> sendMessage(String message) {
        return socket.sendText(message, true).handleAsync((ws, ex) -> {
            return new CompletableFuture<>().complete(ex != null);
        });
    }

    public CompletableFuture<Void> close() {
        return socket.sendClose(WebSocket.NORMAL_CLOSURE, "").thenAccept(WebSocket::abort);
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        // TODO Auto-generated method stub
        Listener.super.onOpen(webSocket);
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
        //TODO: Missing implementation
    }
}
