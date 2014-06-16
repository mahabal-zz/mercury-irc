package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public final class JoinFailedEvent extends AbstractEvent {

    //Can't be changed, so public. No need for getters.
    public final Channel channel;
    public final String reason;

    public JoinFailedEvent(final Connection connection, final Channel channel, final String reason) {
        super(connection, IrcType.ERROR);
        this.channel = channel;
        this.reason = reason;
    }
}
