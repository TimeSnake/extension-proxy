/*
 * extension-proxy.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.extension.proxy.cmd.server;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.extension.proxy.main.ExProxy;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CmdLobby implements CommandListener<Sender, Argument> {

    private Code.Permission perm;
    private Code.Permission otherPerm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.isLengthEquals(0, false)) {
            if (sender.hasPermission(this.perm)) {
                if (sender.isPlayer(true)) {
                    User user = sender.getUser();
                    RegisteredServer server =
                            ExProxy.getServer().getServer(user.getDatabase().getServerLobby().getName()).get();
                    user.connect(server);
                    user.sendPluginMessage(Plugin.NETWORK, Component.text("Switched to lobby ", ExTextColor.PERSONAL)
                            .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
                }
            }
        } else if (args.isLengthEquals(1, true)) {
            if (sender.hasPermission(this.otherPerm)) {
                if (args.get(0).isPlayerName(true)) {
                    User user = args.get(0).toUser();
                    RegisteredServer server =
                            ExProxy.getServer().getServer(user.getDatabase().getServerLobby().getName()).get();
                    user.connect(server);
                    user.sendPluginMessage(Plugin.NETWORK, Component.text("Switched to lobby ", ExTextColor.PERSONAL)
                            .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
                    sender.sendPluginMessage(Component.text("Switched player ", ExTextColor.PERSONAL)
                            .append(user.getChatNameComponent())
                            .append(Component.text(" to lobby ", ExTextColor.PERSONAL))
                            .append(Component.text(server.getServerInfo().getName(), ExTextColor.VALUE)));
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

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("ntw", "exproxy.server.lobby");
        this.otherPerm = plugin.createPermssionCode("ntw", "exproxy.server.lobby.other");
    }
}
