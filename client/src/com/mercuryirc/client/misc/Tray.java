package com.mercuryirc.client.misc;

import com.mercuryirc.client.Mercury;

import java.awt.*;
import java.net.URL;

public class Tray {
    private static final String ICON_CAPTION = "Mercury";
    private static TrayIcon icon = null;

    public static boolean init() {
        if (!SystemTray.isSupported()) {
            return false;
        }

        SystemTray sysTray = SystemTray.getSystemTray();
        URL url = Mercury.class.getResource("/res/images/icon16.png");
        Image im = Toolkit.getDefaultToolkit().getImage(url);
        icon = new TrayIcon(im, ICON_CAPTION);
        icon.setImageAutoSize(true);
        try {
            sysTray.add(icon);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void notify(String caption, String message, boolean abbreviate) {
        if (icon == null) {
            return;
        }
        if (abbreviate && message.length() > 50) {
            message = message.substring(0, 50) + "...";
        }
        //icon.displayMessage(caption, message, TrayIcon.MessageType.INFO); TODO fix later, this locks up the application on osx
    }
}
