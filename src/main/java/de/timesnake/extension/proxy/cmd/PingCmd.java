/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;

public class PingCmd implements CommandListener<Sender, Argument> {

  @Override
  public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
    if (!sender.isPlayer(true)) {
      return;
    }

    sender.sendPluginMessage(Component.text("Pong! ", ExTextColor.PERSONAL)
        .append(Component.text("(" + sender.getUser().getPlayer().getPing() + "ms)",
            ExTextColor.VALUE)));
  }

  @Override
  public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
    return new ArrayList<>();
  }

  @Override
  public void loadCodes(Plugin plugin) {

  }
}
