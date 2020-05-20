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

import java.util.UUID;

public class CreateCmd {

    public static void create(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(player);
            return;
        }
        if (!player.hasGangPermission(PermissionNode.CREATE)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.CREATE.get());
            return;
        }
        if (player.hasGang()) {
            CommandDebug.CREATE_ALREADY_IN_GANG.message(player);
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
        GangManager.createGang(UUID.randomUUID(), player.getPlayerId(), args[1]);
        MessageType.GANG_CREATION.message(player, args[1]);
    }
}
