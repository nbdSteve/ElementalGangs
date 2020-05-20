package gg.steve.elemental.gangs.cmd.gang;

import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCmd {

    public static void leave(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (!player.hasGangPermission(PermissionNode.LEAVE)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.LEAVE.get());
            return;
        }
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return;
        }
        if (player.isGangOwner()) {

            return;
        }
        MessageType.LEAVE.gangMessage(player.getGang(), player.getName(), player.getGang().getName());
        player.getGang().removePlayer(player.getPlayerId());
        GangPlayerManager.updateGangPlayer(player.getPlayerId());
    }
}
