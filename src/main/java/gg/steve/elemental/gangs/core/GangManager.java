package gg.steve.elemental.gangs.core;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.requests.RequestManager;
import gg.steve.elemental.gangs.utils.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GangManager {
    private static Map<UUID, Gang> gangs;
    private static RequestManager requestManager;

    /**
     * Method to load all of the gangs and pending requests for the server
     */
    public static void initialise() {
        gangs = new HashMap<>();
        File dataFolder = new File(Gangs.get().getDataFolder() + File.separator + "gang-data");
        for (File file : dataFolder.listFiles()) {
            switch (file.getName()) {
                case "pending-requests.yml":
                    requestManager = new RequestManager(Files.PENDING.get());
                    continue;
                default:
                    UUID gangId = UUID.fromString(file.getName().split(".yml")[0]);
                    LogUtil.info("Loading data for the gang with id " + gangId);
                    gangs.put(gangId, new Gang(gangId));
            }
        }
    }

    public static void save() {
        for (Gang gang : gangs.values()) {
            LogUtil.info("Saving data for gang, " + gang.getName() + ", with id: " + gang.getGangId());
            gang.saveToFile();
        }
        requestManager.saveToFile();
    }

    public static void createGang(UUID gangId, UUID ownerId, String name) {
        gangs.put(gangId, new Gang(gangId, ownerId, name));
        GangPlayerManager.updateGangPlayer(ownerId);
    }

    public static void disbandGang(Gang gang) {
        List<UUID> onlineMembers = gang.getOnlinePlayers();
        gangs.remove(gang.getGangId());
        gang.disband();
        for (UUID playerId : onlineMembers) {
            MessageType.GANG_DISBAND.message(GangPlayerManager.getGangPlayer(playerId), gang.getName());
            GangPlayerManager.updateGangPlayer(playerId);
        }
    }

    public static Gang getGangById(UUID gangId) {
        for (UUID id : gangs.keySet()) {
            if (id.equals(gangId)) return gangs.get(id);
        }
        return null;
    }

    public static Gang getGang(UUID playerId) {
        for (Gang gang : gangs.values()) {
            if (gang.isMember(playerId)) return gang;
        }
        return null;
    }

    public static Gang getGang(String name) {
        for (Gang gang : gangs.values()) {
            if (gang.getName().equalsIgnoreCase(name)) return gang;
        }
        return null;
    }

    public static RequestManager getRequestManager() {
        return requestManager;
    }

    public static boolean gangAlreadyExists(String name) {
        for (Gang gang : gangs.values()) {
            if (gang.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static Map<UUID, Gang> getGangs() {
        return gangs;
    }
}
