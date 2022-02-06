package de.timesnake.extension.proxy.cmd.server;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.proxy.chat.Plugin;
import de.timesnake.library.basic.util.cmd.Arguments;
import de.timesnake.library.basic.util.cmd.CommandListener;
import de.timesnake.library.basic.util.cmd.ExCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

public class CmdLobby implements CommandListener<Sender, Argument> {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.isLengthEquals(0, false)) {
            if (sender.hasPermission("exproxy.server.lobby", 2103)) {
                if (sender.isPlayer(true)) {
                    User user = sender.getUser();
                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(user.getDbUser().getServerLobby().getName());
                    user.connect(serverInfo);
                    user.sendPluginMessage(Plugin.EX_PROXY, ChatColor.PERSONAL + "Switched to lobby " + ChatColor.VALUE + serverInfo.getName());
                }
            }
        } else if (args.isLengthEquals(1, true)) {
            if (sender.hasPermission("exproxy.server.lobby.other", 2104)) {
                if (args.get(0).isPlayerName(true)) {
                    User user = args.get(0).toUser();
                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(user.getDbUser().getServerLobby().getName());
                    user.connect(serverInfo);
                    user.sendPluginMessage(Plugin.EX_PROXY, ChatColor.PERSONAL + "Switched to lobby " + ChatColor.VALUE + serverInfo.getName());
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Switched player " + ChatColor.VALUE + user.getChatName() + ChatColor.PERSONAL + " to lobby " + ChatColor.VALUE + serverInfo.getName());
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
