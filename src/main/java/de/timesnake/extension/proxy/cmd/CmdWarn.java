/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.CommandListener;
import de.timesnake.basic.proxy.util.chat.Completion;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class CmdWarn implements CommandListener {

  private final Logger logger = LogManager.getLogger("punish.warn");

  private final Code perm = Plugin.NETWORK.createPermssionCode("exproxy.warn");
  private final Code typeNotExists = Plugin.NETWORK.createHelpCode("Warn-Type not exists");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (!sender.hasPermission(this.perm)) {
      return;
    }

    if (!args.isLengthHigherEquals(1, true)) {
      return;
    }

    if (!args.get(0).isPlayerName(true)) {
      return;
    }

    User user = args.get(0).toUser();

    if (!args.isLengthHigherEquals(2, true)) {
      return;
    }

    Type type = Arrays.stream(Type.values()).filter(t -> t.getName().equalsIgnoreCase(args.getString(1))).findFirst().orElse(null);

    if (type == null) {
      sender.sendMessageNotExist(args.getString(1), this.typeNotExists, "warn type");
      return;
    }

    if (type.equals(Type.CUSTOM)) {
      String message = args.toMessage(1);
      user.getPlayer().showTitle(Title.title(Component.text("§cWarning"), Component.text(message)));
    } else {
      user.getPlayer().showTitle(Title.title(Component.text("§cWarning"), Component.text(type.getText())));
    }

    sender.sendPluginMessage(Component.text("Warned player ", ExTextColor.PERSONAL).append(user.getChatNameComponent()));
    user.sendPluginMessage(Plugin.SYSTEM, Component.text(type.getText(), ExTextColor.WARNING));
    this.logger.info("'{}' warned '{}': {}", sender.getChatName(), user.getChatNameComponent(), type.getName());
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(Completion.ofPlayerNames()
            .addArgument(new Completion(Arrays.stream(Type.values()).map(Type::getName).toList())));
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }

  enum Type {
    TEAM("team", "Teaming is forbidden"),
    FAIR_PLAY("fair", "Remember to play fair"),
    SPAM("spam", "Spamming is forbidden"),
    BUG("bug", "Exploiting bugs is forbidden"),
    TIPS("tips", "Giving tips is forbidden"),
    RULES("rules", "Please respect our rules"),
    BETTER("better", "You can do this better"),
    HACKS("hacks", "Big Brother is watching you!"),
    CUSTOM("custom", "");

    private final String name;
    private final String text;

    Type(String name, String text) {
      this.name = name;
      this.text = text;
    }

    public String getName() {
      return name;
    }

    public String getText() {
      return text;
    }
  }
}
