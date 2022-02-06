package de.timesnake.extension.proxy.main;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.extension.proxy.cmd.PingCmd;
import de.timesnake.extension.proxy.cmd.broadcast.BroadcastCmd;
import de.timesnake.extension.proxy.cmd.force.CmdForce;
import de.timesnake.extension.proxy.cmd.mail.MailCmd;
import de.timesnake.extension.proxy.cmd.mail.MailHandler;
import de.timesnake.extension.proxy.cmd.msg.CmdMsg;
import de.timesnake.extension.proxy.cmd.msg.CmdMsgSpy;
import de.timesnake.extension.proxy.cmd.msg.CmdResponse;
import de.timesnake.extension.proxy.cmd.server.CmdLobby;
import de.timesnake.extension.proxy.cmd.server.CmdServer;
import de.timesnake.extension.proxy.cmd.service.CmdService;
import de.timesnake.extension.proxy.cmd.warn.CmdWarn;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.List;

public class ExProxy extends Plugin {

    private static ExProxy plugin;

    @Override
    public void onEnable() {

        plugin = this;

        PluginManager pm = ProxyServer.getInstance().getPluginManager();

        Network.getCommandHandler().addCommand(this, pm, "service", new CmdService(), de.timesnake.extension.proxy.chat.Plugin.EX_PROXY);
        Network.getCommandHandler().addCommand(this, pm, "msgspy", new CmdMsgSpy(), de.timesnake.extension.proxy.chat.Plugin.EX_PROXY);
        Network.getCommandHandler().addCommand(this, pm, "msg", List.of("tell", "message", "pm"), new CmdMsg(), de.timesnake.extension.proxy.chat.Plugin.EX_PROXY);
        Network.getCommandHandler().addCommand(this, pm, "r", List.of("response"), new CmdResponse(), de.timesnake.extension.proxy.chat.Plugin.EX_PROXY);
        Network.getCommandHandler().addCommand(this, pm, "force", List.of("sudo"), new CmdForce(), de.timesnake.extension.proxy.chat.Plugin.EX_PROXY);
        Network.getCommandHandler().addCommand(this, pm, "lobby", List.of("hub", "menu", "leave"), new CmdLobby(), de.timesnake.extension.proxy.chat.Plugin.EX_PROXY);
        Network.getCommandHandler().addCommand(this, pm, "server", new CmdServer(), de.timesnake.extension.proxy.chat.Plugin.EX_PROXY);
        Network.getCommandHandler().addCommand(this, pm, "mail", new MailCmd(), de.timesnake.extension.proxy.chat.Plugin.MAILS);
        Network.getCommandHandler().addCommand(this, pm, "broadcast", new BroadcastCmd(), de.timesnake.library.basic.util.chat.Plugin.NETWORK);
        Network.getCommandHandler().addCommand(this, pm, "warn", new CmdWarn(), de.timesnake.library.basic.util.chat.Plugin.SYSTEM);
        Network.getCommandHandler().addCommand(this, pm, "ping", new PingCmd(), de.timesnake.library.basic.util.chat.Plugin.SYSTEM);

        pm.registerListener(this, new MailHandler());
    }

    public static ExProxy getPlugin() {
        return plugin;
    }
}
