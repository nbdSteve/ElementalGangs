package gg.steve.elemental.gangs.permission;

import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.gui.AbstractGui;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.role.Role;
import gg.steve.elemental.gangs.utils.GuiItemUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class PermissionPageGui extends AbstractGui {
    private YamlConfiguration config;
    private Role role;
    private Gang gang;

    public PermissionPageGui(YamlConfiguration config, Role role, Gang gang) {
        super(config, config.getString("type"), role, config.getInt("size"));
        this.config = config;
        this.role = role;
        this.gang = gang;
        refresh();
    }

    public void refresh() {
        for (String entry : config.getKeys(false)) {
            try {
                Integer.parseInt(entry);
            } catch (Exception e) {
                continue;
            }
            setItemInSlot(config.getInt(entry + ".slot"), GuiItemUtil.createItem(config, entry), player -> {
                switch (config.getString(entry + ".action")) {
                    case "close":
                        player.closeInventory();
                        break;
                    case "back":
                        this.gang.openPermissionMenu(player);
                        break;
                }
            });
        }
        int slot = 0;
        for (String node : config.getStringList("nodes-in-gui")) {
            setItemInSlot(slot, GuiItemUtil.createNodeItem(config, PermissionNode.valueOf(node), role, gang), player -> {
                PermissionNode.valueOf(node).onClick(gang, role);
            });
            slot++;
        }
    }
}
