package com.ryderbelserion.discord;

import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.discord.api.listeners.StatusListener;
import com.ryderbelserion.fusion.core.FusionCore;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class DiscordPlugin {

    protected final FusionCore fusion;
    protected final JDA jda;

    public DiscordPlugin(
            @NotNull final FusionCore fusion,

            // discord only
            @NotNull final List<GatewayIntent> intents,
            @NotNull final List<CacheFlag> flags,
            @NotNull final String token
    ) {
        this.fusion = fusion;

        this.jda = JDABuilder.createDefault(token, intents)
                .enableCache(flags)
                .addEventListeners(new StatusListener(this))
                .build();
    }

    protected Environment environment = Environment.CONSTRUCTION;
    protected CommandHandler commandHandler;

    public abstract void onGuildReady(@NotNull final Guild guild);

    public void onReady(@NotNull final JDA jda) {
        this.commandHandler = new CommandHandler(jda);
    }

    public abstract void onReload(@NotNull final JDA jda);

    public abstract void onStop(@NotNull final JDA jda);

    public void addEventListener(@NotNull final Object... listeners) {
        this.jda.addEventListener(listeners);
    }

    public @NotNull final Environment getEnvironment() {
        return this.environment;
    }

    public @NotNull final Path getAddonDirectory() {
        return getDirectory().resolve("addons");
    }

    public @NotNull final Path getCacheDirectory() {
        return getDirectory().resolve("cache");
    }

    public @NotNull final Path getDirectory() {
        return this.fusion.getDataPath().resolve("discord");
    }

    public void init() {
        final Path directory = getDirectory();

        try {
            if (!Files.exists(directory)) {
                Files.createDirectory(directory);
            }

            final Path addonDirectory = getAddonDirectory();

            if (!Files.exists(addonDirectory)) {
                Files.createDirectory(addonDirectory);
            }

            final Path cacheDirectory = getCacheDirectory();

            if (!Files.exists(cacheDirectory)) {
                Files.createDirectory(cacheDirectory);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public final void stop() {
        if (this.jda != null) {
            this.jda.shutdown();
        }
    }

    public @NotNull final JDA getJDA() {
        return this.jda;
    }
}