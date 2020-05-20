package gg.steve.elemental.gangs.cmd.gang;

import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCmd {

    public static void tag(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (!player.hasGangPermission(PermissionNode.TAG)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.TAG.get());
            return;
        }
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return;
        }
        if (GangManager.gangAlreadyExists(args[1])) {
            CommandDebug.GANG_ALREADY_EXISTS.message(player);
            return;
        }
        if (args[1].length() < Files.CONFIG.get().getInt("tag-length.minimum") || args[1].length() > Files.CONFIG.get().getInt("tag-length.maximum")) {
            CommandDebug.INVALID_TAG_LENGTH.message(player);
            return;
        }
        if (args[1].contains("&")) {
            CommandDebug.INVALID_NAME.message(player);
            return;
        }
        MessageType.TAG_CHANGE.gangMessage(player.getGang(), player.getName(), player.getGang().getName(), args[1]);
        player.getGang().setName(args[1]);
    }
}