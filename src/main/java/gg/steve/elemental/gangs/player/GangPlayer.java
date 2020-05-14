package gg.steve.elemental.gangs.player;

import gg.steve.elemental.gangs.chat.ChatType;
import gg.steve.elemental.gangs.core.Gang;
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

    public boolean hasGang() {
        return this.gang != null;
    }

    public Gang getGang() {
        return gang;
    }

    public ChatType getChatChannel() {
        return channel;
    }

    public void setChatChannel(ChatType channel) {
        this.channel = channel;
    }
}
