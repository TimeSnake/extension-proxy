/*
 * extension-proxy.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.extension.proxy.cmd.msg;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ChatDivider;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CmdMsg implements CommandListener<Sender, Argument> {

    private Code.Permission perm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
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
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return List.of();
    }

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("msg", "network.msg.msg");
    }
}
