package com.github.tyrbot.tyrdata;

import com.github.tyrbot.tyrdata.models.channels.TyrChannelData;
import com.github.tyrbot.tyrdata.models.channels.TyrChannelDataDelta;

public interface TyrDatabase extends AutoCloseable {

    public TyrChannelData getChannelData(final String channelName);

    public void initializeChannelDataIfEmpty(final String channelName);

    public void updateChannelData(final TyrChannelDataDelta dataDelta);
}