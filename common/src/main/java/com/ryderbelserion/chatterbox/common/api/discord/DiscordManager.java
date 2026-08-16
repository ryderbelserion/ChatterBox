package com.ryderbelserion.chatterbox.common.api.discord;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.configs.discord.features.PerServerConfig;
import com.ryderbelserion.chatterbox.api.enums.FileKeys;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.common.api.discord.objects.DiscordBot;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.core.FusionCore;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import java.util.List;
import java.util.Map;

public final class DiscordManager {

    private final ChatterBoxPlugin instance;
    private final FusionCore fusion;

    @NullMarked
    public DiscordManager(final FusionCore fusion, final ChatterBoxPlugin instance) {
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

        final String token = FileKeys.discord.getYamlConfig().node("root", "token").getString("");

        if (token.isBlank()) {
            this.fusion.log(Level.warn, "Bot Token not provided! We are not starting the bot.");

            return;
        }

        if (this.bot != null) {
            this.fusion.log(Level.warn, "Bot is already in use! We are not starting the bot again.");

            return;
        }

        this.bot = new DiscordBot(
                this.fusion,
                this.instance,
                // This will never be configurable, gateway intents ensure all features function.
                List.of(
                        // message intents
                        GatewayIntent.MESSAGE_CONTENT,

                        // guild intents
                        GatewayIntent.GUILD_EXPRESSIONS,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_WEBHOOKS
                ),
                List.of(
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOJI
                ),
                token
        );

        this.bot.init();
    }

    public void stop() {
        final ConfigManager configManager = this.instance.getConfigManager();

        final DiscordConfig config = configManager.getDiscord();

        if (config.isServerAlertsEnabled()) {
            final PerServerConfig serverConfig = config.getDefault();

            serverConfig.sendMessage(Audience.empty(), getGuild(), Environment.SHUTDOWN, Map.of(
                    "{server}", configManager.getServerName()
            ));
        }
    }

    @ApiStatus.Internal
    public @NonNull DiscordBot getBot() {
        return this.bot;
    }

    public @NonNull Guild getGuild() {
        return this.bot.getGuild();
    }
}