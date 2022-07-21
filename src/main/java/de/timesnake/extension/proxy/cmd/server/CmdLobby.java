package de.timesnake.extension.proxy.cmd.server;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.proxy.main.ExProxy;
import de.timesnake.library.basic.util.chat.ChatColor;
import de.timesnake.library.basic.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;

import java.util.List;

public class CmdLobby implements CommandListener<Sender, Argument> {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.isLengthEquals(0, false)) {
            if (sender.hasPermission("exproxy.server.lobby", 2103)) {
                if (sender.isPlayer(true)) {
                    User user = sender.getUser();
                    RegisteredServer server =
                            ExProxy.getServer().getServer(user.getDbUser().getServerLobby().getName()).get();
                    user.connect(server);
                    user.sendPluginMessage(Plugin.NETWORK,
                            ChatColor.PERSONAL + "Switched to lobby " + ChatColor.VALUE + server.getServerInfo().getName());
                }
            }
        } else if (args.isLengthEquals(1, true)) {
            if (sender.hasPermission("exproxy.server.lobby.other", 2104)) {
                if (args.get(0).isPlayerName(true)) {
                    User user = args.get(0).toUser();
                    RegisteredServer server =
                            ExProxy.getServer().getServer(user.getDbUser().getServerLobby().getName()).get();
                    user.connect(server);
                    user.sendPluginMessage(Plugin.NETWORK,
                            ChatColor.PERSONAL + "Switched to lobby " + ChatColor.VALUE + server.getServerInfo().getName());
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Switched player " + ChatColor.VALUE +
                            user.getChatNameComponent() + ChatColor.PERSONAL + " to lobby " + ChatColor.VALUE + server.getServerInfo().getName());
                }
            }

        }

    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return null;
    }
}
