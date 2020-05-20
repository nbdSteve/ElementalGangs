package gg.steve.elemental.gangs.cmd.admin;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import org.bukkit.command.CommandSender;

public class ReloadCmd {

    public static void reload(CommandSender sender) {
        if (!PermissionNode.RELOAD.hasPermission(sender)) {
            MessageType.PERMISSION_DEBUG.message(sender, PermissionNode.RELOAD.get());
            return;
        }
        Gangs.get().onDisable();
        Gangs.get().onEnable();
        Files.reload();
        MessageType.RELOAD.message(sender);
    }
}
