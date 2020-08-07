package com.github.tyrbot.tyrdata.models.channels;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TyrChannelDataDelta {

    private final String channelName;

    private final boolean commandPrefixChanged;
    private final Optional<String> commandPrefix;

    private final boolean disabledCommandsChanged;
    private final Optional<Set<String>> addDisabledCommands;
    private final Optional<Set<String>> removeDisabledCommands;

    private final boolean customCommandsChanged;
    private final Optional<Map<String, String>> addCustomCommands;
    private final Optional<Set<String>> removeCustomCommands;

    protected TyrChannelDataDelta(String channelName, Optional<String> commandPrefix,
            Optional<Set<String>> addDisabledCommands, Optional<Set<String>> removeDisabledCommands,
            Optional<Map<String, String>> addCustomCommands, Optional<Set<String>> removeCustomCommands) {
        this.channelName = channelName;
        this.commandPrefix = commandPrefix;
        this.addDisabledCommands = addDisabledCommands;
        this.removeDisabledCommands = removeDisabledCommands;
        this.addCustomCommands = addCustomCommands;
        this.removeCustomCommands = removeCustomCommands;

        commandPrefixChanged = commandPrefix.isPresent();
        disabledCommandsChanged = addDisabledCommands.isPresent() || removeDisabledCommands.isPresent();
        customCommandsChanged = addCustomCommands.isPresent() || removeCustomCommands.isPresent();
    }

    public boolean hasAnyChange() {
        return isCommandPrefixChanged() || isDisabledCommandsChanged() || isCustomCommandsChanged();
    }

    public String getChannelName() {
        return channelName;
    }

    public boolean isCommandPrefixChanged() {
        return commandPrefixChanged;
    }

    public Optional<String> getCommandPrefix() {
        return commandPrefix;
    }

    public boolean isDisabledCommandsChanged() {
        return disabledCommandsChanged;
    }

    public Optional<Set<String>> getAddDisabledCommands() {
        return addDisabledCommands;
    }

    public Optional<Set<String>> getRemoveDisabledCommands() {
        return removeDisabledCommands;
    }

    public boolean isCustomCommandsChanged() {
        return customCommandsChanged;
    }

    public Optional<Map<String, String>> getAddCustomCommands() {
        return addCustomCommands;
    }

    public Optional<Set<String>> getRemoveCustomCommands() {
        return removeCustomCommands;
    }
}