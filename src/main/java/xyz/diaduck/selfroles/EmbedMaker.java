package xyz.diaduck.selfroles;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import xyz.diaduck.Channels;

public class EmbedMaker {

    public static MessageEmbed reactionRoleEmbed(Guild guild) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Colour Roles!")
                .setColor(0x325fe5)
                .setDescription("")
//                .addField("💜", "%s".formatted(Roles.ROLE1.getAsMention()), false)
//                .addField("💜", "%s".formatted(Roles.ROLE2.getAsMention()), false)
//                .addField("💜", "%s".formatted(Roles.ROLE3.getAsMention()), false)
                .addField("Roles:",
                        "💜 %s".formatted(Roles.ROLE1.getAsMention())
                        + "\n💜 %s".formatted(Roles.ROLE2.getAsMention())
                        + "\n💜 %s".formatted(Roles.ROLE3.getAsMention()),
                        false)
                .setFooter("Kayleigh's Kingdom,", guild.getIconUrl());
        return embed.build();
    }
}
