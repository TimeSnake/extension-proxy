package de.timesnake.extension.proxy.cmd.msg;

import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;

import java.util.List;

public class CmdMsgSpy implements CommandListener<Sender, Argument> {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.hasPermission("exproxy.msg.spy", 2107)) {
            if (sender.isPlayer(true)) {
                User user = sender.getUser();
                user.setListeningPrivateMessages(!user.isListeningPrivateMessages());
                if (user.isListeningPrivateMessages()) {
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Enabled private-message messages");
                } else {
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Disabled private-message messages");
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        return null;
    }
}
