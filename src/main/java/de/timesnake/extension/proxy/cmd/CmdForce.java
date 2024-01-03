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
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.extension.proxy.main.ExProxy;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.Component;

public class CmdForce implements CommandListener {

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.force");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (sender.hasPermission(this.perm)) {
      if (args.isLengthHigherEquals(2, true)) {
        if (args.get(0).isPlayerName(true)) {
          User user = args.get(0).toUser();
          if (sender.hasGroupRankLower(user.getUniqueId())) {
            if (args.get(1).getString().startsWith("/")) {
              String msg = args.toMessage(1);

              ExProxy.getServer().getCommandManager().executeAsync(user.getPlayer(), msg);

              Network.getChannel()
                  .sendMessage(new ChannelUserMessage<>(user.getUniqueId(), MessageType.User.COMMAND, msg));
              sender.sendPluginMessage(Component.text("Forced player ", ExTextColor.PERSONAL)
                      .append(user.getChatNameComponent())
                  .append(Component.text(" to execute command ", ExTextColor.PERSONAL))
                      .append(Component.text(msg, ExTextColor.VALUE)));
            } else {
              sender.sendPluginMessage(Component.text("Only command are permitted", ExTextColor.PERSONAL));
              sender.sendPluginMessage(Component.text("Use /say to force a chat message", ExTextColor.PERSONAL));
            }
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
