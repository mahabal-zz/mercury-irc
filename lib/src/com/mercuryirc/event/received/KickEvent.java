package com.mercuryirc.event.received;

import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public final class KickEvent extends UserEvent {

    public KickEvent(final Connection connection, final Channel channel, final User user, final String reason) {
        super(connection, channel, user, reason, IrcType.PART);
    }

}
