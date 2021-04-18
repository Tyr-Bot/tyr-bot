package com.github.tyrbot.twitchirc;

import com.github.tyrbot.twitchdatamodels.irc.IrcChannel;
import com.github.tyrbot.twitchdatamodels.irc.messages.IrcMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.UnkownMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserJoinMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserPartMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.control.MiscMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.control.PingMessage;

public class MessageParser {

    private MessageParser() {}
    
    //TODO: Missing implementation
    public static IrcMessage parseIrcMessage(String message) {
        if (message.startsWith("PING")) {
            return new PingMessage(message);
        }

        if (message.startsWith(":tmi.twitch.tv")) {
            return new MiscMessage(message);
        }

        //TODO: add user list message

        if (message.startsWith(":") && message.contains("JOIN")) {
            String[] messageParts = message.split(" ");
            return new UserJoinMessage(new IrcChannel(messageParts[2]), messageParts[0].split("!")[0]);
        }

        if (message.startsWith(":") && message.contains("PART")) {
            String[] messageParts = message.split(" ");
            return new UserPartMessage(new IrcChannel(messageParts[2]), messageParts[0].split("!")[0]);
        }

        return new UnkownMessage(message);
    }

}
