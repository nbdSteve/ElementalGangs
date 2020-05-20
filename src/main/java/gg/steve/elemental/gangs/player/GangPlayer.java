package gg.steve.elemental.gangs.player;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.chat.ChatType;
import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.core.GangManager;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GangPlayer {
    private Gang gang;
    private OfflinePlayer player;
    private ChatType channel;

    public GangPlayer(UUID playerId) {
        this.player = Bukkit.getOfflinePlayer(playerId);
        this.channel = ChatType.PUBLIC;
        this.gang = GangManager.getGang(playerId);
    }

    public boolean isOnline() {
        return this.player.isOnline();
    }

    public Player getPlayer() {
        return this.player.getPlayer();
    }

    public String getName() {
        return this.player.getName();
    }

    public void message(String message) {
        if (!isOnline()) return;
        Bukkit.getPlayer(getPlayerId()).sendMessage(message);
    }

    public boolean hasGang() {
        return this.gang != null;
    }

    public Gang getGang() {
        return gang;
    }

    public Role getRole() {
        if (this.gang == null) return Role.NO_GANG;
        return this.gang.getRoleForPlayer(getPlayerId());
    }

    public boolean hasGangPermission(PermissionNode node) {
        if (this.gang == null) return Role.noGangHasPermission(node);
        return this.gang.roleHasPermission(getRole(), node);
    }

    public boolean deposit(double amount) {
        if (this.gang == null) return false;
        if (Gangs.eco().getBalance(player) >= amount) {
            Gangs.eco().withdrawPlayer(player, amount);
            this.gang.incrementBank(amount);
            return true;
        }
        return false;
    }

    public boolean isGangOwner() {
        return this.gang.getOwnerId().equals(getPlayerId());
    }

    public UUID getPlayerId() {
        return this.player.getUniqueId();
    }

    public ChatType getChatChannel() {
        return channel;
    }

    public void setChatChannel(ChatType channel) {
        this.channel = channel;
    }
}