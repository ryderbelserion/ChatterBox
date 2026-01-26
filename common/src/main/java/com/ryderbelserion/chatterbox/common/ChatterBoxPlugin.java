package com.ryderbelserion.chatterbox.common;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.adapters.IPlayerAdapter;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.api.adapters.PlayerAdapter;
import com.ryderbelserion.chatterbox.common.groups.LuckPermsSupport;
import com.ryderbelserion.fusion.core.api.registry.ModRegistry;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public abstract class ChatterBoxPlugin<S, T> extends ChatterBox<S, T> {

    public static final UUID CONSOLE_UUID = new UUID(0, 0);

    public static final String CONSOLE_NAME = "Console";

    private IPlayerAdapter<?> adapter;

    public ChatterBoxPlugin(@NotNull final FusionKyori fusion) {
        super(fusion);
    }

    public abstract ISenderAdapter getSenderAdapter();

    @Override
    public void init() {
        ChatterBoxProvider.register(this);

        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                java.nio.file.Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        List.of(
                "messages.yml",

                "discord.yml",

                "config.yml",
                "chat.yml"
        ).forEach(file -> {
            final String extension = file.split("\\.")[1];

            final FileType fileType = switch (extension) {
                case "json" -> FileType.JSON;
                case "yml" -> FileType.YAML;
                default -> throw new IllegalStateException("Unexpected value: " + extension);
            };

            final Path path = dataPath.resolve(file);

            this.fileManager.addFile(path, fileType);
        });

        this.fileManager.addFolder(this.dataPath.resolve("locale"), FileType.YAML);

        final ModRegistry registry = this.fusion.getModRegistry();

        final Platform platform = getPlatform();

        List.of(
                new LuckPermsSupport(platform)
        ).forEach(mod -> registry.addMod(mod.getKey(), mod));
    }

    @Override
    public void post() {
        this.adapter = new PlayerAdapter<>(getUserRegistry(), getContextRegistry());
    }

    @Override
    public void reload() {
        this.fileManager.refresh(false).addFolder(this.dataPath.resolve("locale"), FileType.YAML);

        this.fusion.reload();

        getMessageRegistry().init();
    }

    @Override
    public @NotNull <C> IPlayerAdapter<C> getPlayerAdapter(@NotNull final Class<C> object) {
        return (IPlayerAdapter<C>) this.adapter;
    }
}