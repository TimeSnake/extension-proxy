/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;
import net.kyori.adventure.text.Component;

public class BugFindCmd implements CommandListener<Sender, Argument> {

  private static final float REWARD = 19;

  private Code perm;

  @Override
  public void onCommand(Sender sender, ExCommand<Sender, Argument> command,
      Arguments<Argument> args) {
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
  public List<String> getTabCompletion(ExCommand<Sender, Argument> exCommand,
      Arguments<Argument> args) {
    if (args.length() == 1) {
      return Network.getCommandManager().getPlayerNames();
    }
    return List.of();
  }

  @Override
  public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {
    this.perm = plugin.createPermssionCode("network.bugfind");
  }
}
