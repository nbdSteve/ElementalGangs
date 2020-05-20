package gg.steve.elemental.gangs.requests;

import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.managers.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class RequestManager {
    private Map<UUID, Map<RequestType, List<UUID>>> pendingRequests;

    public RequestManager(YamlConfiguration config) {
        pendingRequests = new HashMap<>();
        ConfigurationSection invite = config.getConfigurationSection("invite");
        if (invite != null) {
            for (String entry : invite.getKeys(false)) {
                UUID gangId = UUID.fromString(entry);
                pendingRequests.put(gangId, new HashMap<>());
                pendingRequests.get(gangId).put(RequestType.INVITE, new ArrayList<>());
                for (String player : invite.getStringList(entry)) {
                    UUID playerId = UUID.fromString(player);
                    pendingRequests.get(gangId).get(RequestType.INVITE).add(playerId);
                }
            }
        }
        ConfigurationSection kick = config.getConfigurationSection("kick");
        if (kick != null) {
            for (String entry : kick.getKeys(false)) {
                UUID gangId = UUID.fromString(entry);
                pendingRequests.put(gangId, new HashMap<>());
                pendingRequests.get(gangId).put(RequestType.KICK, new ArrayList<>());
                for (String player : kick.getStringList(entry)) {
                    UUID playerId = UUID.fromString(player);
                    pendingRequests.get(gangId).get(RequestType.KICK).add(playerId);
                }
            }
        }
    }

    public boolean hasPendingInvites(UUID playerId) {
        for (UUID gangId : pendingRequests.keySet()) {
            if (!pendingRequests.containsKey(gangId)) continue;
            if (!pendingRequests.get(gangId).containsKey(RequestType.INVITE)) continue;
            if (pendingRequests.get(gangId).get(RequestType.INVITE).contains(playerId)) return true;
        }
        return false;
    }

    public boolean isKickPending(UUID playerId) {
        for (UUID gangId : pendingRequests.keySet()) {
            if (!pendingRequests.containsKey(gangId)) continue;
            if (!pendingRequests.get(gangId).containsKey(RequestType.KICK)) continue;
            if (pendingRequests.get(gangId).get(RequestType.KICK).contains(playerId)) return true;
        }
        return false;
    }

    public List<Gang> getPendingInvites(UUID playerId) {
        List<Gang> pendingInvites = new ArrayList<>();
        for (UUID gangId : pendingRequests.keySet()) {
            if (!pendingRequests.containsKey(gangId)) continue;
            if (!pendingRequests.get(gangId).containsKey(RequestType.INVITE)) continue;
            if (pendingRequests.get(gangId).get(RequestType.INVITE).contains(playerId))
                pendingInvites.add(GangManager.getGangById(gangId));
        }
        return pendingInvites;
    }

    public void addInviteRequest(UUID gangId, UUID targetId) {
        if (!pendingRequests.containsKey(gangId)) pendingRequests.put(gangId, new HashMap<>());
        pendingRequests.get(gangId).computeIfAbsent(RequestType.INVITE, k -> new ArrayList<>());
        pendingRequests.get(gangId).get(RequestType.INVITE).add(targetId);
    }

    public void removeInviteRequest(UUID gangId, UUID targetId) {
        if (!pendingRequests.containsKey(gangId)) return;
        if (pendingRequests.get(gangId).get(RequestType.INVITE) == null) return;
        if (!pendingRequests.get(gangId).get(RequestType.INVITE).contains(targetId)) return;
        pendingRequests.get(gangId).get(RequestType.INVITE).remove(targetId);
    }

    public void addKickRequest(UUID gangId, UUID targetId) {
        if (!pendingRequests.containsKey(gangId)) pendingRequests.put(gangId, new HashMap<>());
        pendingRequests.get(gangId).computeIfAbsent(RequestType.KICK, k -> new ArrayList<>());
        pendingRequests.get(gangId).get(RequestType.KICK).add(targetId);
    }

    public void removeKickRequest(UUID gangId, UUID targetId) {
        if (!pendingRequests.containsKey(gangId)) return;
        if (pendingRequests.get(gangId).get(RequestType.KICK) == null) return;
        if (!pendingRequests.get(gangId).get(RequestType.KICK).contains(targetId)) return;
        pendingRequests.get(gangId).get(RequestType.KICK).remove(targetId);
    }

    public Gang getKickedFrom(UUID playerId) {
        for (UUID gangId : pendingRequests.keySet()) {
            if (pendingRequests.get(gangId).get(RequestType.KICK).contains(playerId))
                return GangManager.getGangById(gangId);
        }
        return null;
    }

    public void saveToFile() {
        YamlConfiguration conf = Files.PENDING.get();
        for (UUID gangId : pendingRequests.keySet()) {
            if (pendingRequests.get(gangId).containsKey(RequestType.INVITE)) {
                List<String> ids = new ArrayList<>();
                for (UUID targetId : pendingRequests.get(gangId).get(RequestType.INVITE)) {
                    ids.add(String.valueOf(targetId));
                }
                conf.set("invite." + gangId, ids);
            }
            if (pendingRequests.get(gangId).containsKey(RequestType.KICK)) {
                List<String> ids = new ArrayList<>();
                for (UUID targetId : pendingRequests.get(gangId).get(RequestType.KICK)) {
                    ids.add(String.valueOf(targetId));
                }
                conf.set("kick." + gangId, ids);
            }
        }
        Files.PENDING.save();
    }
}
