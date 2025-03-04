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
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;

public class CmdForce implements CommandListener {

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.force");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    sender.hasPermissionElseExit(this.perm);
    args.isLengthHigherEqualsElseExit(2, true);
    args.get(0).assertElseExit(a -> a.isPlayerName(true));

    User user = args.get(0).toUser();
    sender.hasGroupRankLowerElseExit(user.getUniqueId(), true);

    if (!args.get(1).getString().startsWith("/")) {
      sender.sendPluginTDMessage("§wOnly command are permitted");
      sender.sendPluginTDMessage("§wUse /say to force a chat message");
    }

    String text = args.toMessage(1);

    ExProxy.getServer().getCommandManager().executeAsync(user.getPlayer(), text);

    Network.getChannel().sendMessage(new ChannelUserMessage<>(user.getUniqueId(), MessageType.User.COMMAND, text));
    sender.sendPluginTDMessage("§sForced player §v" + user.getChatName() + "§s to execute command §v" + text);
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(Completion.ofPlayerNames()
            .addArgument(new Completion("/say", "/<cmd>").allowAny()));
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
