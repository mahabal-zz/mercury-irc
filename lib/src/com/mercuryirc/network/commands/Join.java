package com.mercuryirc.network.commands;

import com.mercuryirc.event.received.JoinEvent;
import com.mercuryirc.event.MercuryEventBus;
import com.mercuryirc.misc.IrcUtils;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.Server;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;

public class Join implements Connection.CommandHandler {

	public boolean applies(Connection connection, String command, String line) {
		return command.equals("JOIN");
	}

	public void process(Connection connection, String line, String[] parts) {
		String chName = parts[2];

		// some chat servers send :#channel, some just send #channel
		if(chName.startsWith(":"))
			chName = chName.substring(1);

		final Server srv = connection.getServer();

        final String nick = IrcUtils.parseSource(parts[0]);
		final String username = parts[0].substring(parts[0].indexOf('!') + 1, parts[0].indexOf('@'));
		final String host = parts[0].substring(parts[0].indexOf('@') + 1);

		final User user = srv.getUser(nick);
		final Channel channel = srv.getChannel(chName);

		// update state
		user.addChannel(channel);
		user.setUserName(username);
		user.setHost(host);

		channel.addUser(user);

		// if the person who joined the channel was us...
		if (nick.equalsIgnoreCase(connection.getLocalUser().getName())) {
			// get info on the hostmasks
			connection.who(chName);
		}

        final JoinEvent event = new JoinEvent(connection, channel, user);
        MercuryEventBus.post(event);

	}

}
