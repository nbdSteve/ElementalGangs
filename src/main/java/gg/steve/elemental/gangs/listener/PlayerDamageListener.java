package gg.steve.elemental.gangs.listener;

import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        GangPlayer damager = GangPlayerManager.getGangPlayer(event.getDamager().getUniqueId());
        GangPlayer player = GangPlayerManager.getGangPlayer(event.getEntity().getUniqueId());
        if (damager.getGang() == null || player.getGang() == null) return;
        if (player.getGang().getGangId().equals(damager.getGang().getGangId())) {
            MessageType.DAMAGE_GANG_MEMBER.message(damager);
            event.setCancelled(true);
        }
    }
}
