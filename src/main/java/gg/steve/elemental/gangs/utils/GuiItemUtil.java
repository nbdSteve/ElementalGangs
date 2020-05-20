package gg.steve.elemental.gangs.utils;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.bank.BankCalculation;
import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.role.Role;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GuiItemUtil {

    public static ItemStack createItem(ConfigurationSection section, String entry) {
        ItemBuilderUtil builder;
        if (section.getString(entry + ".material").startsWith("hdb")) {
            String[] parts = section.getString(entry + ".material").split("-");
            try {
                builder = new ItemBuilderUtil(new HeadDatabaseAPI().getItemHead(parts[1]));
            } catch (NullPointerException e) {
                builder = new ItemBuilderUtil(new ItemStack(Material.valueOf("SKULL_ITEM")));
            }
        } else {
            builder = new ItemBuilderUtil(section.getString(entry + ".material"), section.getString(entry + ".data"));
        }
        builder.addName(section.getString(entry + ".name"));
        builder.addLore(section.getStringList(entry + ".lore"));
        builder.addEnchantments(section.getStringList(entry + ".enchantments"));
        builder.addItemFlags(section.getStringList(entry + ".item-flags"));
        return builder.getItem();
    }

    public static ItemStack createGangItem(ConfigurationSection section, String entry) {
        Gang gang;
        try {
            gang = BankCalculation.getGangsInBankOrder().get(section.getInt(entry + ".gang"));
        } catch (Exception e) {
            return new ItemStack(Material.BARRIER);
        }
        ItemBuilderUtil builder = new ItemBuilderUtil(section.getString("gang-item.material"), section.getString("gang-item.data"));
        if (builder.getMaterial().toString().contains("SKULL_ITEM")) {
            SkullMeta meta = (SkullMeta) builder.getItemMeta();
            meta.setOwner(Bukkit.getOfflinePlayer(gang.getOwnerId()).getName());
            builder.setItemMeta(meta);
        }
        builder.addName(section.getString("gang-item.name").replace("{gang}", gang.getName()));
        builder.setLorePlaceholders("{position}", "{bank}", "{online-number}", "{total-members}", "{founded}", "{owner}");
        builder.addLore(section.getStringList("gang-item.lore"),
                Gangs.formatNumber(section.getInt(entry + ".gang") + 1),
                Gangs.formatNumber(gang.getBank()),
                String.valueOf(gang.getNumberOnline()),
                gang.getTotalPlayers(),
                gang.getFounded(),
                Bukkit.getOfflinePlayer(gang.getOwnerId()).getName());
        builder.addEnchantments(section.getStringList("gang-item.enchantments"));
        builder.addItemFlags(section.getStringList("gang-item.item-flags"));
        return builder.getItem();
    }

    public static ItemStack createNodeItem(ConfigurationSection section, PermissionNode node, Role role, Gang gang) {
        ItemBuilderUtil builder;
        if (node.isEditable()) {
            if (node.isEnabled(gang, role)) {
                builder = new ItemBuilderUtil(section.getString("node-enabled.item"), section.getString("node-enabled.data"));
                builder.setLorePlaceholders("{role}");
                builder.addLore(section.getStringList("node-enabled.lore"), role.toString());
            } else {
                builder = new ItemBuilderUtil(section.getString("node-disabled.item"), section.getString("node-disabled.data"));
                builder.setLorePlaceholders("{role}");
                builder.addLore(section.getStringList("node-disabled.lore"), role.toString());
            }
        } else {
            builder = new ItemBuilderUtil(section.getString("node-un-editable.item"), section.getString("node-un-editable.data"));
            builder.setLorePlaceholders("{role}");
            builder.addLore(section.getStringList("node-un-editable.lore"), role.toString());
        }
        builder.addName(section.getString("node-config.name").replace("{node-type}", node.toString()));
        builder.addEnchantments(section.getStringList("node-config.enchantments"));
        builder.addItemFlags(section.getStringList("node-config.item-flags"));
        return builder.getItem();
    }
}
