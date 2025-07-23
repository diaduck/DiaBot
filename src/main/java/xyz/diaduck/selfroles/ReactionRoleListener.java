package xyz.diaduck.selfroles;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReactionRoleListener extends ListenerAdapter {

    // Store reaction role mappings: MessageID -> (Emoji -> RoleID)
    private final Map<String, Map<String, String>> reactionRoles = new ConcurrentHashMap<>();

    private final Gson gson = new Gson();
    private final String DATA_FILE = "reaction_roles.json";

    public ReactionRoleListener() {
        // You can load existing reaction roles from a database here
        loadReactionRoles();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("reactionrole")) {
            handleReactionRoleCommand(event);
        }
    }

    private void handleReactionRoleCommand(SlashCommandInteractionEvent event) {
        String messageId = event.getOption("message_id").getAsString();
        String emoji = event.getOption("emoji").getAsString();
        Role role = event.getOption("role").getAsRole();

        // Get the message
        event.getChannel().retrieveMessageById(messageId).queue(message -> {
            // Add the reaction to the message
            message.addReaction(Emoji.fromFormatted(emoji)).queue();

            // Store the mapping
            reactionRoles.computeIfAbsent(messageId, k -> new HashMap<>())
                    .put(emoji, role.getId());

            // Save to database/file here if needed
            saveReactionRoles();

            event.reply("Reaction role setup complete! React with " + emoji + " to get the " + role.getName() + " role.")
                    .setEphemeral(true).queue();
        }, failure -> {
            event.reply("Failed to find message with ID: " + messageId).setEphemeral(true).queue();
        });
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        // Ignore bot reactions
        String userId = event.getUserId();
        if (userId == null) return; // Safety check


        String messageId = event.getMessageId();
        String emoji = event.getReaction().getEmoji().getFormatted();

        // Check if this message has reaction roles configured
        Map<String, String> messageRoles = reactionRoles.get(messageId);
        if (messageRoles == null || !messageRoles.containsKey(emoji)) return;

        String roleId = messageRoles.get(emoji);
        Guild guild = event.getGuild();
        Role role = guild.getRoleById(roleId);
        Member member = event.getMember();

        if (role != null && member != null) {
            guild.addRoleToMember(member, role).queue(
                    success -> System.out.println("Added role " + role.getName() + " to " + member.getEffectiveName()),
                    error -> System.err.println("Failed to add role: " + error.getMessage())
            );
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        // Use getUserId() instead of getUser() to avoid null pointer
        String userId = event.getUserId();

        // Skip if no user ID (shouldn't happen but just in case)
        if (userId == null) return;

        String messageId = event.getMessageId();
        String emoji = event.getReaction().getEmoji().getFormatted();

        // Check if this message has reaction roles configured
        Map<String, String> messageRoles = reactionRoles.get(messageId);
        if (messageRoles == null || !messageRoles.containsKey(emoji)) return;

        String roleId = messageRoles.get(emoji);
        Guild guild = event.getGuild();
        Role role = guild.getRoleById(roleId);

        // Get member by user ID and check if it's a bot
        guild.retrieveMemberById(event.getUserId()).queue(member -> {
            if (role != null && member != null && !member.getUser().isBot()) {
                guild.removeRoleFromMember(member, role).queue(
                        success -> System.out.println("Removed role " + role.getName() + " from " + member.getEffectiveName()),
                        error -> System.err.println("Failed to remove role: " + error.getMessage())
                );
            }
        }, error -> {
            // Handle case where member can't be retrieved (user left server, etc.)
            System.err.println("Could not retrieve member for role removal: " + error.getMessage());
        });
    }

    // Create a reaction role setup message
    public void createReactionRoleMessage(TextChannel channel, String title, String description) {
        channel.sendMessage("**" + title + "**\n" + description).queue(message -> {
            System.out.println("Reaction role message created with ID: " + message.getId());
            // You can now use this message ID to set up reaction roles
        });
    }

    // Method to remove a reaction role mapping
    public void removeReactionRole(String messageId, String emoji) {
        Map<String, String> messageRoles = reactionRoles.get(messageId);
        if (messageRoles != null) {
            messageRoles.remove(emoji);
            if (messageRoles.isEmpty()) {
                reactionRoles.remove(messageId);
            }
            saveReactionRoles();
        }
    }

    // Load reaction roles from storage (implement based on your needs)
    private void loadReactionRoles() {
        try (FileReader reader = new FileReader(DATA_FILE)) {
            Type type = new TypeToken<Map<String, Map<String, String>>>(){}.getType();
            Map<String, Map<String, String>> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                reactionRoles.putAll(loaded);
                System.out.println("Loaded " + reactionRoles.size() + " reaction role setups");
            }
        } catch (IOException e) {
            System.out.println("No existing reaction roles file found, starting fresh");
        }
    }

    public Map<String, Map<String, String>> getReactionRoles() {
        return reactionRoles;
    }

    // Save reaction roles to storage (implement based on your needs)
    public void saveReactionRoles() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            gson.toJson(reactionRoles, writer);
            System.out.println("Saved reaction roles to file");
        } catch (IOException e) {
            System.err.println("Failed to save reaction roles: " + e.getMessage());
        }
    }

    // Utility method to get all reaction roles for a message
    public Map<String, String> getReactionRolesForMessage(String messageId) {
        return reactionRoles.getOrDefault(messageId, new HashMap<>());
    }
}
