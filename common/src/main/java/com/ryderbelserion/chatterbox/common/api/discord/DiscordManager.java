package com.ryderbelserion.chatterbox.common.api.discord;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.common.api.discord.objects.DiscordBot;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.discord.configs.DiscordConfig;
import com.ryderbelserion.discord.configs.features.ServerConfig;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class DiscordManager {

    private final ChatterBoxPlugin instance;
    private final FusionKyori fusion;

    public DiscordManager(@NotNull final FusionKyori fusion, @NotNull final ChatterBoxPlugin instance) {
        this.instance = instance;
        this.fusion = fusion;
    }

    private DiscordBot bot;

    public void init() {
        final ConfigManager configManager = this.instance.getConfigManager();

        final DiscordConfig config = configManager.getDiscord();

        if (!config.isEnabled()) {
            if (this.bot != null) { // stop the bot just in case
                this.bot.stop();
            }

            return;
        }

        final String token = config.getToken();

        if (token.isBlank()) {
            this.fusion.log(Level.WARNING, "Bot Token not provided! We are not starting the bot.");

            return;
        }

        if (this.bot != null) {
            this.fusion.log(Level.WARNING, "Bot is already in use! We are not starting the bot again.");

            return;
        }

        this.bot = new DiscordBot(
                this.fusion,
                this.instance,
                // This will never be configurable, gateway intents ensure all features function.
                List.of(
                        GatewayIntent.MESSAGE_CONTENT,

                        // guild intents
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_WEBHOOKS
                ),
                List.of(
                        CacheFlag.ACTIVITY
                ),
                token
        );

        this.bot.init();
    }

    public void stop() {
        final ConfigManager configManager = this.instance.getConfigManager();

        final DiscordConfig config = configManager.getDiscord();

        final ServerConfig serverConfig = config.getDefault();

        serverConfig.sendMessage(this.bot.getJDA(), config.getGuildId(), Environment.SHUTDOWN, Map.of(
                "{server}", configManager.getServerName()
        ));
    }

    @ApiStatus.Internal
    public @NotNull final DiscordBot getBot() {
        return this.bot;
    }
}