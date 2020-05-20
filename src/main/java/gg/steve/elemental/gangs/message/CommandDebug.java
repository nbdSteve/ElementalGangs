package gg.steve.elemental.gangs.message;

import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum CommandDebug {
    // console debug
    ONLY_PLAYERS_CAN_RUN_COMMAND("only-players-can-invite"),
    // debug messages,
    INCORRECT_ARGUMENTS("incorrect-arguments"),
    GANG_DOES_NOT_EXIST("gang-does-not-exist"),
    CAN_NOT_ACCEPT_INVITE("can-not-accept-invite"),
    NO_INVITE_PENDING("no-invite-pending"),
    INVALID_CHAT_CHANNEL("invalid-chat-channel"),
    CHAT_NO_GANG("chat-no-gang"),
    CREATE_ALREADY_IN_GANG("create-already-in-gang"),
    GANG_ALREADY_EXISTS("gang-already-exists"),
    INVALID_NAME("gang-name-invalid"),
    PLAYER_NOT_GANG_MEMBER("player-not-gang-member"),
    PLAYER_NOT_OWNER("player-not-owner"),
    INVALID_TAG_LENGTH("invalid-tag-length"),
    TARGET_DOES_NOT_EXIST("target-does-not-exist"),
    TARGET_NOT_PLAYED_BEFORE("target-has-not-played"),
    TARGET_ALREADY_INVITED("target-already-invited"),
    PLAYER_ALREADY_GANG_MEMBER("player-already-gang-member"),
    PLAYER_GANG_MEMBER("player-gang-member"),
    NO_GANG_INVITE_PENDING("no-gang-invite-pending"),
    INVALID_DEPOSIT("invalid-deposit"),
    TARGET_NOT_MEMBER_OF_GANG("target-not-member-of-gang"),
    TARGET_CAN_NOT_BE_SELF("target-can-not-be-self"),
    TARGET_SAME_OR_HIGHER_ROLE("target-same-or-higher-role"),
    PERMISSION_SAME_OR_HIGHER_RANK("permission-same-or-higher-rank"),
    //
    INSUFFICIENT_PERMISSION("insufficient-permission", "{node}"),
    INVALID_COMMAND("invalid-command"),
    INVALID_NUMBER_OF_ARGUMENTS("invalid-number-of-arguments"),
    TARGET_NOT_ONLINE("target-not-online"),
    INVALID_SELL_GROUP("invalid-sell-group"),
    ONLY_PLAYERS_ACCESSIBLE("only-player-accessible");

    private final String path;
    private List<String> placeholders;

    CommandDebug(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.DEBUG.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.DEBUG.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(GangPlayer receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.DEBUG.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.message(ColorUtil.colorize(line));
        }
    }
}
