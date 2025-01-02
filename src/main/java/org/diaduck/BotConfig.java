package org.diaduck;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotConfig {
    private static final String CONFIG_FILE = "config.properties"; // Ensure this file exists in your working directory
    private static final Properties properties = new Properties();
    private static final String PREFIX = "*"; // Replace with whichever prefix you want

    static {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties file", e);
        }
    }

    public static String getToken() {
        String token = properties.getProperty("bot.token");
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalStateException("Bot token not found in config.properties");
        }
        return token;
    }

    public static String getPrefix() {
        return PREFIX;
    }
}
