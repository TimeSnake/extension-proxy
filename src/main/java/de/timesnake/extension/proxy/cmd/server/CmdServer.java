/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.proxy.cmd.server;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.proxy.main.ExProxy;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;
import net.kyori.adventure.text.Component;

public class CmdServer implements CommandListener<Sender, Argument> {

    private Code.Permission perm;
    private Code.Permission otherPerm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.isLengthHigherEquals(1, true)) {
            if (args.get(0).isServerName(true)) {
                if (args.isLengthEquals(1, false)) {
                    if (sender.hasPermission(this.perm)) {
                        if (sender.isPlayer(true)) {
                            User user = sender.getUser();
                            sendMessageToMovedUser(args, user);
                        }
                    }
                } else if (args.isLengthEquals(2, true)) {
                    if (sender.hasPermission(this.otherPerm)) {
                        if (args.get(1).isPlayerName(true)) {
                            User user = args.get(1).toUser();
                            sendMessageToMovedUser(args, user);
                            RegisteredServer server = ExProxy.getServer().getServer(args.get(0).getString()).get();
                            sender.sendPluginMessage(Component.text("Switched player ", ExTextColor.PERSONAL)
                                    .append(user.getChatNameComponent())
                                    .append(Component.text(" to server ", ExTextColor.PERSONAL))
                                    .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
                        }
                    }
                } else {
                    sender.sendMessageCommandHelp("Switch server", "server <server>");
                }

            }

        } else {
            sender.sendMessageCommandHelp("Switch server", "server <server>");
        }

    }

    private void sendMessageToMovedUser(Arguments<Argument> args, User user) {
        RegisteredServer server = ExProxy.getServer().getServer(args.get(0).getString()).get();
        user.connect(server);
        user.setTask(null);
        user.sendPluginMessage(Plugin.NETWORK, Component.text("Switched to server ", ExTextColor.PERSONAL)
                .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getServerNames();
        }

        if (args.getLength() == 2) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return null;
    }

    @Override
    public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {
        this.perm = plugin.createPermssionCode("ntw", "exproxy.server.switch");
        this.otherPerm = plugin.createPermssionCode("ntw", "exproxy.server.switch.other");
    }


}
