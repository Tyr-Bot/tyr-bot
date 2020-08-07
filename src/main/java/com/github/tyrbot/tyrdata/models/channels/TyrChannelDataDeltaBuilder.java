package com.github.tyrbot.tyrdata.models.channels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TyrChannelDataDeltaBuilder {

    private final String channelName;

    private Optional<String> commandPrefix;

    private Optional<Set<String>> addDisabledCommands;
    private Optional<Set<String>> removeDisabledCommands;

    private Optional<Map<String, String>> addCustomCommands;
    private Optional<Set<String>> removeCustomCommands;

    public TyrChannelDataDeltaBuilder(final String channelName) {
        this.channelName = channelName;
        this.commandPrefix = Optional.empty();

        this.addDisabledCommands = Optional.empty();
        this.removeDisabledCommands = Optional.empty();

        this.addCustomCommands = Optional.empty();
        this.removeDisabledCommands = Optional.empty();
    }

    public TyrChannelDataDelta build() {
        return new TyrChannelDataDelta(channelName, commandPrefix, addDisabledCommands, removeDisabledCommands,
                addCustomCommands, removeCustomCommands);
    }

    public TyrChannelDataDeltaBuilder setCommandPrefix(String commandPrefix) {
        this.commandPrefix = Optional.of(commandPrefix);
        return this;
    }

    public TyrChannelDataDeltaBuilder addDisabledCommand(String disabledCommand) {
        this.addDisabledCommands = addToOptionalSetIfPresentOrCreateNew(addDisabledCommands, disabledCommand);
        return this;
    }

    public TyrChannelDataDeltaBuilder addDisabledCommands(Set<String> disabledCommands) {
        this.addDisabledCommands = addToOptionalSetIfPresentOrCreateNew(addDisabledCommands, disabledCommands);
        return this;
    }

    public TyrChannelDataDeltaBuilder removeDisabledCommand(String disabledCommand) {
        this.removeDisabledCommands = addToOptionalSetIfPresentOrCreateNew(removeDisabledCommands, disabledCommand);
        return this;
    }

    public TyrChannelDataDeltaBuilder removeDisabledCommands(Set<String> disabledCommands) {
        this.removeDisabledCommands = addToOptionalSetIfPresentOrCreateNew(removeDisabledCommands, disabledCommands);
        return this;
    }

    public TyrChannelDataDeltaBuilder addCustomCommand(String commandName, String commandResponse) {
        this.addCustomCommands = addToOptionalMapIfPresentOrCreateNew(addCustomCommands, commandName, commandResponse);
        return this;
    }

    public TyrChannelDataDeltaBuilder addCustomCommands(Map<String, String> customCommands) {
        this.addCustomCommands = addToOptionalMapIfPresentOrCreateNew(addCustomCommands, customCommands);
        return this;
    }

    public TyrChannelDataDeltaBuilder removeCustomCommand(String commandName) {
        this.removeCustomCommands = addToOptionalSetIfPresentOrCreateNew(removeCustomCommands, commandName);
        return this;
    }

    public TyrChannelDataDeltaBuilder removeCustomCommands(Set<String> customCommands) {
        this.removeCustomCommands = addToOptionalSetIfPresentOrCreateNew(removeCustomCommands, customCommands);
        return this;
    }

    private <T> Optional<Set<T>> addToOptionalSetIfPresentOrCreateNew(Optional<Set<T>> initialSet, T element) {
        Set<T> result = initialSet.orElse(new HashSet<T>());
        result.add(element);
        return Optional.of(result);
    }

    private <T> Optional<Set<T>> addToOptionalSetIfPresentOrCreateNew(Optional<Set<T>> initialSet, Set<T> elements) {
        Set<T> result = initialSet.orElse(new HashSet<T>());
        result.addAll(elements);
        return Optional.of(result);
    }

    private <T, U> Optional<Map<T, U>> addToOptionalMapIfPresentOrCreateNew(Optional<Map<T, U>> initalMap, T key,
            U value) {
        Map<T, U> result = initalMap.orElse(new HashMap<T, U>());
        result.put(key, value);
        return Optional.of(result);
    }

    private <T, U> Optional<Map<T, U>> addToOptionalMapIfPresentOrCreateNew(Optional<Map<T, U>> initalMap,
            Map<T, U> elements) {
        Map<T, U> result = initalMap.orElse(new HashMap<T, U>());
        result.putAll(elements);
        return Optional.of(result);
    }
}