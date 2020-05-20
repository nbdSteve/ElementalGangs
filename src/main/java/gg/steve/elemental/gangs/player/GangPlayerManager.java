package gg.steve.elemental.gangs.player;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.chat.ChatType;
import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GangPlayerManager implements Listener {
    private static Map<UUID, GangPlayer> players;

    public static void initialise() {
        players = new HashMap<>();
    }

    public static GangPlayer getGangPlayer(UUID playerId) {
        if (!players.containsKey(playerId)) players.put(playerId, new GangPlayer(playerId));
        return players.get(playerId);
    }

    public static void addGangPlayer(UUID playerId) {
        players.put(playerId, new GangPlayer(playerId));
    }

    public static void removeGangPlayer(UUID playerId) {
        players.remove(playerId);
    }

    public static void updateGangPlayer(UUID playerId) {
        ChatType channel = getGangPlayer(playerId).getChatChannel();
        removeGangPlayer(playerId);
        addGangPlayer(playerId);
        getGangPlayer(playerId).setChatChannel(channel);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        addGangPlayer(event.getPlayer().getUniqueId());
        GangPlayer player = getGangPlayer(event.getPlayer().getUniqueId());
        // do shit with request manager for when player joins
        Bukkit.getScheduler().runTaskLater(Gangs.get(), () -> {
            if (GangManager.getRequestManager().hasPendingInvites(player.getPlayerId())) {
                MessageType.INVITE_PENDING.message(player, String.valueOf(GangManager.getRequestManager().getPendingInvites(player.getPlayerId()).size()));
            }
            if (GangManager.getRequestManager().isKickPending(player.getPlayerId())) {
                MessageType.OFFLINE_KICK.message(player, GangManager.getRequestManager().getKickedFrom(player.getPlayerId()).getName());
                GangManager.getRequestManager().removeKickRequest(GangManager.getRequestManager().getKickedFrom(player.getPlayerId()).getGangId(), player.getPlayerId());
            }
        }, 60L);
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        removeGangPlayer(event.getPlayer().getUniqueId());
    }
}
