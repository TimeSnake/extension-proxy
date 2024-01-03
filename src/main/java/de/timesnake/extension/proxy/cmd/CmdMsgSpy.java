/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.CommandListener;
import de.timesnake.basic.proxy.util.chat.Completion;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.Component;

public class CmdMsgSpy implements CommandListener {

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.msg.spy");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd,
      Arguments<Argument> args) {
    if (sender.hasPermission(this.perm)) {
      if (sender.isPlayer(true)) {
        User user = sender.getUser();
        user.setListeningPrivateMessages(!user.isListeningPrivateMessages());
        if (user.isListeningPrivateMessages()) {
          sender.sendPluginMessage(Component.text("Enabled private-message messages",
              ExTextColor.PERSONAL));
        } else {
          sender.sendPluginMessage(Component.text("Disabled private-message messages",
              ExTextColor.PERSONAL));
        }
      }
    }
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
