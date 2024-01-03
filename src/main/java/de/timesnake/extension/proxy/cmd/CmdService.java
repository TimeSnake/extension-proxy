/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.chat.*;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Code;
import net.kyori.adventure.text.Component;

public class CmdService implements CommandListener {

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.service");
  private final Code otherPerm = Plugin.NETWORK.createPermssionCode("exproxy.service.other");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (args.isLengthEquals(0, false)) {
      if (sender.isPlayer(true)) {
        if (!sender.hasPermission(this.perm)) {
          return;
        }
        User user = sender.getUser();
        user.setService(!user.isService());
        sender.sendPluginMessage(
            Component.text("Updated service-mode to ", ExTextColor.PERSONAL)
                .append(Component.text(user.isService(), ExTextColor.VALUE)));
      }
    } else if (args.isLengthEquals(1, true)) {
      if (!sender.hasPermission(this.otherPerm)) {
        return;
      }
      if (args.get(0).isPlayerName(true)) {
        User user = args.get(0).toUser();
        user.setService(!user.isService());
        user.sendPluginMessage(Plugin.NETWORK,
            Component.text("Updated service-mode to ", ExTextColor.PERSONAL)
                .append(Component.text(user.isService(), ExTextColor.VALUE)));
        if (!sender.getName().equals(user.getName())) {
          sender.sendPluginMessage(
              Component.text("Updated service-mode for player ", ExTextColor.PERSONAL)
                  .append(user.getChatNameComponent())
                  .append(Component.text(" to ", ExTextColor.PERSONAL))
                  .append(Component.text(user.isService(), ExTextColor.VALUE)));
        }
      }
    } else {
      sender.sendTDMessageCommandHelp("Set mode for player", "service [player]");
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(Completion.ofPlayerNames().permission(this.otherPerm));
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
