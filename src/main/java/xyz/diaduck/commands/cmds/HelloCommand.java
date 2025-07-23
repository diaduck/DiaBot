package xyz.diaduck.commands.cmds;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.diaduck.commands.Command;

public class HelloCommand implements Command {
    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Hello, " + event.getAuthor().getAsMention() + "!").queue();
    }
}