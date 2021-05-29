package com.github.tyrbot.twitchirc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public class ParsingDataContainer {

    public final Optional<Map<String, String>> tags;
    public final String senderIdentity;
    public final String command;
    public final String arguments;

    public ParsingDataContainer(@Nullable Map<String, String> tags, String senderIdentity, String command,
            String arguments) {
        this.tags = Optional.ofNullable(tags);
        this.senderIdentity = senderIdentity;
        this.command = command;
        this.arguments = arguments;
    }

    public String getUserIdentity() {
        return senderIdentity.split("!")[0];
    }

    public Map<String, Integer> getBadgeInfo() {
        return parseBadgeFormat("badge-info");
    }

    public Map<String, Integer> getBadges() {
        return parseBadgeFormat("badges");
    }

    public Map<Integer, Set<int[]>> getEmotes() {
        if (tags.orElseThrow().get("emotes").isBlank()) {
            return new HashMap<>(1);
        }

        return Arrays.stream(tags.orElseThrow().get("emotes").split("/")).map(str -> str.split(":"))
                .collect(Collectors.toMap(val -> Integer.parseInt(val[0]),
                        val -> Arrays.stream(val[1].split(","))
                                .map(str -> Arrays.stream(str.split("-")).mapToInt(Integer::parseInt).toArray())
                                .collect(Collectors.toSet())));
    }

    private Map<String, Integer> parseBadgeFormat(String key) {
        if (tags.orElseThrow().get(key).isBlank()) {
            return new HashMap<>(1);
        }

        return Arrays.stream(tags.orElseThrow().get(key).split(",")).map(str -> str.split("/"))
                .collect(Collectors.toMap(val -> val[0], val -> Integer.parseInt(val[1])));
    }
}
