package com.ryderbelserion.chatterbox.common.configs.discord;

import com.ryderbelserion.chatterbox.common.configs.discord.features.PresenceConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.ServerConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DiscordConfig {

    private final Map<String, ServerConfig> servers = new HashMap<>();

    private final CommentedConfigurationNode configuration;

    private final boolean sendServerStatus;
    private final String timezone;
    private final String token;

    public DiscordConfig(@NotNull final String timezone, @NotNull final CommentedConfigurationNode configuration) {
        this.configuration = configuration;

        this.sendServerStatus = this.configuration.node("root", "notifications", "server-status").getBoolean(true);
        this.token = this.configuration.node("root", "token").getString("");
        this.timezone = timezone;

        init();
    }

    private PresenceConfig presenceConfig;
    private boolean isEnabled;
    private long guildId;

    public void init() {
        this.presenceConfig = new PresenceConfig(this.configuration.node("root", "presence"));

        this.isEnabled = this.configuration.node("root", "enabled").getBoolean(false);

        this.guildId = this.configuration.node("root", "guild-id").getLong(0);

        this.servers.clear();

        if (this.configuration.hasChild("notifications") && this.sendServerStatus) {
            this.servers.put("default", new ServerConfig(this.timezone, "default", this.configuration.node("notifications", "default")));

            final Map<Object, CommentedConfigurationNode> notifications = this.configuration.node("notifications", "per-server").childrenMap();

            for (final Map.Entry<Object, CommentedConfigurationNode> key : notifications.entrySet()) {
                final String section = key.getKey().toString();
                final CommentedConfigurationNode config = key.getValue();

                this.servers.put(section, new ServerConfig(this.timezone, section, config));
            }
        }
    }

    public @NotNull final Map<String, ServerConfig> getServers() {
        return Collections.unmodifiableMap(this.servers);
    }

    public @NotNull final PresenceConfig getPresenceConfig() {
        return this.presenceConfig;
    }

    public @NotNull final ServerConfig getDefault() {
        return this.servers.get("default");
    }

    public @NotNull final Optional<ServerConfig> getServer(@NotNull final String name) {
        return Optional.ofNullable(this.servers.get(name));
    }

    public @NotNull final String getToken() {
        return this.token;
    }

    public final boolean isSendServerStatus() {
        return this.sendServerStatus;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }

    public final long getGuildId() {
        return this.guildId;
    }
}