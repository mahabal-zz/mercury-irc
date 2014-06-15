package com.mercuryirc.event;

import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.Message;
import com.mercuryirc.network.Connection;

public final class MessageEvent {

    //Can't be changed, so public. No need for getters.
    public final Connection connection;
    public final Message message;
    public final IrcType type;


    public MessageEvent(final Connection connection, final Message message, final IrcType type) {
        this.connection = connection;
        this.message = message;
        this.type = type;
    }


}
