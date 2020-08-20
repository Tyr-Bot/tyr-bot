package com.github.tyrbot.tyrdata.models.channels;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.tyrbot.tyrdata.models.channels.commands.TyrCommand;

public class TyrChannelData {

    public final String channelName;
    public final String commandPrefix;
    public final Set<TyrCommand> commands;

    public TyrChannelData(final String channelName, final String commandPrefix, final Set<TyrCommand> commands) {
        this.channelName = channelName;
        this.commandPrefix = commandPrefix;
        this.commands = commands;
    }

    public Map<String, Object> getFieldMap() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("channelName", channelName);
        result.put("commandPrefix", commandPrefix);
        result.put("commands", commands);

        return result;
    }

    public static TyrChannelData getDefault(final String channelName) {
        return new TyrChannelData(channelName, "!", TyrCommand.getDefaultCommands());
    }
}