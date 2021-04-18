package com.github.tyrbot.twitchirc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.github.tyrbot.twitchdatamodels.irc.messages.IrcMessage;

public class MessagePublisher {

    private final Map<Consumer<IrcMessage>, Set<SubscriptionType>> subscriberList;

    public MessagePublisher() {
        subscriberList = new HashMap<>();
    }

    public void registerSubscriber(Consumer<IrcMessage> subscriber, SubscriptionType... eventSubscribtions) {
        subscriberList.put(subscriber, Set.of(eventSubscribtions));
    }

    public void publishMessage(IrcMessage message) {
        for (Entry<Consumer<IrcMessage>, Set<SubscriptionType>> subscriberEntry : subscriberList.entrySet()) {
            if (subscriberEntry.getValue().contains(SubscriptionType.getFromMessage(message))) {
                subscriberEntry.getKey().accept(message);
            }
        }
    }
}
