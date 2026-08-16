package com.ryderbelserion.chatterbox.common.api.discord.objects;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.discord.listeners.DiscordChatListener;
import com.ryderbelserion.chatterbox.common.configs.discord.features.PresenceConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.PerServerConfig;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.discord.DiscordPlugin;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.fusion.core.FusionCore;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import java.util.List;
import java.util.Map;

public final class DiscordBot extends DiscordPlugin {

    private final ConfigManager configManager;
    private final ChatterBoxPlugin instance;

    @NullMarked
    public DiscordBot(final FusionCore fusion,
                      final ChatterBoxPlugin instance, final List<GatewayIntent> intents,
                      final List<CacheFlag> flags,
                      final String token
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
    public void onReady(@NonNull final JDA jda) {
        final PresenceConfig config = this.configManager.getDiscord().getPresenceConfig();

        if (config.isEnabled()) {
            setPresence(); // set presence initially.

            this.instance.runTask(_ -> setPresence(), 60L, 0L); // run repeated task
        }

        this.jda.addEventListener(
                new DiscordChatListener(this.instance)
        );
    }

    @Override
    public void onGuildReady(@NonNull final Guild guild) { // no multi guild support yet.
        this.guild = guild;

        final DiscordConfig config = this.configManager.getDiscord();

        if (config.isServerAlertsEnabled()) {
            final PerServerConfig serverConfig = config.getDefault();

            serverConfig.sendMessage(Audience.empty(), this.guild, this.environment, Map.of(
                    "{server}", this.configManager.getServerName()
            ));
        }
    }

    @Override
    public void onReload(@NonNull final JDA jda) {

    }

    @Override
    public void onStop(@NonNull final JDA jda) {

    }

    private void setPresence() {
        final PresenceConfig config = this.configManager.getDiscord().getPresenceConfig();

        final int count = this.instance.getPlayerCount();

        final Activity customStatus = Activity.customStatus(this.fusion.replacePlaceholders(config.getStatus(), Map.of(
                "{count}", String.valueOf(count)
        )));

        jda.getPresence().setPresence(customStatus, false);
    }

    public @NonNull Guild getGuild() {
        return this.guild;
    }
}