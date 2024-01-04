/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.CommandListener;
import de.timesnake.basic.proxy.util.chat.Completion;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;

public class BroadcastCmd implements CommandListener {

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.broadcast");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (!sender.hasPermission(this.perm)) {
      return;
    }

    if (!args.isLengthHigherEquals(1, true)) {
      return;
    }

    String message = args.toMessage();
    Network.broadcastMessage(Plugin.INFO, Component.text(message, ExTextColor.WARNING));
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm);
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
