package com.mercuryirc.network.callback;

import com.mercuryirc.model.Channel;
import com.mercuryirc.model.Entity;
import com.mercuryirc.model.Message;
import com.mercuryirc.model.Mode;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

import java.util.List;
import java.util.Set;

public interface Callback {

	//out

	void onPrivmsgOut(Connection connection, Message message);

	void onNoticeOut(Connection connection, Message message);

	void onCtcpOut(Connection connection, Message message);

	void onQueryOut(Connection connection, User user);

	void onJoinOut(Connection connection, Channel channel);

	void onPartOut(Connection connection, Channel channel);

	void onConnectionRequestOut(Connection connection, String network, String hostname, int port, String nick);

	void onQuitOut(Connection connection, User user, String reason);

}