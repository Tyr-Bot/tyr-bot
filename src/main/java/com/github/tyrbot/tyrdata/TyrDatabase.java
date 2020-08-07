package com.github.tyrbot.tyrdata;

import com.github.tyrbot.tyrdata.models.channels.TyrChannelData;
import com.github.tyrbot.tyrdata.models.channels.TyrChannelDataDelta;

public abstract class TyrDatabase implements AutoCloseable {

    public abstract TyrChannelData getChannelData(final String channelName);

    public abstract void initializeChannelDataIfEmpty(final String channelName);

    public abstract void updateChannelData(final TyrChannelDataDelta dataDelta);

}