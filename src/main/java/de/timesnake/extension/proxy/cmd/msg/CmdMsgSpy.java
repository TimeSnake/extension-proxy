/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd.msg;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;
import net.kyori.adventure.text.Component;

public class CmdMsgSpy implements CommandListener<Sender, Argument> {

    private Code perm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        if (sender.hasPermission(this.perm)) {
            if (sender.isPlayer(true)) {
                User user = sender.getUser();
                user.setListeningPrivateMessages(!user.isListeningPrivateMessages());
                if (user.isListeningPrivateMessages()) {
                    sender.sendPluginMessage(Component.text("Enabled private-message messages",
                            ExTextColor.PERSONAL));
                } else {
                    sender.sendPluginMessage(Component.text("Disabled private-message messages",
                            ExTextColor.PERSONAL));
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        return null;
    }

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("exproxy.msg.spy");
    }
}
