package gg.steve.elemental.gangs.cmd.gang;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositCmd {

    public static void deposit(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(player);
            return;
        }
        if (!player.hasGangPermission(PermissionNode.DEPOSIT)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.DEPOSIT.get());
            return;
        }
        if (!player.hasGang()) {
            CommandDebug.PLAYER_NOT_GANG_MEMBER.message(player);
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (Exception e) {
            CommandDebug.INVALID_DEPOSIT.message(player);
            return;
        }
        if (player.deposit(amount)) {
            MessageType.DEPOSIT_SUCCESS.message(player, player.getName(), Gangs.formatNumber(amount), Gangs.formatNumber(player.getGang().getBank()));
            return;
        }
        MessageType.DEPOSIT_FAILED.message(player);
    }
}
