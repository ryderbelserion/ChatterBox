package com.ryderbelserion.chatterbox.velocity.api.discord.objects;

import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.chatterbox.velocity.api.ChatterBoxPlatform;
import com.ryderbelserion.discord.DiscordPlugin;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.discord.configs.features.PresenceConfig;
import com.ryderbelserion.fusion.FusionVelocity;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class DiscordBot extends DiscordPlugin {

    private final ChatterBox instance;
    private final ConfigManager configManager;

    public DiscordBot(@NotNull final FusionVelocity fusion,
                      @NotNull final ChatterBox instance, @NotNull final List<GatewayIntent> intents,
                      @NotNull final List<CacheFlag> flags,
                      @NotNull final String token
    ) {
        super(fusion, intents, flags, token);

        this.instance = instance;

        final ChatterBoxPlatform platform = this.instance.getPlatform();

        this.configManager = platform.getConfigManager();
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
            final int count = this.instance.getServer().getPlayerCount();

            final Activity customStatus = Activity.customStatus(this.fusion.replacePlaceholders(config.getStatus(), Map.of(
                    "{count}", String.valueOf(count)
            )));

            jda.getPresence().setPresence(customStatus, false);
        }
    }

    @Override
    public void onGuildReady(@NotNull final Guild guild) {
        final TextChannel channel = guild.getTextChannelById("1466260557513490658");

        if (channel == null) return;

        channel.sendMessage("CoreCraft Proxy is now online.").queue();
    } // no multi guild support yet.

    @Override
    public void onReload(@NotNull final JDA jda) {

    }

    @Override
    public void onStop(@NotNull final JDA jda) {

    }
}