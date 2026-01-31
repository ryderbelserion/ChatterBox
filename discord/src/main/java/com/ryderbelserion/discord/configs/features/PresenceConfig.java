package com.ryderbelserion.discord.configs.features;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class PresenceConfig {

    private boolean isEnabled;
    private String status;

    public PresenceConfig(@NotNull final CommentedConfigurationNode configuration) {
        init(configuration);
    }

    public void init(@NotNull final CommentedConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.status = configuration.node("status").getString("");
    }

    public @NotNull final String getStatus() {
        return this.status;
    }

    public final boolean isEnabled() {
        return this.isEnabled && !this.status.isBlank();
    }
}