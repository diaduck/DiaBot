package xyz.diaduck.commands.cmds;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.diaduck.commands.Command;

public class PingCommand implements Command {
    @Override
    public void execute(MessageReceivedEvent event) {
        System.out.println("PingCommand.execute() called!");
        long timestamp = System.currentTimeMillis();
        event.getChannel().sendMessage("Pong! Bot latency: " + event.getJDA().getGatewayPing() + "ms at " + timestamp).queue();
    }
}