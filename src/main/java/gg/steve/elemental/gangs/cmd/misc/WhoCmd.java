package gg.steve.elemental.gangs.cmd.misc;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.core.Gang;
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

public class WhoCmd {

    public static void who(CommandSender sender, String[] args) {
        Gang gang = null;
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
                return;
            }
            GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
            gang = own(player);
        } else if (args.length == 2) {
            if (!GangManager.gangAlreadyExists(args[1])) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                if (player.hasPlayedBefore()) {
                    GangPlayer offline = GangPlayerManager.getGangPlayer(player.getUniqueId());
                    if (offline.hasGang()) {
                        gang = offline.getGang();
                    } else {
                        CommandDebug.GANG_DOES_NOT_EXIST.message(sender);
                        return;
                    }
                } else {
                    CommandDebug.GANG_DOES_NOT_EXIST.message(sender);
                    return;
                }
            }
            if (gang == null) {
                gang = GangManager.getGang(args[1]);
            }
        } else {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(sender);
            return;
        }
        if (gang == null) {
            return;
        }
        MessageType.WHO.message(sender,
                gang.getName(),
                gang.getFounded(),
                Gangs.formatNumber(gang.getBank()),
                gang.getNumberOnline(),
                gang.getOnlinePlayersAsString(),
                gang.getNumberOffline(),
                gang.getOfflinePlayersAsString(),
                gang.getTotalPlayers());
    }

    public static Gang own(GangPlayer player) {
        if (!player.hasGangPermission(PermissionNode.WHO)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.WHO.get());
            return null;
        }
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return null;
        }
        return player.getGang();
    }
}
