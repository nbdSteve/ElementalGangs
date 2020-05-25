package gg.steve.elemental.gangs.papi;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.bank.BankCalculation;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class GangsExpansion extends PlaceholderExpansion {
    private Gangs instance;

    public GangsExpansion(Gangs instance) {
        this.instance = instance;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return instance.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "gang";
    }

    @Override
    public String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";
        GangPlayer gangPlayer = GangPlayerManager.getGangPlayer(player.getUniqueId());
        if (identifier.equalsIgnoreCase("name")) {
            if (gangPlayer.getGang() == null) {
                return "None";
            }
            return gangPlayer.getGang().getName();
        }
        if (identifier.equalsIgnoreCase("balance")) {
            if (gangPlayer.getGang() == null) {
                return "N/A";
            }
            return Gangs.formatNumber(gangPlayer.getGang().getBank());
        }
        if (identifier.equalsIgnoreCase("position")) {
            if (gangPlayer.getGang() == null) {
                return "N/A";
            }
            return Gangs.formatNumber(BankCalculation.getGangPosition(gangPlayer.getGang()));
        }
        return "debug";
    }
}
