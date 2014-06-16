package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public final class NicknameEvent extends AbstractEvent {

    public final User user;
    public final String oldNick;

    public NicknameEvent(final Connection connection, final User user, final String oldNick) {
        super(connection, IrcType.EVENT);
        this.user = user;
        this.oldNick = oldNick;
    }
}
