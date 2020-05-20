package gg.steve.elemental.gangs.cmd;

import gg.steve.elemental.gangs.cmd.admin.HelpCmd;
import gg.steve.elemental.gangs.cmd.admin.ReloadCmd;
import gg.steve.elemental.gangs.cmd.gang.*;
import gg.steve.elemental.gangs.cmd.misc.*;
import gg.steve.elemental.gangs.message.CommandDebug;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GangCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            HelpCmd.help(sender);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "create":
                CreateCmd.create(sender, args);
                break;
            case "disband":
                DisbandCmd.disband(sender);
                break;
            case "join":
            case "j":
            case "accept":
            case "a":
                AcceptCmd.accept(sender, args);
                break;
            case "leave":
            case "l":
                LeaveCmd.leave(sender);
                break;
            case "who":
            case "show":
            case "w":
                WhoCmd.who(sender, args);
                break;
            case "invite":
            case "i":
                InviteCmd.invite(sender, args);
                break;
            case "top":
            case "t":
                TopCmd.top(sender, args);
                break;
            case "deposit":
            case "d":
                DepositCmd.deposit(sender, args);
                break;
            case "help":
            case "h":
                HelpCmd.help(sender);
                break;
            case "chat":
            case "c":
                ChatCmd.chat(sender);
                break;
            case "view":
            case "v":
                ViewCmd.view(sender);
                break;
            case "reject":
            case "r":
                RejectCmd.reject(sender, args);
                break;
            case "promote":
                PromoteCmd.promote(sender, args);
                break;
            case "demote":
                DemoteCmd.demote(sender, args);
                break;
            case "kick":
            case "k":
                KickCmd.kick(sender, args);
                break;
            case "permissions":
            case "permission":
            case "perms":
            case "p":
                PermsCmd.perms(sender);
                break;
            case "tag":
            case "name":
                TagCmd.tag(sender, args);
                break;
            case "reload":
                ReloadCmd.reload(sender);
                break;
            default:
                CommandDebug.INVALID_COMMAND.message(sender);
                break;
        }
        return true;
    }
}
