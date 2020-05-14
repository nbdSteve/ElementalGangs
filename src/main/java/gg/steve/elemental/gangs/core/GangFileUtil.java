package gg.steve.elemental.gangs.core;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.utils.LogUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GangFileUtil {
    //Store the file name string
    private String fileName;
    //Store the player file
    private File file;
    //Store the yaml config
    private YamlConfiguration config;

    public GangFileUtil(String fileName, String gangName, String ownerId) {
        //Set instance variable
        this.fileName = fileName;
        //Get the player file
        file = new File(Gangs.get().getDataFolder(), "gang-data" + File.separator + fileName + ".yml");
        //Load the configuration for the file
        config = YamlConfiguration.loadConfiguration(file);
        //If the file doesn't exist then set the defaults
        if (!file.exists()) {
            setupFactionFileDefaults(config, gangName, fileName, ownerId);
        }
        save();
    }

    public GangFileUtil(String fileName) {
        //Set instance variable
        this.fileName = fileName;
        //Get the player file
        file = new File(Gangs.get().getDataFolder(), "gang-data" + File.separator + fileName + ".yml");
        //Load the configuration for the file
        config = YamlConfiguration.loadConfiguration(file);
        //If this method is being called then we know that the file exists
        save();
    }

    private void setupFactionFileDefaults(YamlConfiguration config, String gangName, String gangId, String ownerId) {
        //Set defaults for the information about the players tiers and currency
        config.set("gangId", gangId);
        config.set("ownerId", ownerId);
        config.set("name", gangName);
        config.set("bank", 0.0);
        config.set("founded", new SimpleDateFormat(Files.CONFIG.get().getString("who-cmd.founded-format")).format(new Date(System.currentTimeMillis())));
        config.createSection("gang-members");
        //Send a nice message
        LogUtil.info("Successfully created a new gang data file: " + fileName + ", defaults have been set.");
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            LogUtil.warning("Critical error saving the file: " + fileName + ", please contact nbdSteve#0583 on discord.");
        }
        reload();
    }

    public void reload() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            LogUtil.warning("Critical error loading the file: " + fileName + ", please contact nbdSteve#0583 on discord.");
        }
    }

    public void delete() {
        file.delete();
    }

    public YamlConfiguration get() {
        return config;
    }
}
