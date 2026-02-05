package com.ryderbelserion.chatterbox.common.api.discord.objects;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.discord.listeners.DiscordChatListener;
import com.ryderbelserion.chatterbox.common.configs.discord.features.PresenceConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.ServerConfig;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.discord.DiscordPlugin;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    private Guild guild;

    @Override
    public void init() {
        super.init();

        this.environment = Environment.INITIALIZED;
    }

    @Override
    public void onReady(@NotNull final JDA jda) {
        final PresenceConfig config = this.configManager.getDiscord().getPresenceConfig();

        if (config.isEnabled()) {
            setPresence(); // set presence initially.

            this.instance.runTask(action -> setPresence(), 60, 0); // run repeated task
        }

        this.jda.addEventListener(
                new DiscordChatListener(this.instance)
        );
    }

    @Override
    public void onGuildReady(@NotNull final Guild guild) { // no multi guild support yet.
        this.guild = guild;

        final DiscordConfig config = this.configManager.getDiscord();

        if (config.isServerAlertsEnabled()) {
            final ServerConfig serverConfig = config.getDefault();

            serverConfig.sendMessage(null, this.guild, this.environment, Map.of(
                    "{server}", this.configManager.getServerName()
            ));
        }
    }

    @Override
    public void onReload(@NotNull final JDA jda) {

    }

    @Override
    public void onStop(@NotNull final JDA jda) {

    }

    private void setPresence() {
        final PresenceConfig config = this.configManager.getDiscord().getPresenceConfig();

        final int count = this.instance.getPlayerCount();

        final Activity customStatus = Activity.customStatus(this.fusion.replacePlaceholders(config.getStatus(), Map.of(
                "{count}", String.valueOf(count)
        )));

        jda.getPresence().setPresence(customStatus, false);
    }

    public @NotNull final Guild getGuild() {
        return this.guild;
    }
}