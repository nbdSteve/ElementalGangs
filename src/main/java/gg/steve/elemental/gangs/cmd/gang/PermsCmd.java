package gg.steve.elemental.gangs.cmd.gang;

import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermsCmd {

    public static void perms(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return;
        }
        if (!player.hasGangPermission(PermissionNode.PERMS)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.PERMS.get());
            return;
        }
        player.getGang().openPermissionMenu(player.getPlayer());
    }
}
