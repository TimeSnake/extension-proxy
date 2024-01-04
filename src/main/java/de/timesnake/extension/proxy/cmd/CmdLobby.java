/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.CommandListener;
import de.timesnake.basic.proxy.util.chat.Completion;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.proxy.main.ExProxy;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;

public class CmdLobby implements CommandListener {

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.server.lobby");
  private final Code otherPerm = Plugin.NETWORK.createPermssionCode("exproxy.server.lobby.other");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (args.isLengthEquals(0, false)) {
      if (sender.hasPermission(this.perm)) {
        if (sender.isPlayer(true)) {
          User user = sender.getUser();
          RegisteredServer server = ExProxy.getServer().getServer(user.getDatabase().getServerLobby().getName()).get();
          user.connect(server);
          user.sendPluginMessage(Plugin.NETWORK,
              Component.text("Switched to lobby ", ExTextColor.PERSONAL)
                  .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
        }
      }
    } else if (args.isLengthEquals(1, true)) {
      if (sender.hasPermission(this.otherPerm)) {
        if (args.get(0).isPlayerName(true)) {
          User user = args.get(0).toUser();
          RegisteredServer server = ExProxy.getServer().getServer(user.getDatabase().getServerLobby().getName()).get();
          user.connect(server);
          user.sendPluginMessage(Plugin.NETWORK, Component.text("Switched to lobby ", ExTextColor.PERSONAL)
              .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
          sender.sendPluginMessage(Component.text("Switched player ", ExTextColor.PERSONAL)
                  .append(user.getChatNameComponent())
                  .append(Component.text(" to lobby ", ExTextColor.PERSONAL))
              .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
        }
      }

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
