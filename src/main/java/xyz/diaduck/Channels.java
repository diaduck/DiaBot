package xyz.diaduck;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class Channels {
    public static TextChannel INFO;
    public static TextChannel GENERAL;
    public static TextChannel ANNOUNCEMENTS;
    public static TextChannel WELCOME;

    public static void initChannels(JDA jda) {
        INFO = jda.getTextChannelById("1397571834874036376");
        GENERAL = jda.getTextChannelById("1382420528186527766");
        ANNOUNCEMENTS = jda.getTextChannelById("1397571851940528219");
        WELCOME = jda.getTextChannelById("1397571863667933345");
    }
}
