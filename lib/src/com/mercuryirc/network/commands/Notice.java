package com.mercuryirc.network.commands;

import com.mercuryirc.event.MercuryEventBus;
import com.mercuryirc.event.MessageEvent;
import com.mercuryirc.misc.IrcUtils;
import com.mercuryirc.model.*;
import com.mercuryirc.network.Connection;

public class Notice implements Connection.CommandHandler {

	public boolean applies(Connection connection, String command, String line) {
		return command.equals("NOTICE");
	}

	public void process(Connection connection, String line, String[] parts) {
		final String from = IrcUtils.parseSource(parts[0]);
		final String to = parts[2];

		final String text = line.substring(line.indexOf(':', 1) + 1);

		final Server server = connection.getServer();
		final User source = server.getUser(from);
		final Entity target = to.startsWith("#") ? server.getChannel(to) : server.getUser(to);

        MessageEvent event;

		if(text.length() > 0 && text.charAt(0) == '\u0001') {
			String ctcp = text.substring(1, text.indexOf('\u0001', 1));
			Message message = new Message(source, target, ctcp);
            event = new MessageEvent(connection, message, IrcType.CTCP);
		} else {
			Message message = new Message(source, target, text);
            event = new MessageEvent(connection, message, IrcType.MESSAGE);
		}

        MercuryEventBus.post(event);

	}

}