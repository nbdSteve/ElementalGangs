package gg.steve.elemental.gangs.bank;

import gg.steve.elemental.gangs.gui.AbstractGui;
import gg.steve.elemental.gangs.utils.GuiItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TopGui extends AbstractGui {
    private YamlConfiguration config;

    public TopGui(YamlConfiguration config) {
        super(config, config.getString("type"), config.getInt("size"));
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
            switch (config.getString(entry + ".gang").toLowerCase()) {
                case "close":
                    setItemInSlot(config.getInt(entry + ".slot"), GuiItemUtil.createItem(config, entry), HumanEntity::closeInventory);
                    break;
                default:
                    setItemInSlot(config.getInt(entry + ".slot"), GuiItemUtil.createGangItem(config, entry), player -> {});
                    break;
            }
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
