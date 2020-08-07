package com.github.tyrbot.tyrdata.models.channels;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TyrChannelData {

    private final String channelName;
    private final String commandPrefix;
    private final Set<String> disabledCommands;
    private final Map<String, String> customCommands;

    public TyrChannelData(final String channelName, final String commandPrefix, final Set<String> disabledCommands,
            final Map<String, String> customCommands) {
        this.channelName = channelName;
        this.commandPrefix = commandPrefix;
        this.disabledCommands = disabledCommands;
        this.customCommands = customCommands;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public Set<String> getDisabledCommands() {
        return disabledCommands;
    }

    public Map<String, String> getCustomCommands() {
        return customCommands;
    }

    public Map<String, Object> getFieldMap() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("channelName", channelName);
        result.put("commandPrefix", commandPrefix);
        result.put("disabledCommands", disabledCommands);
        result.put("customCommands", customCommands);

        return result;
    }

    public static TyrChannelData getDefault(final String channelName) {
        return new TyrChannelData(channelName, "!", Set.of(), Map.of());
    }
}