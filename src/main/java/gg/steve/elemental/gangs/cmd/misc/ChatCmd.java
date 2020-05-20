package gg.steve.elemental.gangs.cmd.misc;

import gg.steve.elemental.gangs.chat.ChatType;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCmd {

    public static void chat(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return;
        }
        if (player.getChatChannel().equals(ChatType.PUBLIC)) {
            if (!player.hasGangPermission(PermissionNode.CHAT_GANG)) {
                MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.CHAT_GANG.get());
                return;
            }
            player.setChatChannel(ChatType.GANG);
            MessageType.CHAT_CHANNEL_GANG.message(player);
        } else {
            if (!player.hasGangPermission(PermissionNode.CHAT_PUBLIC)) {
                MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.CHAT_PUBLIC.get());
                return;
            }
            player.setChatChannel(ChatType.PUBLIC);
            MessageType.CHAT_CHANNEL_PUBLIC.message(player);
        }
    }
}
