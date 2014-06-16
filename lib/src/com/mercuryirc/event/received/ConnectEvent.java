package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.network.Connection;

public final class ConnectEvent extends AbstractEvent {

    public ConnectEvent(final Connection connection) {
        super(connection, IrcType.EVENT);
    }

}
