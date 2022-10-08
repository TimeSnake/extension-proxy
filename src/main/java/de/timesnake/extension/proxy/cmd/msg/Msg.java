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
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Chat;
import de.timesnake.library.extension.util.cmd.ChatDivider;
import net.kyori.adventure.text.Component;

import java.util.HashMap;

public class Msg {

    public static void sendMessageToListeners(User sender, User receiver, String msg) {
        Component holeMsg = Chat.getSenderPlugin(Plugin.PRIVATE_MESSAGES)
                .append(sender.getChatNameComponent())
                .append(Component.text(" " + ChatDivider.COLORED_IN))
                .append(receiver.getChatNameComponent())
                .append(Component.text(ChatDivider.SPLITTER + " "))
                .append(Component.text(msg, ExTextColor.PERSONAL));

        for (User user : Network.getPrivateMessageListeners()) {
            user.sendMessage(holeMsg);
        }
        Network.printText(Plugin.PRIVATE_MESSAGES, holeMsg);
    }

    public static HashMap<User, User> lastPrivateMessageSender = new HashMap<>();
}
