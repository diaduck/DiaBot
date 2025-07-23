package xyz.diaduck.commands.cmds;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.diaduck.commands.Command;
import xyz.diaduck.selfroles.EmbedMaker;
import xyz.diaduck.welcome.WelcomeEmbed;

public class CreateEmbedHereCommand implements Command {
    @Override
    public void execute(MessageReceivedEvent event) {
        try {
            var embed = EmbedMaker.reactionRoleEmbed(event.getGuild());
            event.getChannel().sendMessageEmbeds(embed).queue();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
