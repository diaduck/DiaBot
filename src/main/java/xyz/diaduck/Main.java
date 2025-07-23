package xyz.diaduck;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import xyz.diaduck.commands.Command;
import xyz.diaduck.commands.CommandListener;
import xyz.diaduck.commands.CommandRegistry;
import xyz.diaduck.commands.SlashCommandListener;
import xyz.diaduck.selfroles.ReactionRoleListener;
import xyz.diaduck.welcome.WelcomeListener;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        try {
            // Get token from environment variable
            String token = BotConfig.getToken();

            // System.out.println("Using token: " + token);  // Print it out


            // Initialize JDA with necessary intents
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(
                            GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS
                    )
                    .addEventListeners(new CommandListener())
                    .addEventListeners(new WelcomeListener())
                    .addEventListeners(new ReactionRoleListener())
                    .build();

            jda.awaitReady();
            Channels.initChannels(jda);
            System.out.println("Bot is ready and online!");


            // Register slash commands
            jda.updateCommands().addCommands(
                    Commands.slash("reactionrole", "Set up a reaction role")
                            .addOptions(
                                    new OptionData(OptionType.STRING, "message_id", "The message ID to add reaction role to", true),
                                    new OptionData(OptionType.STRING, "emoji", "The emoji to react with", true),
                                    new OptionData(OptionType.ROLE, "role", "The role to assign", true)
                            ),
                    Commands.slash("createreactionrolesembed", "Creates a reaction role embed with predefined emojis and roles")
            ).queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}