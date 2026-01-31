package com.ryderbelserion.discord.configs;

import com.ryderbelserion.discord.configs.features.PresenceConfig;
import com.ryderbelserion.discord.configs.features.ServerConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class DiscordConfig {

    private final Map<String, ServerConfig> servers = new HashMap<>();

    private final CommentedConfigurationNode configuration;

    private final String token;

    public DiscordConfig(@NotNull final CommentedConfigurationNode configuration) {
        this.configuration = configuration;

        this.token = this.configuration.node("root", "token").getString("");

        init();
    }

    private PresenceConfig presenceConfig;
    private boolean isEnabled;

    public void init() {
        this.presenceConfig = new PresenceConfig(this.configuration.node("root", "presence"));

        this.isEnabled = this.configuration.node("root", "enabled").getBoolean(false);

        if (this.configuration.hasChild("notifications")) {
            final Map<Object, CommentedConfigurationNode> notifications = this.configuration.node("notifications").childrenMap();

            for (final Map.Entry<Object, CommentedConfigurationNode> key : notifications.entrySet()) {
                final String section = key.getKey().toString();
                final CommentedConfigurationNode config = key.getValue();

                this.servers.put(section, new ServerConfig(section, config));
            }
        }
    }

    public @NotNull final PresenceConfig getPresenceConfig() {
        return this.presenceConfig;
    }

    public @NotNull final String getToken() {
        return this.token;
    }

    public @NotNull final Map<String, ServerConfig> getServers() {
        return this.servers;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }
}