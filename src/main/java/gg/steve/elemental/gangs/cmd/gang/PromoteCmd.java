package gg.steve.elemental.gangs.cmd.gang;

import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromoteCmd {

    public static void promote(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(player);
            return;
        }
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return;
        }
        if (!player.hasGangPermission(PermissionNode.PROMOTE)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.PROMOTE.get());
            return;
        }
        OfflinePlayer offline = Bukkit.getOfflinePlayer(args[1]);
        if (!offline.hasPlayedBefore()) {
            CommandDebug.TARGET_NOT_MEMBER_OF_GANG.message(player);
            return;
        }
        GangPlayer target = GangPlayerManager.getGangPlayer(offline.getUniqueId());
        if (target.getGang() == null || !target.getGang().getGangId().equals(player.getGang().getGangId())) {
            CommandDebug.TARGET_NOT_MEMBER_OF_GANG.message(player);
            return;
        }
        if (target.getPlayerId().equals(player.getPlayerId())) {
            CommandDebug.TARGET_CAN_NOT_BE_SELF.message(player);
            return;
        }
        if (!Role.higherRole(player.getRole(), target.getRole())) {
            CommandDebug.TARGET_SAME_OR_HIGHER_ROLE.message(player);
            return;
        }
        player.getGang().promote(target.getPlayerId());
        MessageType.PROMOTE.gangMessage(player.getGang(), target.getName(), target.getRole().name(), player.getName());
    }
}
