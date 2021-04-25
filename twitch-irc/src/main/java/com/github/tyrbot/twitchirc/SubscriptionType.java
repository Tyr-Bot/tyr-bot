package com.github.tyrbot.twitchirc;

import com.github.tyrbot.twitchdatamodels.irc.messages.IrcMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.ClearChatMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.ClearMsgMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.PrivMsgMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.RoomStateMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserJoinMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserNoticeMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserPartMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.channel.UserStateMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.control.MiscMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.control.PingMessage;
import com.github.tyrbot.twitchdatamodels.irc.messages.UnkownMessage;

public enum SubscriptionType {

    CLEAR_CHAT(ClearChatMessage.class), 
    CLEAR_MSG(ClearMsgMessage.class), 
    PRIV_MSG(PrivMsgMessage.class),
    ROOM_STATE(RoomStateMessage.class), 
    USER_JOIN(UserJoinMessage.class), 
    USER_NOTICE(UserNoticeMessage.class),
    USER_PART(UserPartMessage.class), 
    USER_STATE(UserStateMessage.class),

    MISC(MiscMessage.class), 
    PING(PingMessage.class),

    UNKNOWN(UnkownMessage.class);

    private Class<? extends IrcMessage> messageSubclass;

    private SubscriptionType(Class<? extends IrcMessage> messageSubclass) {
        this.messageSubclass = messageSubclass;
    }

    public static SubscriptionType getFromMessage(IrcMessage message) {
        for (SubscriptionType type : SubscriptionType.values()) {
            if (type.messageSubclass.isInstance(message)) {
                return type;
            }
        }

        return UNKNOWN;
    }
}
