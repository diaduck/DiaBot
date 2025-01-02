package org.diaduck;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.diaduck.commands.Command;
import org.diaduck.commands.CommandRegistry;

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
                    .addEventListeners(new Main())
                    .build();

            jda.awaitReady();
            System.out.println("Bot is ready and online!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        if (message.startsWith(BotConfig.getPrefix())) {
            String commandKey = message.substring(BotConfig.getPrefix().length()).split(" ")[0].toLowerCase();
            Command command = CommandRegistry.getCommand(commandKey);

            if (command != null) {
                command.execute(event);
            } else {
                event.getChannel().sendMessage("Unknown command. Use " + BotConfig.getPrefix() + "help to see available commands.").queue();
            }
        }
    }
}