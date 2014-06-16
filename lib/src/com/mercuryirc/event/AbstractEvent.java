package com.mercuryirc.event;

import com.mercuryirc.model.IrcType;
import com.mercuryirc.network.Connection;

public abstract class AbstractEvent {

    public final Connection connection;
    public final IrcType type;

    protected AbstractEvent(final Connection connection, final IrcType type) {
        this.connection = connection;
        this.type = type;
    }

}
