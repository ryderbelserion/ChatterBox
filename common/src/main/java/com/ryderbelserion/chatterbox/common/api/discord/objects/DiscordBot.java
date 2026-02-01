package com.ryderbelserion.chatterbox.common.api.discord.objects;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.configs.discord.features.PresenceConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.ServerConfig;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.discord.DiscordPlugin;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class DiscordBot extends DiscordPlugin {

    private final ConfigManager configManager;
    private final ChatterBoxPlugin instance;

    public DiscordBot(@NotNull final FusionKyori fusion,
                      @NotNull final ChatterBoxPlugin instance, @NotNull final List<GatewayIntent> intents,
                      @NotNull final List<CacheFlag> flags,
                      @NotNull final String token
    ) {
        super(fusion, intents, flags, token);

        this.instance = instance;

        this.configManager = this.instance.getConfigManager();
    }

    @Override
    public void init() {
        super.init();

        this.environment = Environment.INITIALIZED;
    }

    @Override
    public void onReady(@NotNull final JDA jda) {
        final PresenceConfig config = this.configManager.getDiscord().getPresenceConfig();

        if (config.isEnabled()) {
            final int count = this.instance.getPlayerCount();

            final Activity customStatus = Activity.customStatus(this.fusion.replacePlaceholders(config.getStatus(), Map.of(
                    "{count}", String.valueOf(count)
            )));

            jda.getPresence().setPresence(customStatus, false);
        }
    }

    @Override
    public void onGuildReady(@NotNull final Guild guild) { // no multi guild support yet.
        final DiscordConfig config = this.configManager.getDiscord();

        final ServerConfig serverConfig = config.getDefault();

        serverConfig.sendMessage(getJDA(), config.getGuildId(), this.environment, Map.of(
                "{server}", this.configManager.getServerName()
        ));
    }

    @Override
    public void onReload(@NotNull final JDA jda) {

    }

    @Override
    public void onStop(@NotNull final JDA jda) {

    }
}