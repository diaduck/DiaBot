package xyz.diaduck.welcome;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import xyz.diaduck.Channels;

public class WelcomeEmbed {

    public static MessageEmbed welcomeEmbed(Guild guild, Member member) {
        User user = member.getUser();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Welcome to the Server!")
                .setColor(0x325fe5)
                .setDescription("Welcome to the server `%s`".formatted(member.getEffectiveName()))
                .addField("📚 Information", "» Check out the information!\n%s".formatted(Channels.INFO.getAsMention()), true)
                .addField("🗨️ General", "» Talk to people!\n%s".formatted(Channels.GENERAL.getAsMention()), true)
                .addField("📢 Announcements", "» Get the latest news!\n%s".formatted(Channels.ANNOUNCEMENTS.getAsMention()), true)
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setFooter("Server", guild.getIconUrl());
        return embed.build();
    }
}
