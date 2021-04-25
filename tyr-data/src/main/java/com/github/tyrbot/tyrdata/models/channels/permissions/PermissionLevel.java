package com.github.tyrbot.tyrdata.models.channels.permissions;

public enum PermissionLevel {

    BROADCASTER(0),
    MODERATOR(1),
    SUBSCRIBER(2),
    FOLLOWER(3),
    VIEWER(4);

    /**
     * The lower the permissionIndex is, the more priviliged is the user.
     */
    public final int permissionIndex;

    private PermissionLevel(int permissionIndex) {
        this.permissionIndex = permissionIndex;
    }
}