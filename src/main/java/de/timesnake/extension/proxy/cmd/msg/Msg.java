package de.timesnake.extension.proxy.cmd.msg;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.extension.util.chat.Chat;
import de.timesnake.library.extension.util.cmd.ChatDivider;

import java.util.HashMap;

public class Msg {

    public static HashMap<User, User> lastPrivateMessageSender = new HashMap<>();

    public static void sendMessageToListeners(User sender, User receiver, String msg) {
        String holeMsg = Chat.getSenderPlugin(Plugin.PRIVATE_MESSAGES) + sender.getChatName() + " " + ChatDivider.COLORED_IN + receiver.getChatName() + ChatDivider.SPLITTER + " " + ChatColor.PERSONAL + msg;

        for (User user : Network.getPrivateMessageListeners()) {
            user.sendMessage(holeMsg);
        }
        Network.printText(Plugin.PRIVATE_MESSAGES, holeMsg);
    }
}
