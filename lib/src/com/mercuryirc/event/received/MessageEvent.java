package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.Message;
import com.mercuryirc.network.Connection;

public final class MessageEvent extends AbstractEvent {

    public final Message message;


    public MessageEvent(final Connection connection, final Message message, final IrcType type) {
        super(connection, type);
        this.message = message;
    }


}
