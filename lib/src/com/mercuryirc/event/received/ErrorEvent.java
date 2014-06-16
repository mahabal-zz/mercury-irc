package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.network.Connection;

public final class ErrorEvent extends AbstractEvent {

    public final String error;

    public ErrorEvent(final Connection connection, final String error) {
        super(connection, IrcType.ERROR);
        this.error = error;
    }
}
