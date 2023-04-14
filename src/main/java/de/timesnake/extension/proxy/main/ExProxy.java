/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.main;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import de.timesnake.basic.proxy.util.Network;
import de.timesnake.extension.proxy.cmd.BugFindCmd;
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
import java.util.List;
import java.util.logging.Logger;

@Plugin(id = "extension-proxy", name = "ExProxy", version = "1.0-SNAPSHOT", url = "https://git.timesnake.de", authors = {
        "MarkusNils"}, dependencies = {@Dependency(id = "basic-proxy")})
public class ExProxy {

    public static ExProxy getPlugin() {
        return plugin;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static EventManager getEventManager() {
        return server.getEventManager();
    }

    public static PluginManager getPluginManager() {
        return server.getPluginManager();
    }

    public static CommandManager getCommandManager() {
        return server.getCommandManager();
    }

    private static ExProxy plugin;
    private static ProxyServer server;
    private static Logger logger;

    @Inject
    public ExProxy(ProxyServer server, Logger logger) {
        ExProxy.server = server;
        ExProxy.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin = this;

        Network.getCommandManager().addCommand(this, "service", new CmdService(),
                de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);
        Network.getCommandManager().addCommand(this, "msgspy", new CmdMsgSpy(),
                de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);
        Network.getCommandManager()
                .addCommand(this, "msg", List.of("tell", "message", "pm"), new CmdMsg(),
                        de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);
        Network.getCommandManager().addCommand(this, "r", List.of("response"), new CmdResponse(),
                de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);
        Network.getCommandManager().addCommand(this, "force", List.of("sudo"), new CmdForce(),
                de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);
        Network.getCommandManager()
                .addCommand(this, "lobby", List.of("l", "hub", "menu", "leave"), new CmdLobby(),
                        de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);
        Network.getCommandManager().addCommand(this, "server", new CmdServer(),
                de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);
        Network.getCommandManager().addCommand(this, "switch", new CmdServer(),
                de.timesnake.basic.proxy.util.chat.Plugin.NETWORK);

        Network.getCommandManager().addCommand(this, "mail", new MailCmd(),
                de.timesnake.basic.proxy.util.chat.Plugin.MAILS);
        Network.getCommandManager().addCommand(this, "broadcast", new BroadcastCmd(),
                de.timesnake.library.extension.util.chat.Plugin.NETWORK);
        Network.getCommandManager().addCommand(this, "warn", new CmdWarn(),
                de.timesnake.library.extension.util.chat.Plugin.SYSTEM);
        Network.getCommandManager().addCommand(this, "ping", new PingCmd(),
                de.timesnake.library.extension.util.chat.Plugin.SYSTEM);

        Network.getCommandManager().addCommand(this, "bugfind", new BugFindCmd(),
                de.timesnake.library.extension.util.chat.Plugin.NETWORK);

        server.getEventManager().register(this, new MailHandler());
    }
}
