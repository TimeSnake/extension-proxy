/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.*;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Code;
import net.kyori.adventure.text.Component;

public class BugFindCmd implements CommandListener {

  private static final float REWARD = 20;

  private final Code perm = Plugin.NETWORK.createPermssionCode("network.bugfind");

  @Override
  public void onCommand(Sender sender, PluginCommand command, Arguments<Argument> args) {
    if (!sender.hasPermission(this.perm)) {
      return;
    }

    if (!args.isLengthEquals(1, true)) {
      return;
    }

    if (!args.get(0).isPlayerDatabaseName(true)) {
      return;
    }

    DbUser user = args.get(0).toDbUser();

    Network.broadcastMessage(Plugin.NETWORK, Component.text(user.getName(), ExTextColor.VALUE)
        .append(Component.text(" received " + REWARD + " TimeCoins for reporting a bug",
            ExTextColor.PUBLIC)));

    user.addCoins(REWARD);
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
