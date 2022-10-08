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

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Plugin;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class BugFindCmd implements CommandListener<Sender, Argument> {

    private static final float REWARD = 19;

    private Code.Permission perm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> command, Arguments<Argument> args) {
        if (!sender.hasPermission(this.perm)) {
            return;
        }

        if (!args.isLengthEquals(1, true)) {
            return;
        }

        if (!args.get(0).isPlayerDatabaseName(true)) {
            return;
        }

        DbUser user = args.get(0).toDbUser();

        Network.broadcastMessage(Plugin.NETWORK, Component.text(user.getName(), ExTextColor.VALUE)
                .append(Component.text(" received " + REWARD + " TimeCoins for reporting a bug", ExTextColor.PUBLIC)));

        user.addCoins(REWARD);
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> exCommand, Arguments<Argument> args) {
        if (args.length() == 1) {
            return Network.getCommandHandler().getPlayerNames();
        }
        return List.of();
    }

    @Override
    public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {
        this.perm = plugin.createPermssionCode("ntw", "network.bugfind");
    }
}
