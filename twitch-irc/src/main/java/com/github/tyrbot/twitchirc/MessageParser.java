package com.github.tyrbot.twitchirc;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.tyrbot.twitchdatamodels.irc.IrcChannel;
import com.github.tyrbot.twitchdatamodels.irc.messages.IrcMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.UnknownMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.PrivMsgMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.RoomStateMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserJoinMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserPartMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserStateMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.control.MiscMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.control.PingMessage;

public class MessageParser {

    private MessageParser() {
    }

    // TODO: Missing implementation
    public static IrcMessage parseIrcMessage(String message) {
        if (message.startsWith("PING")) {
            return new PingMessage(message);
        }

        ParsingDataContainer parsingData = parseBasicData(message);
        Map<String, String> tags;
        Map<String, Integer> badges;

        switch (parsingData.command) {
            case "PRIVMSG":
                String[] messageParts = parsingData.arguments.split(" :", 2);
                tags = parsingData.tags.orElseThrow();
                badges = parsingData.getBadges();
                return new PrivMsgMessage(new IrcChannel(messageParts[0]), parsingData.getBadgeInfo(), badges,
                        tags.get("color"), tags.get("display-name"), badges.containsKey("moderator"),
                        badges.containsKey("subscriber"), badges.containsKey("broadcaster"), tags.get("user-id"),
                        tags.get("room-id"), Long.parseLong(tags.get("tmi-sent-ts")), tags.get("id"),
                        parsingData.getEmotes(), messageParts[1]);
            case "ROOMSTATE":
                tags = parsingData.tags.orElseThrow();
                return new RoomStateMessage(new IrcChannel(parsingData.arguments), tags.get("room-id"),
                        Integer.parseInt(tags.get("slow")), Integer.parseInt(tags.get("followers-only")),
                        tags.get("subs-only").equals("1"), tags.get("emote-only").equals("1"),
                        tags.get("rituals").equals("1"), tags.get("r9k").equals("1"));
            case "JOIN":
                return new UserJoinMessage(new IrcChannel(parsingData.arguments), parsingData.getUserIdentity());
            case "PART":
                return new UserPartMessage(new IrcChannel(parsingData.arguments), parsingData.getUserIdentity());
            case "USERSTATE":
                tags = parsingData.tags.orElseThrow();
                badges = parsingData.getBadges();
                return new UserStateMessage(new IrcChannel(parsingData.arguments), parsingData.getBadgeInfo(), badges,
                        tags.get("color"), tags.get("display-name"), badges.containsKey("moderator"),
                        badges.containsKey("subscriber"), badges.containsKey("broadcaster"));

            default:
                if (parsingData.command.matches("\\d{3}")) {
                    return new MiscMessage(message);
                }

                return new UnknownMessage(message);
        }

        // TODO: add user list message

    }

    public static ParsingDataContainer parseBasicData(String message) {
        int tagOffset = message.startsWith("@") ? 1 : 0;
        String[] messageParts = message.split(" ", 3 + tagOffset);
        return new ParsingDataContainer(tagOffset == 1 ? parseMessageTags(messageParts[0].substring(1)) : null,
                messageParts[0 + tagOffset].substring(1), messageParts[1 + tagOffset], messageParts[2 + tagOffset]);
    }

    public static Map<String, String> parseMessageTags(String tagString) {
        String[] values = tagString.split(";");
        return Arrays.stream(values).map(str -> str.split("="))
                .collect(Collectors.toMap(val -> val[0], val -> val.length > 1 ? val[1] : ""));
    }

}
