package com.ryderbelserion.chatterbox.common.configs.discord.features;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;

@NullMarked
public final class PresenceConfig {

    private boolean isEnabled = false;
    private String status = "";

    public PresenceConfig(final CommentedConfigurationNode configuration) {
        init(configuration);
    }

    public void init(final CommentedConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.status = configuration.node("status").getString("");
    }

    public String getStatus() {
        return this.status;
    }

    public boolean isEnabled() {
        return this.isEnabled && !this.status.isBlank();
    }
}