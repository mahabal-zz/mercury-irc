package com.mercuryirc.network;

import com.mercuryirc.network.numerics.*;

import java.util.ArrayList;
import java.util.List;

public class NumericHandlers {

    public static final List<Connection.NumericHandler> list = new ArrayList<Connection.NumericHandler>();

    static {
        list.add(new ChannelNickList());
        list.add(new ChannelTopic());
        list.add(new TopicTimestamp());
        list.add(new EndOfNamesList());
        list.add(new Welcome());
        list.add(new NickInUse());
        list.add(new BanList());
        list.add(new Who());
        list.add(new JoinError());
        list.add(new Unknown());
    }

}
