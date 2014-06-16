package com.mercuryirc.client.event;

import com.google.common.eventbus.Subscribe;
import com.mercuryirc.client.Mercury;
import com.mercuryirc.client.misc.Tray;
import com.mercuryirc.client.ui.ApplicationPane;
import com.mercuryirc.client.ui.Tab;
import com.mercuryirc.client.ui.TabPane;
import com.mercuryirc.client.ui.model.MessageRow;
import com.mercuryirc.event.MercuryEventBus;
import com.mercuryirc.event.received.*;
import com.mercuryirc.model.*;
import com.mercuryirc.network.Connection;
import com.mercuryirc.network.callback.Callback;
import javafx.application.Platform;

import java.awt.*;
import java.util.Set;

public class EventHandler implements Callback {

    private ApplicationPane appPane;

    public EventHandler(ApplicationPane appPane) {
        this.appPane = appPane;
        MercuryEventBus.register(this);
    }

    @Subscribe
    public void onConnect(final ConnectEvent event) {
        final Connection c = event.connection;
        User local = c.getLocalUser();
        if (local.getNickservPassword() != null) {
            c.privmsg(new Message(local, c.getServer().getUser("NickServ"), "identify " + local.getNickservPassword()), true);
        }
        if (local.getAutojoinChannels() != null) {
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
            }
            for (String channel : local.getAutojoinChannels()) {
                c.join(channel);
            }
        }
    }

    @Subscribe
    public void onMessageReceived(final MessageEvent e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (e.type == IrcType.MESSAGE) {
                    boolean highlight = e.message.getMessage().toLowerCase().contains(e.connection.getLocalUser().getName().toLowerCase());
                    appPane.getTabPane().addTargetedMessage(e.connection, e.message, highlight ? MessageRow.Type.HIGHLIGHT : MessageRow.Type.PRIVMSG);
                    if (highlight && !Mercury.getStage().isFocused()) {
                        String title = "Highlighted by " + e.message.getSource().getName();
                        Tray.notify(title, e.message.getMessage(), true);
                        Toolkit.getDefaultToolkit().beep();
                    }
                } else if (e.type == IrcType.NOTICE) {
                    appPane.getTabPane().addUntargetedMessage(e.connection, e.message, MessageRow.Type.NOTICE);
                    if (!Mercury.getStage().isFocused()) {
                        Tray.notify("Notice from " + e.message.getSource(), e.message.getMessage(), true);
                        Toolkit.getDefaultToolkit().beep();
                    }
                } else if (e.type == IrcType.CTCP) {
                    String ctcp = e.message.getMessage();
                    if (ctcp.startsWith("ACTION")) {
                        Message _message = new Message(e.message.getSource(), e.message.getTarget(), ctcp.substring(7));
                        appPane.getTabPane().addTargetedMessage(e.connection, _message, MessageRow.Type.EVENT);
                    } else {
                        Message _message = new Message(e.message.getSource(), e.message.getTarget(), ctcp);
                        appPane.getTabPane().addUntargetedMessage(e.connection, _message, MessageRow.Type.CTCP);
                        if (ctcp.equals("VERSION")) {
                            String arch = System.getProperty("os.arch");
                            if (arch.equals("amd64")) {
                                arch = "x64";
                            }
                            String sysInfo = System.getProperty("os.name") + " (" + arch + " "
                                    + Runtime.getRuntime().availableProcessors() + "-core)";
                            e.connection.ctcp(e.message.getSource(), "VERSION MercuryIRC // " + sysInfo);
                        }
                    }
                }
            }
        });
    }


    @Subscribe
    public void onJoin(final JoinEvent e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(e.user, e.channel, "has joined the channel");
                appPane.getTabPane().addTargetedMessage(e.connection, message, MessageRow.Type.JOIN);
            }
        });
    }

    @Subscribe
    public void onPart(final PartEvent event) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(event.user, event.channel, "has left the channel (" + event.message + ")");
                appPane.getTabPane().addTargetedMessage(event.connection, message, MessageRow.Type.PART);
            }
        });
    }

    @Subscribe
    public void onQuit(final QuitEvent e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(e.user, null, "has quit (" + e.reason + ")");
                appPane.getTabPane().addUserStatusMessage(e.user, message, MessageRow.Type.PART, null);
            }
        });
    }

    @Subscribe
    public void onNickList(final NickListEvent e) {
    }

    @Subscribe
    public void onTopic(final TopicEvent e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(e.user, e.channel, "has set the topic: " + e.message);
                appPane.getTabPane().addTargetedMessage(e.connection, message, MessageRow.Type.EVENT);
                appPane.getTabPane().get(e.connection, e.channel).getContentPane().getTopicPane().setTopic(e.message);
            }
        });
    }

    @Subscribe
    public void onNick(final NicknameEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(null, null, event.oldNick + " is now known as " + event.user.getName());
                appPane.getTabPane().addUserStatusMessage(event.user, message, MessageRow.Type.EVENT, new TabPane.TabAction() {
                    @Override
                    public void process(Tab tab) {
                        tab.getContentPane().getUserPane().sort();
                    }
                });
                if (event.user.equals(event.connection.getLocalUser())) {
                    appPane.getContentPane().getInputPane().setNick(event.user.getName());
                }
            }
        });
    }

    @Subscribe
    public void onKick(final KickEvent e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(e.user, e.channel, "has been kicked from the channel (" + e.message + ")");
                appPane.getTabPane().addTargetedMessage(e.connection, message, MessageRow.Type.PART);
            }
        });
    }

    @Subscribe
    public void onModeList(final ModeListEvent e) {
        Message message = new Message(null, null, "Channel " + e.modeType.toString() + " list:");
        appPane.getTabPane().addTargetedMessage(e.connection, message, MessageRow.Type.EVENT);
        for (Mode mode : e.list) {
            Message message2 = new Message(null, null, ((User) mode.getTarget()).getHost() + " by " + mode.getSource().getHost());
            appPane.getTabPane().addTargetedMessage(e.connection, message2, MessageRow.Type.EVENT);
        }
    }

    @Subscribe
    public void onError(final ErrorEvent e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Message message = new Message(null, null, e.error);
                appPane.getTabPane().addUntargetedMessage(e.connection, message, MessageRow.Type.ERROR);
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    @Subscribe
    public void onMode(final ModeEvent e) {

        final Entity target = e.target;
        final Set<Mode> modes = e.modes;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Mode mode : modes) {
                    Message message = new Message(mode.getSource(), target, (e.add ? "" : "un") + "sets " + mode.getType().toString() + " on " + mode.getTarget().getName() + (mode.getTarget().equals(target) ? "" : " in " + target.getName()));
                    if (target instanceof Channel) {
                        appPane.getTabPane().addTargetedMessage(e.connection, message, MessageRow.Type.EVENT);
                    } else {
                        appPane.getTabPane().addUntargetedMessage(e.connection, message, MessageRow.Type.EVENT);
                    }
                }
            }
        });
    }

    @Override
    public void onPrivmsgOut(final Connection connection, final Message message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                appPane.getTabPane().addTargetedMessage(connection, message, MessageRow.Type.SELF);
            }
        });
    }

    @Override
    public void onNoticeOut(final Connection connection, final Message message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                appPane.getTabPane().addUntargetedMessage(connection, message, MessageRow.Type.NOTICE);
            }
        });
    }

    @Override
    public void onCtcpOut(final Connection connection, final Message message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String ctcp = message.getMessage();
                if (ctcp.startsWith("ACTION")) {
                    Message _message = new Message(connection.getLocalUser(), message.getTarget(), ctcp.substring(7));
                    appPane.getTabPane().addTargetedMessage(connection, _message, MessageRow.Type.EVENT);
                } else {
                    appPane.getTabPane().addUntargetedMessage(connection, message, MessageRow.Type.CTCP);
                }
            }
        });
    }

    @Override
    public void onQueryOut(final Connection connection, final User user) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                appPane.getTabPane().create(connection, user, true);
            }
        });
    }

    @Override
    public void onJoinOut(final Connection connection, final Channel channel) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Tab tab = appPane.getTabPane().get(connection, channel);
                appPane.getTabPane().select(tab);
            }
        });
    }

    @Override
    public void onPartOut(final Connection connection, final Channel channel) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Tab tab = appPane.getTabPane().get(connection, channel);
                appPane.getTabPane().close(tab, false);
            }
        });
    }

    @Override
    public void onConnectionRequestOut(final Connection connection, final String network, final String hostname, final int port, final String nick) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Server server = new Server(network, hostname, port, "", false);
                User local = connection.getLocalUser();
                User user = new User(server, local.getName(), local.getUserName(), local.getRealName());
                Mercury.connect(server, user);
            }
        });
    }

    @Override
    public void onQuitOut(final Connection connection, final User user, final String reason) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                appPane.getTabPane().close(appPane.getTabPane().get(connection, connection.getServer()), false);
            }
        });
    }

    @Subscribe
    public void onJoinError(final JoinFailedEvent e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Tab tab = appPane.getTabPane().get(e.connection, e.channel);
                appPane.getTabPane().close(tab);
                Toolkit.getDefaultToolkit().beep();

                Message reasonMessage = new Message(null, null, e.channel.getName() + ": " + e.reason);
                appPane.getTabPane().addUntargetedMessage(e.connection, reasonMessage, MessageRow.Type.ERROR);
            }
        });
    }

}