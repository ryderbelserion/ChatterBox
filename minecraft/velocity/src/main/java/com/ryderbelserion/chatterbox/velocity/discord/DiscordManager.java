package com.ryderbelserion.chatterbox.velocity.discord;

import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.velocity.discord.objects.DiscordBot;
import com.ryderbelserion.fusion.FusionVelocity;
import com.ryderbelserion.fusion.core.api.enums.Level;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;

public class DiscordManager {

    private final FusionVelocity fusion;

    public DiscordManager(@NotNull final FusionVelocity fusion) {
        this.fusion = fusion;
    }

    private DiscordBot bot;

    public void init() {
        final CommentedConfigurationNode configuration = FileKeys.discord.getYamlConfig();

        if (!configuration.node("root", "enable").getBoolean(false)) {
            return;
        }

        final String token = configuration.node("root", "token").getString("");

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
    }

    @ApiStatus.Internal
    public @NotNull final DiscordBot getBot() {
        return this.bot;
    }
}