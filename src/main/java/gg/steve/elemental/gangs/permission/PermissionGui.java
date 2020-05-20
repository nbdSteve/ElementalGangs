package gg.steve.elemental.gangs.permission;

import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.gui.AbstractGui;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.role.Role;
import gg.steve.elemental.gangs.utils.GuiItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PermissionGui extends AbstractGui {
    private YamlConfiguration config;

    public PermissionGui(YamlConfiguration config, Gang gang) {
        super(config, config.getString("type"), gang, config.getInt("size"));
        this.config = config;
        for (int i = 0; i < config.getInt("size"); i++) {
            if (i % 2 == 0) {
                setItemInSlot(i, getFillerGlass((byte) 10), player -> {
                });
            } else {
                setItemInSlot(i, getFillerGlass((byte) 0), player -> {
                });
            }
        }
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
                GangPlayer gangPlayer = GangPlayerManager.getGangPlayer(player.getUniqueId());
                switch (config.getString(entry + ".action").toLowerCase()) {
                    case "close":
                        player.closeInventory();
                        break;
                    default:
                        Role role = Role.valueOf(config.getString(entry + ".action").toUpperCase());
                        if (Role.higherRole(role, gangPlayer.getRole())) {
                            CommandDebug.PERMISSION_SAME_OR_HIGHER_RANK.message(gangPlayer);
                            player.closeInventory();
                            return;
                        }
                        gangPlayer.getGang().openPermissionPageGui(role, player);
                        break;
                }
            });
        }
    }

    public ItemStack getFillerGlass(byte data) {
        ItemStack item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
}
