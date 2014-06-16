package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

import java.util.List;

public final class NickListEvent extends AbstractEvent {

    public final Channel channel;
    public final List<User> users;

    public NickListEvent(final Connection c, final Channel channel, final List<User> users) {
        super(c, IrcType.EVENT);
        this.channel = channel;
        this.users = users;
    }
}

