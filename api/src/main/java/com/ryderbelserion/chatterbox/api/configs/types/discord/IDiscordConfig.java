package com.ryderbelserion.chatterbox.api.configs.types.discord;
import com.ryderbelserion.chatterbox.api.configs.types.discord.features.IPresenceConfig;
import com.ryderbelserion.chatterbox.api.configs.types.discord.features.IPerServerConfig;
import com.ryderbelserion.chatterbox.api.configs.types.discord.features.alerts.IPlayerAlertConfig;
import org.jspecify.annotations.NullMarked;
import java.util.Map;
import java.util.Optional;

@NullMarked
public interface IDiscordConfig<SC extends IPerServerConfig> {

    IPresenceConfig getPresenceConfig();

    IPlayerAlertConfig getAlertConfig();

    boolean isPlayerAlertsEnabled();

    boolean isServerAlertsEnabled();

    boolean isEnabled();

    long getGuildId();

    Map<String, SC> getServers();

    Optional<SC> getServer(final String name);

    SC getDefault();

    void init();

}