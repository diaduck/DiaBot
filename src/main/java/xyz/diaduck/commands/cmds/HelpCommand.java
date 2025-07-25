package xyz.diaduck.commands.cmds;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.diaduck.commands.Command;

public class HelpCommand implements Command {
    @Override
    public void execute(MessageReceivedEvent event) {
        String helpMessage = """
            Available commands:
            *ping - Check bot latency
            *hello - Get a greeting
            *help - Show this help message
            """;
        event.getChannel().sendMessage(helpMessage).queue();
    }
}
