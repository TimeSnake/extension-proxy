/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd.warn;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public class CmdWarn implements CommandListener<Sender, Argument> {

    public static Type getTypeByName(String name) {
        for (Type type : Type.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getTypeNames() {
        List<String> names = new ArrayList<>();
        for (Type type : Type.values()) {
            names.add(type.getName());
        }
        return names;
    }

    private Code perm;
    private Code typeNotExists;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
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

        Type type = getTypeByName(args.getString(1));

        if (type == null) {
            sender.sendMessageNotExist(args.getString(1), this.typeNotExists, "warn type");
            return;
        }

        if (type.equals(Type.CUSTOM)) {
            String message = args.toMessage(1);
            user.getPlayer()
                    .showTitle(Title.title(Component.text("§cWarning"), Component.text(message)));
        } else {
            user.getPlayer().showTitle(
                    Title.title(Component.text("§cWarning"), Component.text(type.getText())));
        }

        sender.sendPluginMessage(Component.text("Warned player ", ExTextColor.PERSONAL)
                .append(user.getChatNameComponent()));
        user.sendPluginMessage(Plugin.SYSTEM, Component.text(type.getText(), ExTextColor.WARNING));
        Network.printText(Plugin.SYSTEM,
                sender.getChatName() + " warned " + user.getChatNameComponent() + ": "
                        + type.getName());
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        } else if (args.getLength() == 2) {
            return getTypeNames();
        }
        return List.of();
    }

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("exproxy.warn");
        this.typeNotExists = plugin.createHelpCode("Warn-Type not exists");
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
