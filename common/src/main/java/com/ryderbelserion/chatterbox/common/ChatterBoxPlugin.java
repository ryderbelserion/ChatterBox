package com.ryderbelserion.chatterbox.common;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.adapters.IGroupAdapter;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.api.adapters.ServerAdapter;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.api.adapters.filter.types.RegexFilterAdapter;
import com.ryderbelserion.chatterbox.common.api.adapters.filter.types.SimpleFilterAdapter;
import com.ryderbelserion.chatterbox.common.configs.FilterConfig;
import com.ryderbelserion.chatterbox.common.configs.ServerConfig;
import com.ryderbelserion.chatterbox.common.enums.messages.Messages;
import com.ryderbelserion.chatterbox.common.groups.LuckPermsSupport;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.mods.ModRegistry;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.apache.logging.log4j.core.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public abstract class ChatterBoxPlugin<S, R> extends ChatterBox<S> {

    public static final UUID CONSOLE_UUID = new UUID(0, 0);

    public static final String CONSOLE_NAME = "Console";

    protected DiscordManager discordManager;
    protected ConfigManager configManager;

    protected ServerAdapter serverAdapter;

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
                runDelayedTask(_ -> adapter.sendMessage(sender, Messages.message_of_the_day.getKey(), placeholders), delay);

                return;
            }

            adapter.sendMessage(sender, Messages.message_of_the_day.getKey(), placeholders);
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

        final Platform platform = getPlatform();
        final String jarFolder = platform.getJarFolder();

        this.fileManager.addFolder(this.dataPath.resolve("discord"), FileType.YAML);

        this.fileManager.addFile(this.dataPath.resolve("server.json"), FileType.JSON);

        this.fileManager.addFolder(this.dataPath.resolve("locale"), jarFolder, FileType.YAML);

        final List<String> files = new ArrayList<>(List.of(
                "messages.yml",
                "config.yml"
        ));

        switch (platform) {
            case HYTALE, MINECRAFT -> files.add("chat.yml");
        }

        files.forEach(file -> this.fileManager.addFile(this.dataPath.resolve(file), jarFolder, FileType.YAML));

        final ModRegistry registry = this.fusion.getModRegistry();

        List.of(
                new LuckPermsSupport(platform)
        ).forEach(mod -> registry.addMod(mod.getKey(), mod));

        this.serverAdapter = new ServerAdapter();
    }

    @Override
    public void loadMessages() {
        this.messageRegistry.init(action -> {
            final List<Path> paths = this.fileManager.getFilesByPath(this.dataPath.resolve("locale"), ".yml", 1);

            paths.add(this.dataPath.resolve("messages.yml")); // add to list

            final Platform platform = getPlatform();

            for (final Path path : paths) {
                this.fileManager.getYamlFile(path).ifPresentOrElse(file -> {
                    final String fileName = file.getFileName();

                    final FusionKey key = FusionKey.key(ChatterBox.namespace, fileName.equalsIgnoreCase("messages.yml") ? "default" : fileName.toLowerCase());

                    final CommentedConfigurationNode configuration = file.getConfiguration();

                    for (final Messages message : Messages.values()) {
                        switch (platform) {
                            case MINECRAFT, HYTALE -> message.addMinecraftKey(action, configuration, key);
                            case VELOCITY -> message.addVelocityKey(action, configuration, key);
                            default -> message.addKey(action, configuration, key);
                        }
                    }
                }, () -> this.fusion.log(Level.INFO, "Path %s not found in cache.".formatted(path)));
            }
        });
    }

    @Override
    public void post() {
        this.configManager = new ConfigManager();
        this.configManager.init();

        loadMessages();

        final DiscordConfig discordConfig = this.configManager.getDiscord();

        if (discordConfig.isEnabled()) {
            this.discordManager = new DiscordManager(this.fusion, this);
            this.discordManager.init();
        }

        final ServerConfig serverConfig = this.configManager.getServer();
        final FilterConfig config = serverConfig.getFilterConfig();

        if (config.isEnabled()) {
            final Logger logger = (Logger) org.apache.logging.log4j.LogManager.getRootLogger();

            if (config.isUseRegex()) {
                logger.addFilter(new RegexFilterAdapter(config));
            } else {
                logger.addFilter(new SimpleFilterAdapter(config));
            }
        }

        for (final Permissions permission : Permissions.values()) {
            permission.registerPermission();
        }
    }

    @Override
    public void reload() {
        this.fileManager.refresh(false).addFolder(this.dataPath.resolve("locale"), FileType.YAML);

        this.fusion.reload();

        this.configManager.reload();

        loadMessages();

        if (this.discordManager != null) {
            this.discordManager.init();
        }
    }

    @Override
    public @NonNull final MessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }

    @Override
    public void shutdown() {
        if (this.discordManager != null) {
            this.discordManager.stop();
        }
    }

    @Override
    public @NonNull final ServerAdapter getServerAdapter() {
        return this.serverAdapter;
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

    public @NotNull final DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    public @NotNull final ConfigManager getConfigManager() {
        return this.configManager;
    }
}