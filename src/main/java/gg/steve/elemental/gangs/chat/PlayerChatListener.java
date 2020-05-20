package gg.steve.elemental.gangs.chat;

import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerChat(AsyncPlayerChatEvent event) {
        GangPlayer player = GangPlayerManager.getGangPlayer(event.getPlayer().getUniqueId());
        if (player.getChatChannel().equals(ChatType.GANG)) {
            event.setCancelled(true);
            ChatType.GANG.message(player, event.getMessage());
        }
    }
}
