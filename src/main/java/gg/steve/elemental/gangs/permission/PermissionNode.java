package gg.steve.elemental.gangs.permission;

import gg.steve.elemental.gangs.managers.Files;
import org.bukkit.command.CommandSender;

public enum PermissionNode {
    GUI("command.gui"),
    CAPACITY("gui.increase-capacity"),
    RELOAD("command.reload"),
    SELL("command.sell"),
    ADMIN_SELL("command.admin-sell"),
    HELP("command.help");

    private String path;

    PermissionNode(String path) {
        this.path = path;
    }

    public String get() {
        return Files.PERMISSIONS.get().getString(this.path);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(get());
    }
}