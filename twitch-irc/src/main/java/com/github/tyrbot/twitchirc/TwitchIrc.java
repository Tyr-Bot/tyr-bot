package com.github.tyrbot.twitchirc;

import java.net.URISyntaxException;

import java.util.concurrent.ExecutionException;

public class TwitchIrc {

    private static final String DEFAULT_CONNECTION_URI = "wss://irc-ws.chat.twitch.tv:443";

    public static IrcClient getClient(String token, String username)
            throws URISyntaxException, InterruptedException, ExecutionException {
        return new IrcClient(token, username, DEFAULT_CONNECTION_URI);
    }
}
