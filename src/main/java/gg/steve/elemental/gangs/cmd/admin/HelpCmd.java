package gg.steve.elemental.gangs.cmd.admin;

import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import org.bukkit.command.CommandSender;

public class HelpCmd {

    public static void help(CommandSender sender) {
        if (!PermissionNode.HELP.hasPermission(sender)) {
            MessageType.PERMISSION_DEBUG.message(sender, PermissionNode.RELOAD.get());
            return;
        }
        MessageType.HELP.message(sender);
    }
}
