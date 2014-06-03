package com.mercuryirc.client.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Notifications implements Constants {

    //TODO: Winblows
    //TODO: Apple



    public static void dispatch(final String title, final String message) {

        switch (OperatingSystem.current()) {
            case LINUX:
                try {
                    final List<String> command = new ArrayList<>();
                    command.add("notify-send");
                    command.add("-u");
                    command.add("normal");
                    command.add("-a");
                    command.add("'" + NAME + "'");
                    command.add("-i");
                    //Lol this whole next line
                    command.add(IoUtils.resourceUrlAsString("client/src/res/images/icon64.png").replace("file:", ""));
                    command.add(title);
                    command.add(message);
                    Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
