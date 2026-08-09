package com.ryderbelserion.chatterbox.common.managers;

import com.ryderbelserion.chatterbox.common.configs.ServerConfig;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public final class ConfigManager {

    private DiscordConfig discord;
    private ServerConfig server;

    private String serverName;
    private String timezone;

    public void init() {
        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        this.serverName = configuration.node("root", "server-name").getString("global");
        this.timezone = configuration.node("root", "timezone").getString("America/New_York");

        this.discord = new DiscordConfig(this.timezone);

        this.server = new ServerConfig().init();
    }

    public void reload() {
        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        this.serverName = configuration.node("root", "server-name").getString("global");
        this.timezone = configuration.node("root", "timezone").getString("America/New_York");

        this.discord = new DiscordConfig(this.timezone);

        this.server = new ServerConfig().init();
    }

    public @NonNull DiscordConfig getDiscord() {
        return this.discord;
    }

    public @NonNull ServerConfig getServer() {
        return this.server;
    }

    public @NonNull String getServerName() {
        return this.serverName;
    }

    public @NonNull String getTimezone() {
        return this.timezone;
    }
}