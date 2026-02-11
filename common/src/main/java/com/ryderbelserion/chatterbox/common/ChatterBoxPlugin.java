package com.ryderbelserion.chatterbox.common;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.adapters.IGroupAdapter;
import com.ryderbelserion.chatterbox.api.adapters.IPlayerAdapter;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.api.adapters.PlayerAdapter;
import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.api.objects.filter.types.SimpleFilterAdapter;
import com.ryderbelserion.chatterbox.common.configs.FilterConfig;
import com.ryderbelserion.chatterbox.common.configs.ServerConfig;
import com.ryderbelserion.chatterbox.common.groups.LuckPermsSupport;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.fusion.core.api.registry.ModRegistry;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public abstract class ChatterBoxPlugin<S, T, R> extends ChatterBox<S, T> {

    public static final UUID CONSOLE_UUID = new UUID(0, 0);

    public static final String CONSOLE_NAME = "Console";

    private ConfigManager configManager;
    private DiscordManager discordManager;
    private IPlayerAdapter<?> adapter;

    public ChatterBoxPlugin(@NotNull final FusionKyori fusion) {
        super(fusion);
    }

    public void broadcast(@NotNull final S sender, @NotNull final String message, @NotNull final Map<String, String> placeholders) {

    }

    public void broadcast(@NotNull final String message, @NotNull final Map<String, String> placeholders) {

    }

    public void runTask(@NotNull final Consumer<R> consumer, final long seconds, final long delay) {

    }

    public void runTask(@NotNull final Consumer<R> consumer, final long seconds) {
        runTask(consumer, seconds, 0);
    }

    public void runTask(@NotNull final Consumer<R> consumer) {
        runTask(consumer, 0, 0);
    }

    public void runDelayedTask(@NotNull final Consumer<R> consumer, final long delay) {
        runTask(consumer, 0, delay);
    }

    public void sendTitle(
            @NotNull final S sender, final boolean notifyServer,
            @NotNull final String title, @NotNull final String subtitle, final int duration, final int fadeIn, final int fadeOut,
            @NotNull final Map<String, String> placeholders
    ) {

    }

    public void sendMessageOfTheDay(@NotNull final CommentedConfigurationNode config, @NotNull final S sender, @NotNull final Map<String, String> placeholders) {
        if (config.node("root", "motd", "toggle").getBoolean(false)) {
            final int delay = config.node("root", "motd", "delay").getInt(0);

            final ISenderAdapter adapter = getSenderAdapter();

            if (delay > 0) {
                runDelayedTask(consumer -> adapter.sendMessage(sender, Messages.message_of_the_day, placeholders), delay);

                return;
            }

            adapter.sendMessage(sender, Messages.message_of_the_day, placeholders);
        }
    }

    public abstract ISenderAdapter getSenderAdapter();

    public abstract int getPlayerCount();

    @Override
    public void init() {
        ChatterBoxProvider.register(this);

        try {
            Files.createDirectories(this.dataPath);
        } catch (final IOException ignored) {}

        Map.of( // map internal files to external output
                "/discord.yml", "discord;config.yml",
                "/alerts.yml", "discord;alerts.yml"
        ).forEach((input, output) -> {
            final String[] splitter = output.split(";");
            final String folder = splitter[0];
            final String name = splitter[1];

            final Path path = this.dataPath.resolve(folder).resolve(name);

            this.fileManager.extractFile(input, path);

            this.fileManager.addFile(path, FileType.YAML, action -> action.addAction(FileAction.ALREADY_EXTRACTED));
        });

        final Platform platform = getPlatform();

        final List<String> files = new ArrayList<>(List.of(
                "config.yml",
                "server.json"
        ));

        switch (platform) {
            case VELOCITY -> {
                files.forEach(file -> {
                    final String extension = file.split("\\.")[1];

                    final FileType fileType = switch (extension) {
                        case "json" -> FileType.JSON;
                        case "yml" -> FileType.YAML;
                        default -> throw new IllegalStateException("Unexpected value: " + extension);
                    };

                    this.fileManager.addFile(this.dataPath.resolve(file), fileType);
                });
            }

            case HYTALE, MINECRAFT -> {
                files.addAll(List.of(
                        "messages.yml",
                        "chat.yml"
                ));

                files.forEach(file -> {
                    final String extension = file.split("\\.")[1];

                    final FileType fileType = switch (extension) {
                        case "json" -> FileType.JSON;
                        case "yml" -> FileType.YAML;
                        default -> throw new IllegalStateException("Unexpected value: " + extension);
                    };

                    this.fileManager.addFile(this.dataPath.resolve(file), fileType);
                });

                this.fileManager.addFolder(this.dataPath.resolve("locale"), FileType.YAML);
            }
        }

        final ModRegistry registry = this.fusion.getModRegistry();

        List.of(
                new LuckPermsSupport(platform)
        ).forEach(mod -> registry.addMod(mod.getKey(), mod));
    }

    @Override
    public void post() {
        this.adapter = new PlayerAdapter<>(getUserRegistry(), getContextRegistry());

        this.configManager = new ConfigManager();
        this.configManager.init();

        final DiscordConfig discordConfig = this.configManager.getDiscord();

        if (discordConfig.isEnabled()) {
            this.discordManager = new DiscordManager(this.fusion, this);
            this.discordManager.init();
        }

        final ServerConfig serverConfig = this.configManager.getServer();

        final FilterConfig filterConfig = serverConfig.getFilterConfig();

        if (filterConfig.isEnabled()) {
            final Logger logger = (Logger) LogManager.getRootLogger();

            logger.addFilter(new SimpleFilterAdapter(filterConfig));
        }
    }

    @Override
    public void reload() {
        this.fileManager.refresh(false).addFolder(this.dataPath.resolve("locale"), FileType.YAML);

        this.fusion.reload();

        getMessageRegistry().init();

        this.configManager.reload();

        if (this.discordManager != null) {
            this.discordManager.init();
        }
    }

    @Override
    public void shutdown() {
        if (this.discordManager != null) {
            this.discordManager.stop();
        }
    }

    @Override
    public @NotNull <C> IPlayerAdapter<C> getPlayerAdapter(@NotNull final Class<C> object) {
        return (IPlayerAdapter<C>) this.adapter;
    }

    public @NotNull final Map<String, String> getPlaceholders(@Nullable final IUser user, @NotNull final String playerName) {
        final Map<String, String> placeholders = new HashMap<>();

        placeholders.putIfAbsent("{player}", playerName);

        if (user != null) {
            final IGroupAdapter adapter = user.getGroupAdapter();

            final Map<String, String> map = adapter.getPlaceholders();

            if (!map.isEmpty()) {
                placeholders.putAll(map);
            }
        }

        return placeholders;
    }

    public @NotNull final ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NotNull final DiscordManager getDiscordManager() {
        return this.discordManager;
    }
}