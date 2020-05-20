package gg.steve.elemental.gangs.cmd.gang;

import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCmd {

    public static void invite(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(player);
            return;
        }
        if (!player.hasGangPermission(PermissionNode.INVITE)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.INVITE.get());
            return;
        }
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return;
        }
        OfflinePlayer offlinePlayer;
        try {
            offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        } catch (Exception e) {
            CommandDebug.TARGET_DOES_NOT_EXIST.message(player);
            return;
        }
        if (!offlinePlayer.hasPlayedBefore()) {
            CommandDebug.TARGET_NOT_PLAYED_BEFORE.message(player);
            return;
        }
        GangPlayer target = GangPlayerManager.getGangPlayer(offlinePlayer.getUniqueId());
        if (target.getGang() != null && target.getGang().getGangId().equals(player.getGang().getGangId())) {
            CommandDebug.PLAYER_GANG_MEMBER.message(player);
            return;
        }
        if (GangManager.getRequestManager().getPendingInvites(target.getPlayerId()).contains(player.getGang())) {
            CommandDebug.TARGET_ALREADY_INVITED.message(player);
            return;
        }
        GangManager.getRequestManager().addInviteRequest(player.getGang().getGangId(), target.getPlayerId());
        MessageType.INVITE.gangMessage(player.getGang(), target.getName(), player.getName());
        if (target.isOnline()) {
            MessageType.INVITE_RECEIVER.message(target, player.getGang().getName(), player.getName());
        }
    }
}
