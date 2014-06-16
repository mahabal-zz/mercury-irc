package com.mercuryirc.client.misc;

import java.io.File;

public class IoUtils {

    public static String resourceUrlAsString(final String path) {
        final File resource = new File(path);
        if (resource.exists()) {
            return resource.toURI().toString();
        } else {
            return IoUtils.class.getResource("/" + path.replace("client/src/", "")).toExternalForm();
        }
    }


}
