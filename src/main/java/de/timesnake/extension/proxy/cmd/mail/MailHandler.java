package de.timesnake.extension.proxy.cmd.mail;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.ChatColor;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.user.DbUserMail;
import de.timesnake.extension.proxy.chat.Plugin;
import de.timesnake.library.extension.util.cmd.ChatDivider;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MailHandler implements Listener {

    private Sender sender;
    private Integer id;

    public MailHandler() {
    }

    public MailHandler(Sender sender) {
        this.sender = sender;
    }

    public MailHandler(Sender sender, Integer id) {
        this.sender = sender;
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void printMails() {
        Collection<DbUserMail> mails = this.sender.getUser().getDatabase().getMails();
        if (mails.size() == 0) {
            sender.sendPluginMessage(ChatColor.PERSONAL + "You have no mails");
            return;
        }
        for (DbUserMail mail : mails) {
            this.printMail(mail);
        }
    }

    public void printMail() {
        DbUserMail mail = this.sender.getUser().getDatabase().getMail(this.id);
        this.printMail(mail);
    }

    public void printMail(DbUserMail mail) {
        if (mail != null) {
            this.sender.sendPluginMessage(ChatColor.PERSONAL + "Mail " + ChatColor.VALUE + mail.getId());
            this.sender.sendMessage(ChatDivider.COLORED_SPLITTER + mail.getSenderName() + ChatDivider.SPLITTER + mail.getMessage());
        } else {
            this.sender.sendMessageNotExist(String.valueOf(mail.getId()), 2202, "Id");
        }
    }

    public void sendMail(UUID receiverUuid, String message) {
        User user = this.sender.getUser();
        try {
            this.id = Database.getUsers().getUser(receiverUuid).addMail(user.getUniqueId(), user.getName(), message);
            this.sender.sendPluginMessage(ChatColor.PERSONAL + "Send mail:");
            DbUserMail mail = this.sender.getUser().getDatabase().getMail(this.id);
            if (mail != null) {
                this.sender.sendMessage(ChatDivider.COLORED_IN + mail.getName() + ChatDivider.SPLITTER + mail.getMessage());

                User receiverUser = Network.getUser(receiverUuid);
                if (receiverUser != null) {
                    receiverUser.sendPluginMessage(Plugin.MAILS, ChatColor.PERSONAL + "A new mail " + "received (id: " + ChatColor.VALUE + this.id + ChatColor.PERSONAL + ")");
                    receiverUser.getAsSender(Plugin.MAILS).sendMessageCommandHelp("Show mail", "mail show <id>");
                }
            }


        } catch (TooLongEntryException e) {
            this.sender.sendPluginMessage(ChatColor.WARNING + e.getMessage());
        }
    }

    public void deleteMail() {
        this.sender.getUser().getDatabase().deleteMail(this.id);
        this.sender.sendPluginMessage(ChatColor.PERSONAL + "Deleted mail " + ChatColor.VALUE + this.id);
    }

    private final ArrayList<ProxiedPlayer> joined = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        joined.add(e.getPlayer());
    }

    @EventHandler
    public void onPlayerConnectedServer(ServerConnectedEvent e) {
        if (!joined.contains(e.getPlayer())) {
            return;
        }
        joined.remove(e.getPlayer());
        User user = Network.getUser(e.getPlayer());
        if (user == null) {
            return;
        }
        this.sender = user.getAsSender(Plugin.MAILS);
        Collection<DbUserMail> mails = this.sender.getUser().getDatabase().getMails();
        if (mails.size() > 0) {
            sender.sendPluginMessage(ChatColor.PERSONAL + "You have " + ChatColor.VALUE + mails.size() + ChatColor.PUBLIC + " mails");
        }
    }
}
