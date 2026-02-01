package com.ryderbelserion.chatterbox.common;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.adapters.IPlayerAdapter;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.api.adapters.PlayerAdapter;
import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.groups.LuckPermsSupport;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.fusion.core.api.registry.ModRegistry;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class ChatterBoxPlugin<S, T> extends ChatterBox<S, T> {

    public static final UUID CONSOLE_UUID = new UUID(0, 0);

    public static final String CONSOLE_NAME = "Console";

    private ConfigManager configManager;
    private DiscordManager discordManager;
    private IPlayerAdapter<?> adapter;

    public ChatterBoxPlugin(@NotNull final FusionKyori fusion) {
        super(fusion);
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
                "/discord.yml", "discord;config.yml"
        ).forEach((input, output) -> {
            final String[] splitter = output.split(";");
            final String folder = splitter[0];
            final String name = splitter[1];

            final Path path = this.dataPath.resolve(folder).resolve(name);

            this.fileManager.extractFile(input, path);

            this.fileManager.addFile(path, FileType.YAML, action -> action.addAction(FileAction.ALREADY_EXTRACTED));
        });

        final Platform platform = getPlatform();

        switch (platform) {
            case VELOCITY -> {
                List.of(
                        "config.yml"
                ).forEach(file -> {
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
                List.of(
                        "messages.yml",
                        "config.yml",
                        "chat.yml"
                ).forEach(file -> {
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

        final DiscordConfig config = this.configManager.getDiscord();

        if (config.isEnabled()) {
            this.discordManager = new DiscordManager(this.fusion, this);
            this.discordManager.init();
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

    public @NotNull final ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NotNull final DiscordManager getDiscordManager() {
        return this.discordManager;
    }
}