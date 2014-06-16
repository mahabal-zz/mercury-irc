package com.mercuryirc.event.received;

import com.mercuryirc.event.AbstractEvent;
import com.mercuryirc.model.Entity;
import com.mercuryirc.model.IrcType;
import com.mercuryirc.model.Mode;
import com.mercuryirc.network.Connection;

import java.util.Set;

public final class ModeEvent extends AbstractEvent {

    public final boolean add;
    public final Set<Mode> modes;
    public final Entity target;

    public ModeEvent(final Connection connection, final Entity target, final Set<Mode> modes, final boolean add) {
        super(connection, IrcType.EVENT);
        this.target = target;
        this.modes = modes;
        this.add = add;
    }
}
