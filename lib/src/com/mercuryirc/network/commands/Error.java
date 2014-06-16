package com.mercuryirc.network.commands;

import com.mercuryirc.event.MercuryEventBus;
import com.mercuryirc.event.received.ErrorEvent;
import com.mercuryirc.network.Connection;

public class Error implements Connection.CommandHandler {

	@Override
	public boolean applies(Connection connection, String command, String line) {
		return command.equals("ERROR");
	}

	@Override
	public void process(Connection connection, String line, String[] parts) {
        MercuryEventBus.post(new ErrorEvent(connection, line.substring(7)));
	}

}