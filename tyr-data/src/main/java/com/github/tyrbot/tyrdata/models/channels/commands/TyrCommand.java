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
        return Set.of(new TyrCommand(TyrCommandType.LIST_COMMANDS, "commands", "Commands available in this channel are: $COMMANDS.", PermissionLevel.VIEWER),
            new TyrCommand(TyrCommandType.ADD_COMMAND, "add", "Added command $ARG0.", PermissionLevel.MODERATOR),
            new TyrCommand(TyrCommandType.REMOVE_COMMAND, "remove", "Removed command $ARG0.", PermissionLevel.MODERATOR),
            new TyrCommand(TyrCommandType.UPTIME, "uptime", "The stream is currently offline.$|The stream has been online for $TIME{d,h,m,s}", PermissionLevel.VIEWER),
            new TyrCommand(TyrCommandType.FOLLOWAGE, "followage", "$SENDER is not following this channel.$|$SENDER has been following this channel for $TIME{y,d,h}", PermissionLevel.VIEWER),
            new TyrCommand(TyrCommandType.PERMIT, "permit", "The user $ARG0 is now temporary excluded from the chat filter.", PermissionLevel.MODERATOR));
    }
}