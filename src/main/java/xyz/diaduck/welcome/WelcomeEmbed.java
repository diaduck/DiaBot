package xyz.diaduck.welcome;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.FileUpload;
import xyz.diaduck.Channels;

import java.io.File;

public class WelcomeEmbed {

    public static MessageEmbed welcomeEmbed(Guild guild, Member member) {
        User user = member.getUser();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Welcome to Kayleigh's Kingdom!")
                .setColor(0x325fe5)
                .setDescription("Welcome to the kingdom `%s`".formatted(member.getEffectiveName()))
                .addField("📜 Rules", "» Understand the rules!\n%s".formatted(Channels.INFO.getAsMention()), true)
                .addField("🗨️ General", "» Talk to people!\n%s".formatted(Channels.GENERAL.getAsMention()), true)
                .addField("📢 Announcements", "» Get the latest news!\n%s".formatted(Channels.ANNOUNCEMENTS.getAsMention()), true)
                //.setThumbnail("https://tenor.com/view/mario-bros-princess-felicidad-princess-peach-spin-gif-16875329")
                .setImage("https://c.tenor.com/vxklCOowrvQAAAAC/tenor.gif")
                //.setImage("attachment://mario-bros-princess.gif")
                .setFooter("kayleigh’s kingdom", guild.getIconUrl());
        return embed.build();

    }
}
