package com.github.tyrbot.tyrdata.models.channels.commands;

import com.github.tyrbot.tyrdata.models.channels.permissions.PermissionLevel;

import java.util.Set;

public class TyrCommand {

    public final TyrCommandType commandType;
    public final String commandName;
    public final String commandMessage;
    public final PermissionLevel requiredPermissionLevel;

    public TyrCommand(TyrCommandType commandType, String commandName, String commandMessage, PermissionLevel requiredPermissionLevel) {
        this.commandType = commandType;
        this.commandName = commandName;
        this.commandMessage = commandMessage;
        this.requiredPermissionLevel = requiredPermissionLevel;
    }

    public static Set<TyrCommand> getDefaultCommands() {
        //TODO: Missing impl
        return null;
    }
}