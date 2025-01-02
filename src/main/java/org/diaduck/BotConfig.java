package org.diaduck;

public class BotConfig {
    private static final String PREFIX = "*"; // Replace with whichever prefix you want

    public static String getToken() {
        String token = System.getenv("BOT_TOKEN");
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalStateException("Bot token not found in environment variables");
        }
        return token;
    }

    public static String getPrefix() {
        return PREFIX;
    }
}
