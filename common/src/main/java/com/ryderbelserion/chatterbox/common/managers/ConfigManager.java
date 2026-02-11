package com.ryderbelserion.chatterbox.common.managers;

import com.ryderbelserion.chatterbox.common.configs.ServerConfig;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class ConfigManager {

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

    public @NotNull final DiscordConfig getDiscord() {
        return this.discord;
    }

    public @NotNull final ServerConfig getServer() {
        return this.server;
    }

    public @NotNull final String getServerName() {
        return this.serverName;
    }

    public @NotNull final String getTimezone() {
        return this.timezone;
    }
}