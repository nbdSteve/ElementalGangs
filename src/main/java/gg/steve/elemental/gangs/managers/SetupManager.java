package gg.steve.elemental.gangs.managers;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.chat.PlayerChatListener;
import gg.steve.elemental.gangs.cmd.GangCmd;
import gg.steve.elemental.gangs.gui.GuiClickListener;
import gg.steve.elemental.gangs.listener.PlayerDamageListener;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Class that handles setting up the plugin on start
 */
public class SetupManager {

    private SetupManager() throws IllegalAccessException {
        throw new IllegalAccessException("Manager class cannot be instantiated.");
    }

    /**
     * Loads the files into the file manager
     *
     * @param fileManager FileManager, the plugins file manager
     */
    public static void setupFiles(FileManager fileManager) {
        // general files
        for (Files file : Files.values()) {
            file.load(fileManager);
        }
    }

    public static void registerCommands(Gangs instance) {
        instance.getCommand("g").setExecutor(new GangCmd());
    }

    /**
     * Register all of the events for the plugin
     *
     * @param instance Plugin, the main plugin instance
     */
    public static void registerEvents(Plugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new GangPlayerManager(), instance);
        pm.registerEvents(new PlayerChatListener(), instance);
        pm.registerEvents(new PlayerDamageListener(), instance);
        pm.registerEvents(new GuiClickListener(), instance);
     }
}
