package com.github.tyrbot.tyrdata.models.channels;

import java.util.Optional;
import java.util.Set;

import com.github.tyrbot.tyrdata.models.channels.commands.TyrCommand;

public class TyrChannelDataDelta {

    public final String channelName;

    public final boolean commandPrefixChanged;
    public final Optional<String> commandPrefix;

    public final boolean commandsChanged;
    public final Optional<Set<TyrCommand>> addCommands;
    public final Optional<Set<TyrCommand>> removeCommands;

    protected TyrChannelDataDelta(String channelName, Optional<String> commandPrefix,
            Optional<Set<TyrCommand>> addCommands, Optional<Set<TyrCommand>> removeCommands) {
        this.channelName = channelName;
        this.commandPrefix = commandPrefix;
        this.addCommands = addCommands;
        this.removeCommands = removeCommands;

        commandPrefixChanged = commandPrefix.isPresent();
        commandsChanged = addCommands.isPresent() || removeCommands.isPresent();
    }

    public boolean hasAnyChange() {
        return commandPrefixChanged || commandsChanged;
    }
}