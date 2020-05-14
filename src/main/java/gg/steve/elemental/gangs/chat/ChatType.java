package gg.steve.elemental.gangs.chat;

import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.utils.ColorUtil;

public enum ChatType {
    GANG(Files.CONFIG.get().getString("chat-prefix.gang")),
    PUBLIC(Files.CONFIG.get().getString("chat-prefix.public"));

    private final String prefix;

    ChatType(String prefix) {
        this.prefix = prefix;
    }

    public void message(GangPlayer gangPlayer, String message) {
        if (gangPlayer.getChatChannel().equals(ChatType.PUBLIC)) return;
        gangPlayer.getGang().messageOnlineMembers(ColorUtil.colorize(prefix.replace("{role}", gangPlayer.getRole().getPrefix()).replace("{player}", gangPlayer.getPlayer().getDisplayName()) + message));
    }
}
