/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.CommandListener;
import de.timesnake.basic.proxy.util.chat.Completion;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.library.chat.Chat;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;

public class CmdResponse implements CommandListener {

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.msg.response");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (args.isLengthHigherEquals(1, true)) {
      if (sender.hasPermission(this.perm)) {
        if (sender.isPlayer(true)) {
          if (CmdMsg.lastPrivateMessageSender.containsKey(sender.getUser())) {
            User receiver = CmdMsg.lastPrivateMessageSender.get(sender.getUser());
            String msg = args.toMessage();

            if (!sender.getUser().equals(receiver)) {
              sender.sendMessage(receiver.getChatNameComponent()
                  .append(Chat.COLORED_OUT)
                  .append(Component.text(msg, ExTextColor.VALUE)));

              receiver.sendMessage(sender.getUser().getChatNameComponent()
                  .append(Chat.COLORED_IN)
                  .append(Component.text(msg, ExTextColor.VALUE)));
              receiver.playSound(ChannelUserMessage.Sound.PLING);
            } else {
              sender.sendMessage(receiver.getChatNameComponent()
                  .append(Chat.COLORED_OUT_IN)
                  .append(Component.text(msg, ExTextColor.VALUE)));
              receiver.playSound(ChannelUserMessage.Sound.PLING);
            }

            CmdMsg.lastPrivateMessageSender.put(receiver, sender.getUser());
          } else {
            sender.sendPluginMessage(
                Component.text("No open private chat ", ExTextColor.WARNING)
                    .append(Chat.getMessageCode("H", 2200, Plugin.NETWORK)));
          }
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
