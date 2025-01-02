package org.diaduck.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelloCommand implements Command {
    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Hello, " + event.getAuthor().getAsMention() + "!").queue();
    }
}