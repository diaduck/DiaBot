package xyz.diaduck;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import xyz.diaduck.commands.Command;
import xyz.diaduck.commands.CommandListener;
import xyz.diaduck.commands.CommandRegistry;
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
                            GatewayIntent.GUILD_MEMBERS
                    )
                    .addEventListeners(new CommandListener())
                    .addEventListeners(new WelcomeListener())
                    .build();

            jda.awaitReady();
            Channels.initChannels(jda);
            System.out.println("Bot is ready and online!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}