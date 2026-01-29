package com.ryderbelserion.discord.configs;

import com.ryderbelserion.discord.configs.features.PresenceConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class DiscordConfig {

    private final CommentedConfigurationNode configuration;

    private final String token;

    public DiscordConfig(@NotNull final CommentedConfigurationNode configuration) {
        this.configuration = configuration;

        this.token = this.configuration.node("root", "token").getString("");
        this.isEnabled = this.configuration.node("root", "enabled").getBoolean(false);
    }

    private PresenceConfig presenceConfig;
    private boolean isEnabled;

    public void init() {
        this.presenceConfig = new PresenceConfig(this.configuration.node("root", "presence"));

        this.isEnabled = this.configuration.node("root", "enabled").getBoolean(false);
    }

    public @NotNull final PresenceConfig getPresenceConfig() {
        return this.presenceConfig;
    }

    public @NotNull final String getToken() {
        return this.token;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }
}