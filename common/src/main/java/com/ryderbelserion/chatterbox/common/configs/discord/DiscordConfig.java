package com.ryderbelserion.chatterbox.common.configs.discord;

import com.ryderbelserion.chatterbox.common.configs.discord.features.PresenceConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.ServerConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class DiscordConfig {

    private final Map<String, ServerConfig> servers = new HashMap<>();

    private final boolean isServerAlertsEnabled;
    private final boolean isPlayerAlertsEnabled;
    private final String timezone;
    private final String token;

    private final CommentedConfigurationNode config;

    public DiscordConfig(@NonNull final String timezone) {
        final CommentedConfigurationNode config = FileKeys.discord.getYamlConfig();

        this.isServerAlertsEnabled = config.node("root", "alerts", "server").getBoolean(true);
        this.isPlayerAlertsEnabled = config.node("root", "alerts", "player").getBoolean(true);
        this.timezone = timezone;
        this.token = config.node("root", "token").getString("");

        this.config = config;

        init();
    }

    private PlayerAlertConfig alertConfig;
    private PresenceConfig presenceConfig;
    private boolean isEnabled;
    private long guildId;

    public void init() {
        this.presenceConfig = new PresenceConfig(this.config.node("root", "presence"));

        this.isEnabled = this.config.node("root", "enabled").getBoolean(false);

        this.guildId = this.config.node("root", "guild-id").getLong(0);

        this.servers.clear();

        final CommentedConfigurationNode alerts = FileKeys.alerts.getYamlConfig();

        if (this.isServerAlertsEnabled && alerts.hasChild("alerts", "server")) {
            final CommentedConfigurationNode server = alerts.node("alerts", "server");

            this.servers.put("default", new ServerConfig(this.timezone, "default", server.node("default")));

            /*final Map<Object, CommentedConfigurationNode> notifications = status.node("per-server").childrenMap();

            for (final Map.Entry<Object, CommentedConfigurationNode> key : notifications.entrySet()) {
                final String section = key.getKey().toString();
                final CommentedConfigurationNode config = key.getValue();

                this.servers.put(section, new ServerConfig(this.timezone, section, config));
            }*/
        }

        if (this.isPlayerAlertsEnabled && alerts.hasChild("alerts", "player")) {
            final CommentedConfigurationNode players = alerts.node("alerts", "player");

            this.alertConfig = new PlayerAlertConfig(this.timezone, players);
        }
    }

    public @NonNull Map<String, ServerConfig> getServers() {
        return Collections.unmodifiableMap(this.servers);
    }

    public @NonNull PresenceConfig getPresenceConfig() {
        return this.presenceConfig;
    }

    public @NonNull PlayerAlertConfig getAlertConfig() {
        return this.alertConfig;
    }

    public @NonNull ServerConfig getDefault() {
        return this.servers.get("default");
    }

    public @NonNull Optional<ServerConfig> getServer(@NonNull final String name) {
        return Optional.ofNullable(this.servers.get(name));
    }

    public boolean isPlayerAlertsEnabled() {
        return this.isPlayerAlertsEnabled;
    }

    public boolean isServerAlertsEnabled() {
        return this.isServerAlertsEnabled;
    }

    public @NonNull String getToken() {
        return this.token;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public long getGuildId() {
        return this.guildId;
    }
}