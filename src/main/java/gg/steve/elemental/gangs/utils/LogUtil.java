package gg.steve.elemental.gangs.utils;

import gg.steve.elemental.gangs.Gangs;

public class LogUtil {

    public static void info(String message) {
        Gangs.get().getLogger().info(message);
    }

    public static void warning(String message) {
        Gangs.get().getLogger().warning(message);
    }
}
