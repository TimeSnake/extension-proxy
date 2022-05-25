package de.timesnake.extension.proxy.cmd.service;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;

import java.util.List;

public class CmdService implements CommandListener<Sender, Argument> {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.isLengthEquals(0, false)) {
            if (sender.isPlayer(true)) {
                if (!sender.hasPermission("exproxy.service", 2110)) {
                    return;
                }
                User user = sender.getUser();
                user.setService(!user.isService());
                sender.sendPluginMessage(ChatColor.PERSONAL + "Updated service-mode to " + ChatColor.VALUE + user.isService());
            }
        } else if (args.isLengthEquals(1, true)) {
            if (!sender.hasPermission("exproxy.service.other", 2111)) {
                return;
            }
            if (args.get(0).isPlayerName(true)) {
                User user = args.get(0).toUser();
                user.setService(!user.isService());
                user.sendPluginMessage(Plugin.NETWORK,
                        ChatColor.PERSONAL + "Updated service-mode to " + ChatColor.VALUE + user.isService());
                if (!sender.getName().equals(user.getName())) {
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Updated service-mode for player " + ChatColor.VALUE + user.getChatName() + ChatColor.PERSONAL + " to " + ChatColor.VALUE + user.isService());
                }
            }
        } else {
            sender.sendMessageCommandHelp("Set mode for player", "service [player]");
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return null;
    }
}
