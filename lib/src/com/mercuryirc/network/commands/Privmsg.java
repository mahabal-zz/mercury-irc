package com.mercuryirc.network.commands;

import com.mercuryirc.event.MercuryEventBus;
import com.mercuryirc.event.received.MessageEvent;
import com.mercuryirc.misc.IrcUtils;
import com.mercuryirc.model.*;
import com.mercuryirc.network.Connection;

public class Privmsg implements Connection.CommandHandler {

	public boolean applies(Connection connection, String command, String line) {
		return command.equals("PRIVMSG");
	}

	public void process(Connection connection, String line, String[] parts) {

        //Label the parts (easier to work with)
		final String from = IrcUtils.parseSource(parts[0]);
		final String to = parts[2];

		//IPv6 addresses broke this lol.
		//String text = line.substring(line.indexOf(':', 1) + 1);
		final String text = line.substring(line.indexOf(parts[3]) + 1);
		final Server server = connection.getServer();
		final User source = server.getUser(from);
        final Entity target = to.startsWith("#") ? server.getChannel(to) : server.getUser(to);

        //Event to be posted.
        MessageEvent event;

        //Create the event
		if(text.length() > 0 && text.charAt(0) == '\u0001') {
			final String ctcp = text.substring(1, text.indexOf('\u0001', 1));
			final Message message = new Message(source, target, ctcp);
            event = new MessageEvent(connection, message, IrcType.CTCP);
        } else {
			final Message message = new Message(source, target, text);
            event = new MessageEvent(connection, message, IrcType.MESSAGE);
		}

        //Post the event to the event bus
        MercuryEventBus.post(event);

	}

}