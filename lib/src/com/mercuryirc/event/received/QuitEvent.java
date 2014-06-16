package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public final class QuitEvent extends AbstractEvent {

    public final User user;
    public final String reason;

    public QuitEvent(final Connection connection, final User user, final String reason) {
        super(connection, IrcType.EVENT);
        this.user = user;
        this.reason = reason;
    }
}
