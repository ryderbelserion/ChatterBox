package com.ryderbelserion.chatterbox.velocity.discord.objects;

import com.ryderbelserion.discord.DiscordPlugin;
import com.ryderbelserion.discord.enums.Environment;
import com.ryderbelserion.fusion.FusionVelocity;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.util.List;

public class DiscordBot extends DiscordPlugin {

    public DiscordBot(@NotNull final FusionVelocity fusion,
                      @NotNull final List<GatewayIntent> intents,
                      @NotNull final List<CacheFlag> flags,
                      @NotNull final String token
    ) {
        super(fusion, intents, flags, token);
    }

    @Override
    public void init() {
        super.init();

        this.environment = Environment.INITIALIZED;
    }

    @Override
    public void onGuildReady(@NotNull final Guild guild) {

    }

    @Override
    public void onReload(@NotNull final JDA jda) {

    }

    @Override
    public void onStop(@NotNull final JDA jda) {

    }
}