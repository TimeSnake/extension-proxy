package de.timesnake.extension.proxy.cmd.force;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.md_5.bungee.api.ProxyServer;

import java.util.List;

public class CmdForce implements CommandListener<Sender, Argument> {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.hasPermission("exproxy.force", 2108)) {
            if (args.isLengthHigherEquals(2, true)) {
                if (args.get(0).isPlayerName(true)) {
                    User user = args.get(0).toUser();
                    if (sender.hasGroupRankLower(user.getUniqueId())) {
                        if (args.get(1).getString().startsWith("/")) {
                            String msg = args.toMessage(1);

                            ProxyServer.getInstance().getPluginManager().dispatchCommand(user.getPlayer(), msg);

                            Network.getChannel().sendMessage(new ChannelUserMessage<>(user.getUniqueId(),
                                    MessageType.User.COMMAND, msg));
                            sender.sendPluginMessage(ChatColor.PERSONAL + "Forced player " + ChatColor.VALUE + user.getChatName() + ChatColor.PERSONAL + " to execute command " + ChatColor.VALUE + msg);
                        } else {
                            sender.sendPluginMessage(ChatColor.WARNING + "Only command are permitted");
                            sender.sendPluginMessage(ChatColor.PERSONAL + "Use /say to force a chat message");
                        }
                    }
                }
            }
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
