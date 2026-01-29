package com.ryderbelserion.chatterbox.common.managers;

import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.discord.configs.DiscordConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class ConfigManager {

    private DiscordConfig discord;

    public void init() {
        final CommentedConfigurationNode configuration = FileKeys.discord.getYamlConfig();

        this.discord = new DiscordConfig(configuration);
    }

    public void reload() {
        final CommentedConfigurationNode configuration = FileKeys.discord.getYamlConfig();

        this.discord = new DiscordConfig(configuration);
    }

    public @NotNull final DiscordConfig getDiscord() {
        return this.discord;
    }
}