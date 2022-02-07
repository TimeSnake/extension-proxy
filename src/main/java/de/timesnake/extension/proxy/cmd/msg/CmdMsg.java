package de.timesnake.extension.proxy.cmd.msg;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.channel.api.message.ChannelUserMessage;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ChatDivider;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;

import java.util.List;

public class CmdMsg implements CommandListener<Sender, Argument> {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.hasPermission("exproxy.msg.msg", 2105)) {
            if (args.isLengthHigherEquals(2, true)) {
                if (args.get(0).isPlayerName(true) && sender.isPlayer(true)) {
                    User receiver = args.get(0).toUser();
                    String msg = args.toMessage(1);

                    sender.sendMessage(receiver.getChatName() + ChatDivider.COLORED_OUT + ChatColor.VALUE + msg);

                    receiver.sendMessage(sender.getUser().getChatName() + ChatDivider.COLORED_IN + ChatColor.VALUE + msg);
                    receiver.playSound(ChannelUserMessage.Sound.PLING);

                    Msg.sendMessageToListeners(sender.getUser(), receiver, msg);
                    Msg.lastPrivateMessageSender.put(sender.getUser(), receiver);
                    Msg.lastPrivateMessageSender.put(receiver, sender.getUser());
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return List.of();
    }
}
