package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.NamedTextColor;
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class BugFindCmd implements CommandListener<Sender, Argument> {

    private static final float REWARD = 19;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> command, Arguments<Argument> args) {
        if (!sender.hasPermission("network.bugfind", 51)) {
            return;
        }

        if (!args.isLengthEquals(1, true)) {
            return;
        }

        if (!args.get(0).isPlayerDatabaseName(true)) {
            return;
        }

        DbUser user = args.get(0).toDbUser();

        Network.broadcastMessage(Plugin.NETWORK, Component.text(user.getName()).color(NamedTextColor.VALUE)
                .append(Component.text(" received " + REWARD + " TimeCoins for reporting a bug").color(NamedTextColor.PUBLIC)));

        user.addCoins(REWARD);
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> exCommand, Arguments<Argument> args) {
        if (args.length() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return List.of();
    }
}
