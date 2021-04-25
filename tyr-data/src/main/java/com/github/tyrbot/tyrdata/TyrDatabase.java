package com.github.tyrbot.tyrdata;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.tyrbot.tyrdata.models.channels.TyrChannelData;
import com.github.tyrbot.tyrdata.models.channels.TyrChannelDataDelta;
import com.google.common.io.Resources;

public abstract class TyrDatabase implements AutoCloseable {

    public final static String VERSION;

    static {
        String versionBuffer;
        try {
            List<String> gradleConfigLines = Resources.readLines(Resources.getResource("gradleConfig"),
                    Charset.forName("utf-8"));
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

    public abstract TyrChannelData getChannelData(final String channelName);

    public abstract void initializeChannelDataIfEmpty(final String channelName);

    public abstract void updateChannelData(final TyrChannelDataDelta dataDelta);
}