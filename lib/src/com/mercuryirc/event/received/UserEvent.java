package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public class UserEvent extends AbstractEvent {

    public final Channel channel;
    public final User user;
    public final String message;

    UserEvent(final Connection c, final Channel channel, final User user, final String message, final IrcType type) {
        super(c, type);
        this.channel = channel;
        this.user = user;
        this.message = message;
    }
}
