/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.proxy.cmd.service;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;
import net.kyori.adventure.text.Component;

public class CmdService implements CommandListener<Sender, Argument> {

    private Code.Permission perm;
    private Code.Permission otherPerm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.isLengthEquals(0, false)) {
            if (sender.isPlayer(true)) {
                if (!sender.hasPermission(this.perm)) {
                    return;
                }
                User user = sender.getUser();
                user.setService(!user.isService());
                sender.sendPluginMessage(Component.text("Updated service-mode to ", ExTextColor.PERSONAL)
                        .append(Component.text(user.isService(), ExTextColor.VALUE)));
            }
        } else if (args.isLengthEquals(1, true)) {
            if (!sender.hasPermission(this.otherPerm)) {
                return;
            }
            if (args.get(0).isPlayerName(true)) {
                User user = args.get(0).toUser();
                user.setService(!user.isService());
                user.sendPluginMessage(Plugin.NETWORK, Component.text("Updated service-mode to ", ExTextColor.PERSONAL)
                        .append(Component.text(user.isService(), ExTextColor.VALUE)));
                if (!sender.getName().equals(user.getName())) {
                    sender.sendPluginMessage(Component.text("Updated service-mode for player ", ExTextColor.PERSONAL)
                            .append(user.getChatNameComponent())
                            .append(Component.text(" to ", ExTextColor.PERSONAL))
                            .append(Component.text(user.isService(), ExTextColor.VALUE)));
                }
            }
        } else {
            sender.sendMessageCommandHelp("Set mode for player", "service [player]");
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return null;
    }

    @Override
    public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {
        this.perm = plugin.createPermssionCode("ser", "exproxy.service");
        this.otherPerm = plugin.createPermssionCode("ser", "exproxy.service.other");
    }
}
