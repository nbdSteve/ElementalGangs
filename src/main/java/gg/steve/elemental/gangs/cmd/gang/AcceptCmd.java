package gg.steve.elemental.gangs.cmd.gang;

import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.role.Role;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCmd {

    public static void accept(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(player);
            return;
        }
        if (!player.hasGangPermission(PermissionNode.ACCEPT)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.ACCEPT.get());
            return;
        }
        if (player.hasGang()) {
            CommandDebug.PLAYER_ALREADY_GANG_MEMBER.message(player);
            return;
        }
        if (!GangManager.gangAlreadyExists(args[1])) {
            CommandDebug.GANG_DOES_NOT_EXIST.message(player);
            return;
        }
        Gang gang = GangManager.getGang(args[1]);
        if (!GangManager.getRequestManager().getPendingInvites(player.getPlayerId()).contains(gang)) {
            CommandDebug.NO_GANG_INVITE_PENDING.message(player);
            return;
        }
        assert gang != null;
        if (gang.getPlayers().size() >= Files.CONFIG.get().getInt("max-members")) {
            MessageType.MAX_MEMBERS_JOINER.message(player, gang.getName());
            MessageType.MAX_MEMBERS_GANG.gangMessage(gang, player.getName());
            return;
        }
        GangManager.getRequestManager().removeInviteRequest(gang.getGangId(), player.getPlayerId());
        gang.addPlayer(player.getPlayerId(), Role.MEMBER);
        GangPlayerManager.updateGangPlayer(player.getPlayerId());
        MessageType.INVITE_ACCEPT.gangMessage(gang, player.getName(), gang.getName());
    }
}
