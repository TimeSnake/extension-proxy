/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.proxy.cmd;

import de.timesnake.basic.proxy.util.Network;
import de.timesnake.basic.proxy.util.chat.Argument;
import de.timesnake.basic.proxy.util.chat.Sender;
import de.timesnake.database.util.Database;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.CommandListener;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;
import java.util.UUID;

public class MailCmd implements CommandListener<Sender, Argument> {


  @Override
  public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
      Arguments<Argument> args) {
    MailHandler mailer = new MailHandler(sender);

    if (!sender.isPlayer(true)) {
      return;
    }

    if (args.isLengthEquals(0, false)) {
      mailer.printMails();
      return;
    }

    switch (args.getString(0)) {
      case "show" -> {
        if (args.isLengthEquals(1, false)) {
          mailer.printMails();
        } else {
          if (!args.get(1).isInt(true)) {
            return;
          }

          mailer.setId(args.get(1).toInt());
          mailer.printMail();
        }
      }
      case "send" -> {
        if (!args.isLengthHigherEquals(2, true)) {
          sender.sendTDMessageCommandHelp("Send mail",
              "mail send <receiverName> {<message>}");
          return;
        }
        if (!args.get(1).isPlayerDatabaseName(true)) {
          return;
        }
        if (!args.isLengthHigherEquals(3, true)) {
          sender.sendTDMessageCommandHelp("Send mail",
              "mail send <receiverName> {<message>}");
          return;
        }
        UUID uuid = Database.getUsers().getUser(args.getString(1)).getUniqueId();
        String msg = args.toMessage(2);
        mailer.sendMail(uuid, msg);
      }
      case "delete" -> {
        if (!args.get(1).isInt(true)) {
          return;
        }
        mailer.setId(args.get(1).toInt());
        mailer.deleteMail();
      }
      default -> {
        sender.sendTDMessageCommandHelp("Show mails", "mail show");
        sender.sendTDMessageCommandHelp("Show mail", "mail show <id>");
        sender.sendTDMessageCommandHelp("Send mail",
            "mail send <receiverName> {<message>}");
        sender.sendTDMessageCommandHelp("Delete mail", "mail delete <id>");
      }
    }
  }

  @Override
  public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
      Arguments<Argument> args) {
    int length = args.getLength();
    if (length == 1) {
      return List.of("send", "show", "delete");
    }
    if (length == 2) {
      return Network.getCommandManager().getPlayerNames();
    }
    return null;
  }

  @Override
  public void loadCodes(Plugin plugin) {

  }
}
