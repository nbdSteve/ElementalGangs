package gg.steve.elemental.gangs.cmd.misc;

import gg.steve.elemental.gangs.Gangs;
import gg.steve.elemental.gangs.bank.BankCalculation;
import gg.steve.elemental.gangs.core.Gang;
import gg.steve.elemental.gangs.message.CommandDebug;
import gg.steve.elemental.gangs.message.MessageType;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.player.GangPlayer;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCmd {

    public static void top(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        GangPlayer player = GangPlayerManager.getGangPlayer(((Player) sender).getUniqueId());
        if (!player.hasGangPermission(PermissionNode.TOP)) {
            MessageType.INSUFFICIENT_GANG_PERMISSION.message(player, PermissionNode.TOP.get());
            return;
        }
        int page;
        if (args.length == 1) {
            Gangs.openTopGui(player.getPlayer());
            return;
        } else if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                CommandDebug.INCORRECT_ARGUMENTS.message(player);
                return;
            }
        } else {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(player);
            return;
        }
        MessageType.TOP_HEADER.message(player, String.valueOf(page));
        for (int i = (page - 1) * 9; i <= ((page - 1) * 9 + 8); i++) {
            if (i >= BankCalculation.getGangsInBankOrder().size()) break;
            Gang gang = BankCalculation.getGangsInBankOrder().get(i);
            MessageType.TOP_ENTRY.message(player, Gangs.formatNumber(i + 1), gang.getName(), Gangs.formatNumber(gang.getBank()));
        }
        MessageType.TOP_FOOTER.message(player, String.valueOf(page));
    }
}
