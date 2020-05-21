package gg.steve.elemental.gangs;

import gg.steve.elemental.gangs.bank.TopGui;
import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.managers.FileManager;
import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.managers.SetupManager;
import gg.steve.elemental.gangs.papi.GangsExpansion;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.utils.LogUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public final class Gangs extends JavaPlugin {
    private static Gangs instance;
    private static Economy economy;
    private static DecimalFormat numberFormat = new DecimalFormat("#,###.##");
    private static TopGui topGui;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        SetupManager.setupFiles(new FileManager(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        GangManager.initialise();
        GangPlayerManager.initialise();
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        } else {
            LogUtil.info("Unable to find economy instance, disabling economy features.");
            economy = null;
        }
        // register placeholders with papi
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new GangsExpansion(instance).register();
        }
        LogUtil.info("Gangs has successfully loaded, please contact nbdSteve#0583 on discord if there are any issues.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        GangManager.save();
    }

    public static Gangs get() {
        return instance;
    }

    public static Economy eco() {
        return economy;
    }

    public static String formatNumber(double amount) {
        return numberFormat.format(amount);
    }

    public static void openTopGui(Player player) {
        if (topGui == null) {
            topGui = new TopGui(Files.TOP_GUI.get());
        } else {
            topGui.refresh();
        }
        topGui.open(player);
    }
}
