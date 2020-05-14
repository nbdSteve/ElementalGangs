package gg.steve.elemental.gangs;

import org.bukkit.plugin.java.JavaPlugin;

public final class Gangs extends JavaPlugin {
    private static Gangs instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Gangs get() {
        return instance;
    }
}
