package de.timesnake.extension.proxy.chat;

public class Plugin extends de.timesnake.basic.proxy.util.chat.Plugin {

    public static final Plugin EX_PROXY = new Plugin("Network", "EXP");
    public static final Plugin PRIVATE_MESSAGES = new Plugin("Msg", "XPM");
    public static final Plugin MAILS = new Plugin("Mails", "XMA");

    protected Plugin(String name, String code) {
        super(name, code);
    }
}
