package xyz.diaduck.commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import xyz.diaduck.selfroles.ReactionRoleEmbedCommand;

import java.util.HashMap;
import java.util.Map;

public class SlashCommandListener extends ListenerAdapter {
    private final ReactionRoleEmbedCommand embedCreator;

    public SlashCommandListener(ReactionRoleEmbedCommand embedCreator) {
        this.embedCreator = embedCreator;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "createreactionrolesembed" -> {
                Guild guild = event.getGuild();
                TextChannel channel = event.getChannel().asTextChannel();

                // Hardcoded mapping for demo purposes — change to dynamic if needed
                Map<String, Role> reactionRoles = new HashMap<>();
                reactionRoles.put("🎮", guild.getRoleById("ROLE_ID_1"));
                reactionRoles.put("🎨", guild.getRoleById("ROLE_ID_2"));
                reactionRoles.put("🎵", guild.getRoleById("ROLE_ID_3"));

                embedCreator.createFancyReactionRoleEmbed(channel, "Choose Your Roles", reactionRoles, true);
                event.reply("Reaction role embed created!").setEphemeral(true).queue();
            }

            // You can add other slash command cases here too
        }
    }

}
