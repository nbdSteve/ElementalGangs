package gg.steve.elemental.gangs.message;

import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum MessageType {
    RELOAD("reload"),
    HELP("help"),
    GANG_CREATION("gang-creation", "{gang}"),
    GANG_DISBAND("gang-disband", "{gang}"),
    CHAT_CHANNEL_GANG("chat-channel-gang"),
    CHAT_CHANNEL_PUBLIC("chat-channel-public"),
    INSUFFICIENT_GANG_PERMISSION("insufficient-gang-permission", "{node}"),
    INVITE("invite", "{target}", "{player}"),
    INVITE_RECEIVER("invite-receiver", "{gang}", "{player}"),
    VIEW_INVITES("view-invites", "{gangs}"),
    INVITE_ACCEPT("invite-accept", "{target}", "{gang}"),
    INVITE_PENDING("invite-pending", "{number}"),
    REJECT("reject", "{gang}"),
    REJECT_RECEIVER("reject-receiver", "{player}", "{gang}"),
    LEAVE("leave", "{player}", "{gang}"),
    DEPOSIT_SUCCESS("deposit-success", "{player}", "{deposit}", "{total}"),
    DEPOSIT_FAILED("deposit-failed"),
    KICK("kick", "{target}", "{gang}", "{player}"),
    OFFLINE_KICK("offline-kick", "{gang}"),
    DAMAGE_GANG_MEMBER("damage-gang-member"),
    PROMOTE("promote", "{target}", "{role}", "{player}"),
    DEMOTE("demote", "{target}", "{role}", "{player}"),
    TAG_CHANGE("tag-change", "{player}", "{old}", "{new}"),
    // top
    TOP_HEADER("gang-top.header", "{page}"),
    TOP_ENTRY("gang-top.entry", "{rank}", "{gang}", "{bank}"),
    TOP_FOOTER("gang-top.footer", "{page}"),
    // who
    WHO("gang-who", "{gang}", "{founded}", "{bank}", "{online-number}", "{online}", "{offline-number}", "{offline}", "{total-members}"),
    PERMISSION_DEBUG("permission-debug", "{node}");

    private String path;
    private List<String> placeholders;

    MessageType(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.MESSAGES.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.MESSAGES.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(GangPlayer receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.MESSAGES.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.message(ColorUtil.colorize(line));
        }
    }

    public void gangMessage(Gang gang, String... replacements) {
        for (UUID player : gang.getOnlinePlayers()) {
            GangPlayer receiver = GangPlayerManager.getGangPlayer(player);
            List<String> data = Arrays.asList(replacements);
            for (String line : Files.MESSAGES.get().getStringList(this.path)) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                receiver.message(ColorUtil.colorize(line));
            }
        }
    }

    public static void doMessage(Player receiver, List<String> lines) {
        for (String line : lines) {
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }
}