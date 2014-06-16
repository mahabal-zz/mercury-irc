package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.Mode;
import com.mercuryirc.network.Connection;

import java.util.List;

public final class ModeListEvent extends AbstractEvent {

    public final Mode.Type modeType;
    public final Channel channel;
    public final List<Mode> list;

    public ModeListEvent(final Connection c, final Channel channel, final Mode.Type modeType, final List<Mode> list) {
        super(c, IrcType.EVENT);
        this.channel = channel;
        this.modeType = modeType;
        this.list = list;
    }
}
