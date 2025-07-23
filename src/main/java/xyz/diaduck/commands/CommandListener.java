package xyz.diaduck.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.diaduck.BotConfig;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        System.out.println("Command handler triggered! From: " + event.getAuthor().getName());

        String message = event.getMessage().getContentRaw();

        if (message.startsWith(BotConfig.getPrefix())) {
            String commandKey = message.substring(BotConfig.getPrefix().length()).split(" ")[0].toLowerCase();
            Command command = CommandRegistry.getCommand(commandKey);


            System.out.println("Executing command: " + commandKey);


            if (command != null) {
                command.execute(event);
            } else {
                event.getChannel().sendMessage("Unknown command. Use " + BotConfig.getPrefix() + "help to see available commands.").queue();
            }
        }
    }
}
