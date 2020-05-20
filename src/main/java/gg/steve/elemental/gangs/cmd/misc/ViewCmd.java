package gg.steve.elemental.gangs.cmd.misc;

import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewCmd {

    public static void view(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (!player.hasGangPermission(PermissionNode.VIEW)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.VIEW.get());
            return;
        }
        if (GangManager.getRequestManager().hasPendingInvites(player.getPlayerId())) {
            StringBuilder gangs = new StringBuilder();
            for (int i = 0; i < GangManager.getRequestManager().getPendingInvites(player.getPlayerId()).size(); i++) {
                gangs.append(GangManager.getRequestManager().getPendingInvites(player.getPlayerId()).get(i).getName());
                if (i != GangManager.getRequestManager().getPendingInvites(player.getPlayerId()).size() - 1) {
                    gangs.append(", ");
                }
            }
            MessageType.VIEW_INVITES.message(player, gangs.toString());
        } else {
            MessageType.VIEW_INVITES.message(player, "No invites pending");
        }
    }
}
