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
