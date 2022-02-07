package de.timesnake.extension.proxy.cmd.warn;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.basic.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class CmdWarn implements CommandListener<Sender, Argument> {

    enum Type {
        TEAM("team", "Teaming is forbidden"), FAIR_PLAY("fair", "Remember to play fair"), SPAM("spam", "Spamming is forbidden"), BUG("bug", "Exploiting bugs is forbidden"), TIPS("tips", "Giving tips is forbidden"), RULES("rules", "Please respect our rules"), BETTER("better", "You can do this better"), HACKS("hacks", "Big Brother is watching you!"), CUSTOM("custom", "");

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

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (!sender.hasPermission("exproxy.warn", 2112)) {
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
            sender.sendMessageNotExist(args.getString(1), 2203, "warn type");
            return;
        }

        if (type.equals(Type.CUSTOM)) {
            String message = args.toMessage(1);
            user.getPlayer().sendTitle(new BungeeTitle().title(new TextComponent("§cWarning")).subTitle(new TextComponent(message)));
        } else {
            user.getPlayer().sendTitle(new BungeeTitle().title(new TextComponent("§cWarning")).subTitle(new TextComponent(type.getText())));
        }

        sender.sendPluginMessage(ChatColor.PERSONAL + "Warned player " + user.getChatName());
        user.sendPluginMessage(Plugin.SYSTEM, ChatColor.WARNING + type.getText());
        Network.printText(Plugin.SYSTEM, sender.getChatName() + " warned " + user.getChatName() + ": " + type.getName());
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        } else if (args.getLength() == 2) {
            return getTypeNames();
        }
        return List.of();
    }

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
}
