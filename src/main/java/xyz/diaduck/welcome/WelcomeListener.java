package xyz.diaduck.welcome;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.diaduck.Channels;

public class WelcomeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        var embed = WelcomeEmbed.welcomeEmbed(event.getGuild(), event.getMember());

        // Send it to the welcome channel
        if (Channels.WELCOME != null) {
            Channels.WELCOME.sendMessageEmbeds(embed).queue();
        } else {
            System.err.println("WELCOME channel is not initialized!");
        }
    }
}
