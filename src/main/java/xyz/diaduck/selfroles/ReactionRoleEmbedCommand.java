package xyz.diaduck.selfroles;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactionRoleEmbedCommand {

    private final ReactionRoleListener reactionRoleListener;

    public ReactionRoleEmbedCommand(ReactionRoleListener reactionRoleListener) {
        this.reactionRoleListener = reactionRoleListener;
    }

    /**
     * Creates and sends a reaction role embed with automatic reactions
     * @param channel The channel to send the embed to
     * @param title The title of the embed
     * @param description Optional description for the embed
     * @param color The color of the embed (can be null for default)
     * @param reactionRoles Map of emoji -> role pairs to set up
     */
    public void createReactionRoleEmbed(TextChannel channel, String title, String description,
                                        Color color, Map<String, Role> reactionRoles) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);

        if (description != null && !description.isEmpty()) {
            embed.setDescription(description);
        }

        if (color != null) {
            embed.setColor(color);
        } else {
            embed.setColor(Color.BLUE); // Default color
        }

        // Build the field showing emoji -> role mappings
        StringBuilder fieldValue = new StringBuilder();
        for (Map.Entry<String, Role> entry : reactionRoles.entrySet()) {
            String emoji = entry.getKey();
            Role role = entry.getValue();
            fieldValue.append(emoji).append(" → ").append(role.getAsMention()).append("\n");
        }

        embed.addField("Available Roles", fieldValue.toString(), false);
        embed.setFooter("React to get/remove roles!");

        // Send the embed
        channel.sendMessageEmbeds(embed.build()).queue(message -> {
            // Add all reactions automatically
            addReactionsSequentially(message, reactionRoles, 0);

            // Store the reaction role mappings
            storeReactionRoleMappings(message.getId(), reactionRoles);

            System.out.println("Created reaction role embed with ID: " + message.getId());
        });
    }

    /**
     * Creates a simple reaction role embed with just emoji and role names
     */
    public void createSimpleReactionRoleEmbed(TextChannel channel, String title,
                                              Map<String, Role> reactionRoles) {
        createReactionRoleEmbed(channel, title, "React with the emoji below to get the corresponding role!",
                Color.GREEN, reactionRoles);
    }

    /**
     * Updates an existing message to show current reaction roles
     * @param message The message to update
     * @param title New title for the embed
     */
    public void updateReactionRoleEmbed(Message message, String title) {
        Map<String, String> roleIds = reactionRoleListener.getReactionRolesForMessage(message.getId());
        Guild guild = message.getGuild();

        if (roleIds.isEmpty()) {
            message.editMessageEmbeds(createNoRolesEmbed(title)).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setColor(Color.BLUE);

        StringBuilder fieldValue = new StringBuilder();
        for (Map.Entry<String, String> entry : roleIds.entrySet()) {
            String emoji = entry.getKey();
            String roleId = entry.getValue();
            Role role = guild.getRoleById(roleId);

            if (role != null) {
                fieldValue.append(emoji).append(" → ").append(role.getAsMention()).append("\n");
            }
        }

        embed.addField("Available Roles", fieldValue.toString(), false);
        embed.setFooter("React to get/remove roles!");

        message.editMessageEmbeds(embed.build()).queue();
    }

    /**
     * Creates a fancy embed with role counts
     * @param channel Channel to send to
     * @param title Embed title
     * @param reactionRoles Map of reactions to roles
     * @param showMemberCount Whether to show how many members have each role
     */
    public void createFancyReactionRoleEmbed(TextChannel channel, String title,
                                             Map<String, Role> reactionRoles, boolean showMemberCount) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🎭 " + title);
        embed.setDescription("Click the reactions below to assign yourself roles!");
        embed.setColor(Color.MAGENTA);

        StringBuilder fieldValue = new StringBuilder();
        for (Map.Entry<String, Role> entry : reactionRoles.entrySet()) {
            String emoji = entry.getKey();
            Role role = entry.getValue();

            fieldValue.append(emoji).append(" **").append(role.getName()).append("**");

            if (showMemberCount && role.getGuild() != null) {
                int memberCount = role.getGuild().getMembersWithRoles(role).size();
                fieldValue.append(" *(").append(memberCount).append(" members)*");
            }

            fieldValue.append("\n");
        }

        embed.addField("📋 Available Roles", fieldValue.toString(), false);
        embed.addField("ℹ️ How it works",
                "• React to **get** a role\n• Remove your reaction to **remove** the role\n• You can have multiple roles!",
                false);
        embed.setFooter("Reaction Roles • " + reactionRoles.size() + " roles available");
        embed.setTimestamp(java.time.Instant.now());

        channel.sendMessageEmbeds(embed.build()).queue(message -> {
            addReactionsSequentially(message, reactionRoles, 0);
            storeReactionRoleMappings(message.getId(), reactionRoles);
            System.out.println("Created fancy reaction role embed with ID: " + message.getId());
        });
    }

    /**
     * Adds reactions to a message one by one to avoid rate limits
     */
    private void addReactionsSequentially(Message message, Map<String, Role> reactionRoles, int index) {
        String[] emojis = reactionRoles.keySet().toArray(new String[0]);

        if (index >= emojis.length) {
            System.out.println("Finished adding all reactions!");
            return;
        }

        String emoji = emojis[index];
        message.addReaction(Emoji.fromFormatted(emoji)).queue(
                success -> {
                    System.out.println("Added reaction: " + emoji);
                    // Add next reaction after a small delay
                    addReactionsSequentially(message, reactionRoles, index + 1);
                },
                error -> {
                    System.err.println("Failed to add reaction " + emoji + ": " + error.getMessage());
                    // Try next reaction even if this one failed
                    addReactionsSequentially(message, reactionRoles, index + 1);
                }
        );
    }

    /**
     * Stores the reaction role mappings in the listener
     */
    private void storeReactionRoleMappings(String messageId, Map<String, Role> reactionRoles) {
        for (Map.Entry<String, Role> entry : reactionRoles.entrySet()) {
            String emoji = entry.getKey();
            String roleId = entry.getValue().getId();

            // Store in the reaction role listener
            reactionRoleListener.getReactionRoles()
                    .computeIfAbsent(messageId, k -> new java.util.HashMap<>())
                    .put(emoji, roleId);
        }

        // Save to file
        reactionRoleListener.saveReactionRoles();
    }

    /**
     * Creates an embed for when no roles are configured
     */
    private MessageEmbed createNoRolesEmbed(String title) {
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription("No reaction roles are currently configured for this message.")
                .setColor(Color.ORANGE)
                .build();
    }

    /**
     * Gets role statistics for an embed
     */
    public String getRoleStats(Guild guild, Map<String, Role> reactionRoles) {
        StringBuilder stats = new StringBuilder();
        AtomicInteger totalMembers = new AtomicInteger(0);

        reactionRoles.forEach((emoji, role) -> {
            int count = guild.getMembersWithRoles(role).size();
            totalMembers.addAndGet(count);
            stats.append(String.format("%s %s: %d members\n", emoji, role.getName(), count));
        });

        stats.append(String.format("\n**Total role assignments:** %d", totalMembers.get()));
        return stats.toString();
    }
}