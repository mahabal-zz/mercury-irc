package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public final class JoinEvent extends AbstractEvent {

    //Can't be changed, so public. No need for getters.
    public final Channel channel;
    public final User user;

    public JoinEvent(final Connection connection, final Channel channel, final User user) {
        super(connection, IrcType.JOIN);
        this.channel = channel;
        this.user = user;
    }
}
