package com.mercuryirc.client.ui;

import com.mercuryirc.client.Mercury;
import com.mercuryirc.client.misc.Notifications;
import com.mercuryirc.client.ui.model.MessageRow;
import com.mercuryirc.network.Connection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.awt.*;
import java.net.URI;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MessagePane extends VBox {

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("h:mmaa");

    {
        DateFormatSymbols sym = TIME_FORMATTER.getDateFormatSymbols();
        sym.setAmPmStrings(new String[]{"am", "pm"});
        TIME_FORMATTER.setDateFormatSymbols(sym);
    }

    private final ApplicationPane appPane;
    private final Tab tab;
    private final Connection connection;

    private final WebView webView;
    private final List<MessageRow> loadQueue;
    private boolean pageLoaded;

    public MessagePane(ApplicationPane appPane, Tab tab, Connection connection) {
        this.connection = connection;
        this.appPane = appPane;
        this.tab = tab;
        setId("message-pane");
        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
        webView = new WebView();
        loadQueue = Collections.synchronizedList(new ArrayList<MessageRow>());

        webView.setContextMenuEnabled(false);
        VBox.setVgrow(webView, Priority.ALWAYS);
        HBox.setHgrow(webView, Priority.ALWAYS);

        WebEngine engine = webView.getEngine();
        engine.getLoadWorker().stateProperty().addListener(new LoadListener());
        engine.load(Mercury.class.getResource("/res/html/MessageList.html").toExternalForm());

        getChildren().add(webView);
    }

    @SuppressWarnings("unused")
    public void openUrl(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                Runtime.getRuntime().exec("xdg-open " + url);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void addRow(MessageRow message) {
        if (pageLoaded) {
            webView.getEngine().executeScript(String.format("addRow('%s', '%s', '%s', '%s')", message.getSource(), message.getMessage(), TIME_FORMATTER.format(new Date()), message.getType().style()));
            Tab selected = appPane.getTabPane().getSelected();
            if (message.getType() == MessageRow.Type.HIGHLIGHT) {
                if (!appPane.isFocused()) {
                    Notifications.dispatch(message.getSource(), message.getMessage());
                }
            }
            if ((message.getType() == MessageRow.Type.PRIVMSG || message.getType() == MessageRow.Type.HIGHLIGHT) &&
                    selected != null && !selected.equals(tab)) {
                tab.setUnread(true);
            }
        } else {
            loadQueue.add(message);
        }

    }

    private void onLoad() {
        pageLoaded = true;
        synchronized (loadQueue) {
            for (MessageRow message : loadQueue) {
                addRow(message);
            }
        }
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember("panel", this);
    }

    private class LoadListener implements ChangeListener<Worker.State> {

        public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State newState) {
            if (newState.equals(Worker.State.SUCCEEDED)) {
                onLoad();
            }
        }
    }

}