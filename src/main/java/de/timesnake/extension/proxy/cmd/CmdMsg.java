/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.CommandListener;
import de.timesnake.basic.proxy.util.chat.Completion;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.chat.Chat;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.HashMap;

public class CmdMsg implements CommandListener {

  public static void sendMessageToListeners(User sender, User receiver, String msg) {
    Component holeMsg = Chat.getSenderPlugin(
            de.timesnake.basic.proxy.util.chat.Plugin.PRIVATE_MESSAGES)
        .append(sender.getChatNameComponent())
        .append(Component.text(" " + Chat.COLORED_IN))
        .append(receiver.getChatNameComponent())
        .append(Component.text(Chat.SPLITTER + " "))
        .append(Component.text(msg, ExTextColor.PERSONAL));

    for (User user : Network.getPrivateMessageListeners()) {
      user.sendMessage(holeMsg);
    }

    Loggers.PRIVATE_MESSAGES.info(PlainTextComponentSerializer.plainText().serialize(holeMsg));
  }

  public static HashMap<User, User> lastPrivateMessageSender = new HashMap<>();

  private final Code perm = Plugin.NETWORK.createPermssionCode("network.msg.msg");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (sender.hasPermission(this.perm)) {
      if (args.isLengthHigherEquals(2, true)) {
        if (args.get(0).isPlayerName(true) && sender.isPlayer(true)) {
          User receiver = args.get(0).toUser();
          String msg = args.toMessage(1);

          if (!sender.getUser().equals(receiver)) {
            sender.sendMessage(receiver.getChatNameComponent()
                .append(Chat.COLORED_OUT)
                .append(Component.text(msg, ExTextColor.PERSONAL)));

            receiver.sendMessage(sender.getUser().getChatNameComponent()
                .append(Chat.COLORED_IN)
                .append(Component.text(msg, ExTextColor.PERSONAL)));
            receiver.playSound(ChannelUserMessage.Sound.PLING);
          } else {
            sender.sendMessage(receiver.getChatNameComponent()
                .append(Chat.COLORED_OUT_IN)
                .append(Component.text(msg, ExTextColor.PERSONAL)));
            receiver.playSound(ChannelUserMessage.Sound.PLING);
          }

          sendMessageToListeners(sender.getUser(), receiver, msg);
          lastPrivateMessageSender.put(sender.getUser(), receiver);
          lastPrivateMessageSender.put(receiver, sender.getUser());
        }
      }
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(Completion.ofPlayerNames());
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
