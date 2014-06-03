package com.mercuryirc.client.misc;

public enum OperatingSystem {

    WINDOWS, MAC, LINUX, OTHER;

    public static OperatingSystem current() {
        final String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows"))
            return WINDOWS;
        if (osName.contains("mac"))
            return MAC;
        if (osName.contains("linux"))
            return LINUX;
        return OTHER;
    }

}