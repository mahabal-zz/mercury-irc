package com.mercuryirc.event;

import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public final class PartEvent {

    //Can't be changed, so public. No need for getters.
    public final Connection connection;
    public final Channel channel;
    public final User user;
    public final String reason;
    public final IrcType type;

    public PartEvent(final Connection connection, final Channel channel, final User user, final String reason) {
        this.connection = connection;
        this.channel = channel;
        this.user = user;
        this.reason = reason;
        this.type = IrcType.PART;
    }
}
