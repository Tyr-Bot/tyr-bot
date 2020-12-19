package com.github.tyrbot.twitchirc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Resources;

public class TwitchIrc {

    public static final String VERSION;

    static {
        String versionBuffer;
        try {
            List<String> gradleConfigLines = Resources.readLines(Resources.getResource("gradleConfig"),
                    StandardCharsets.UTF_8);
            Map<String, String> configStore = new HashMap<>();
            gradleConfigLines.forEach(line -> {
                String[] values = line.split("=");
                configStore.put(values[0], values[1]);
            });

            versionBuffer = configStore.get("version");
        } catch (IOException ex) {
            ex.printStackTrace();
            versionBuffer = "UNKNOWN";
        }

        VERSION = versionBuffer;
    }
}
