/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.basic.proxy.util.user.User;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.user.DbUserMail;
import de.timesnake.library.chat.Chat;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MailHandler {

  private final ArrayList<Player> joined = new ArrayList<>();
  private Sender sender;
  private Integer id;

  private Code mailNotExists;

  public MailHandler() {
    this.mailNotExists = Plugin.MAILS.createHelpCode("Mail does not exists");
  }

  public MailHandler(Sender sender) {
    this();
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
    if (mails.isEmpty()) {
      sender.sendPluginMessage(Component.text("You have no mails", ExTextColor.WARNING));
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
      this.sender.sendPluginMessage(Component.text("Mail ", ExTextColor.PERSONAL)
          .append(Component.text(mail.getId(), ExTextColor.VALUE)));
      this.sender.sendMessage(Chat.COLORED_SPLITTER
          .append(Component.text(mail.getSenderName(), ExTextColor.VALUE))
          .append(Chat.SPLITTER)
          .append(Component.text(mail.getMessage(), ExTextColor.VALUE)));
    } else {
      this.sender.sendMessageNotExist(String.valueOf(mail.getId()), this.mailNotExists, "Id");
    }
  }

  public void sendMail(UUID receiverUuid, String message) {
    User user = this.sender.getUser();
    try {
      this.id = Database.getUsers().getUser(receiverUuid)
          .addMail(user.getUniqueId(), user.getName(), message);
      this.sender.sendPluginMessage(Component.text("Send mail:", ExTextColor.PERSONAL));
      DbUserMail mail = this.sender.getUser().getDatabase().getMail(this.id);
      if (mail != null) {
        this.sender.sendMessage(Chat.COLORED_IN
            .append(Component.text(mail.getName(), ExTextColor.VALUE))
            .append(Chat.SPLITTER)
            .append(Component.text(mail.getMessage(), ExTextColor.VALUE)));

        User receiverUser = Network.getUser(receiverUuid);
        if (receiverUser != null) {
          receiverUser.sendPluginMessage(Plugin.MAILS,
              Component.text("Received a new mail (id: ", ExTextColor.PERSONAL)
                  .append(Component.text(this.id, ExTextColor.VALUE))
                  .append(Component.text(")", ExTextColor.PERSONAL)));
          receiverUser.getAsSender(Plugin.MAILS)
              .sendTDMessageCommandHelp("Show mail", "mail show <id>");
        }
      }


    } catch (TooLongEntryException e) {
      this.sender.sendPluginMessage(Component.text(e.getMessage(), ExTextColor.WARNING));
    }
  }

  public void deleteMail() {
    this.sender.getUser().getDatabase().deleteMail(this.id);
    this.sender.sendPluginMessage(Component.text("Deleted mail ", ExTextColor.PERSONAL)
        .append(Component.text(this.id, ExTextColor.VALUE)));
  }

  @Subscribe
  public void onPlayerJoin(PostLoginEvent e) {
    joined.add(e.getPlayer());
  }

  @Subscribe
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
    if (!mails.isEmpty()) {
      sender.sendPluginMessage(Component.text("You have ", ExTextColor.PERSONAL)
          .append(Component.text(mails.size(), ExTextColor.VALUE))
          .append(Component.text(" mails", ExTextColor.PERSONAL)));
    }
  }
}
