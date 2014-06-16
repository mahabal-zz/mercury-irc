package com.mercuryirc.event.received;

import com.mercuryirc.event.received.UserEvent;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public final class PartEvent extends UserEvent {

    public PartEvent(final Connection connection, final Channel channel, final User user, final String reason) {
        super(connection, channel, user, reason, IrcType.PART);
    }

}
