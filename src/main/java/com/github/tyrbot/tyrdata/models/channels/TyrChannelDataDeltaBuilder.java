package com.github.tyrbot.tyrdata.models.channels;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.github.tyrbot.tyrdata.models.channels.commands.TyrCommand;

public class TyrChannelDataDeltaBuilder {

    private final String channelName;

    private Optional<String> commandPrefix;

    private Optional<Set<TyrCommand>> addCommands;
    private Optional<Set<TyrCommand>> removeCommands;

    public TyrChannelDataDeltaBuilder(final String channelName) {
        this.channelName = channelName;
        this.commandPrefix = Optional.empty();

        this.addCommands = Optional.empty();
        this.removeCommands = Optional.empty();
    }

    public TyrChannelDataDelta build() {
        return new TyrChannelDataDelta(channelName, commandPrefix, addCommands, removeCommands);
    }

    public TyrChannelDataDeltaBuilder setCommandPrefix(String commandPrefix) {
        this.commandPrefix = Optional.of(commandPrefix);
        return this;
    }

    public TyrChannelDataDeltaBuilder addCommand(TyrCommand command) {
        this.addCommands = addToOptionalSetIfPresentOrCreateNew(addCommands, command);
        return this;
    }

    public TyrChannelDataDeltaBuilder addCommands(Set<TyrCommand> commands) {
        this.addCommands = addToOptionalSetIfPresentOrCreateNew(addCommands, commands);
        return this;
    }

    public TyrChannelDataDeltaBuilder removeCommand(TyrCommand command) {
        this.removeCommands = addToOptionalSetIfPresentOrCreateNew(removeCommands, command);
        return this;
    }

    public TyrChannelDataDeltaBuilder removeCommands(Set<TyrCommand> commands) {
        this.removeCommands = addToOptionalSetIfPresentOrCreateNew(removeCommands, commands);
        return this;
    }

    private <T> Optional<Set<T>> addToOptionalSetIfPresentOrCreateNew(Optional<Set<T>> initialSet, T element) {
        Set<T> result = initialSet.orElse(new HashSet<>());
        result.add(element);
        return Optional.of(result);
    }

    private <T> Optional<Set<T>> addToOptionalSetIfPresentOrCreateNew(Optional<Set<T>> initialSet, Set<T> elements) {
        Set<T> result = initialSet.orElse(new HashSet<>());
        result.addAll(elements);
        return Optional.of(result);
    }
}