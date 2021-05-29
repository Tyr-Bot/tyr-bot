package com.github.tyrbot.twitchdatamodels.irc.messages;

public class UnknownMessage implements IrcMessage {
    
    public final String messageContent;

    public UnknownMessage(String messageContent) {
        this.messageContent = messageContent;
    }

}
