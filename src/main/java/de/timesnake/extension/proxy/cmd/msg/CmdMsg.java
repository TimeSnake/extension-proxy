/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd.msg;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ChatDivider;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;
import net.kyori.adventure.text.Component;

public class CmdMsg implements CommandListener<Sender, Argument> {

    private Code perm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        if (sender.hasPermission(this.perm)) {
            if (args.isLengthHigherEquals(2, true)) {
                if (args.get(0).isPlayerName(true) && sender.isPlayer(true)) {
                    User receiver = args.get(0).toUser();
                    String msg = args.toMessage(1);

                    if (!sender.getUser().equals(receiver)) {
                        sender.sendMessage(receiver.getChatNameComponent()
                                .append(ChatDivider.COLORED_OUT)
                                .append(Component.text(msg, ExTextColor.PERSONAL)));

                        receiver.sendMessage(sender.getUser().getChatNameComponent()
                                .append(ChatDivider.COLORED_IN)
                                .append(Component.text(msg, ExTextColor.PERSONAL)));
                        receiver.playSound(ChannelUserMessage.Sound.PLING);
                    } else {
                        sender.sendMessage(receiver.getChatNameComponent()
                                .append(ChatDivider.COLORED_OUT_IN)
                                .append(Component.text(msg, ExTextColor.PERSONAL)));
                        receiver.playSound(ChannelUserMessage.Sound.PLING);
                    }

                    Msg.sendMessageToListeners(sender.getUser(), receiver, msg);
                    Msg.lastPrivateMessageSender.put(sender.getUser(), receiver);
                    Msg.lastPrivateMessageSender.put(receiver, sender.getUser());
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return List.of();
    }

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("network.msg.msg");
    }
}
