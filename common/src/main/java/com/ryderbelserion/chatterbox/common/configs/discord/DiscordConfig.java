package com.ryderbelserion.chatterbox.common.configs.discord;

import com.ryderbelserion.chatterbox.common.configs.discord.features.PresenceConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.ServerConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DiscordConfig {

    private final Map<String, ServerConfig> servers = new HashMap<>();

    private final boolean isServerAlertsEnabled;
    private final boolean isPlayerAlertsEnabled;
    private final String timezone;
    private final String token;

    public DiscordConfig(@NotNull final String timezone) {
        final CommentedConfigurationNode config = FileKeys.discord.getYamlConfig();

        this.isServerAlertsEnabled = config.node("root", "alerts", "server").getBoolean(true);
        this.isPlayerAlertsEnabled = config.node("root", "alerts", "player").getBoolean(true);
        this.timezone = timezone;
        this.token = config.node("root", "token").getString("");

        init();
    }

    private PlayerAlertConfig alertConfig;
    private PresenceConfig presenceConfig;
    private boolean isEnabled;
    private long guildId;

    public void init() {
        final CommentedConfigurationNode config = FileKeys.discord.getYamlConfig();

        this.presenceConfig = new PresenceConfig(config.node("root", "presence"));

        this.isEnabled = config.node("root", "enabled").getBoolean(false);

        this.guildId = config.node("root", "guild-id").getLong(0);

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

        if (this.isPlayerAlertsEnabled && alerts.hasChild("alerts", "players")) {
            final CommentedConfigurationNode players = alerts.node("alerts", "players");

            this.alertConfig = new PlayerAlertConfig(this.timezone, players);
        }
    }

    public @NotNull final Map<String, ServerConfig> getServers() {
        return Collections.unmodifiableMap(this.servers);
    }

    public @NotNull final PresenceConfig getPresenceConfig() {
        return this.presenceConfig;
    }

    public @NotNull final PlayerAlertConfig getAlertConfig() {
        return this.alertConfig;
    }

    public @NotNull final ServerConfig getDefault() {
        return this.servers.get("default");
    }

    public @NotNull final Optional<ServerConfig> getServer(@NotNull final String name) {
        return Optional.ofNullable(this.servers.get(name));
    }

    public final boolean isPlayerAlertsEnabled() {
        return this.isPlayerAlertsEnabled;
    }

    public final boolean isServerAlertsEnabled() {
        return this.isServerAlertsEnabled;
    }

    public @NotNull final String getToken() {
        return this.token;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }

    public final long getGuildId() {
        return this.guildId;
    }
}