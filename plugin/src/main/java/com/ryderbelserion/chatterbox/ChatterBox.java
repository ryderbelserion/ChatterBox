package com.ryderbelserion.chatterbox;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.ryderbelserion.chatterbox.api.enums.Files;
import com.ryderbelserion.chatterbox.listeners.PostConnectListener;
import com.ryderbelserion.chatterbox.users.UserManager;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ChatterBox extends JavaPlugin {

    private static ChatterBox instance;

    public ChatterBox(@NonNullDecl final JavaPluginInit init) {
        super(init);

        instance = this;
    }

    private FileManager fileManager;
    private UserManager userManager;
    private Path directory;

    @Override
    protected void setup() {
        super.setup();

        final Path modDirectory = getFile();
        final Path dataDirectory = getDataDirectory();

        this.directory = dataDirectory.getParent().resolve(dataDirectory.getFileName().toString().split("_")[0]);

        if (java.nio.file.Files.notExists(this.directory)) {
            try {
                java.nio.file.Files.createDirectory(this.directory);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.fileManager = new FileManager(modDirectory, this.directory);

        List.of(
                "server.json"
        ).forEach(file -> {
            final String extension = file.split("\\.")[1];

            final FileType fileType = switch (extension) {
                case "json" -> FileType.JSON;
                case "yml" -> FileType.YAML;
                default -> throw new IllegalStateException("Unexpected value: " + extension);
            };

            final Path path = this.directory.resolve(file);

            this.fileManager.addFile(path, fileType);
        });

        final BasicConfigurationNode configuration = Files.server.getJsonConfig();

        final Path folder = Universe.get().getPath();

        final int count = this.fileManager.getFiles(folder, ".json", 1).size();

        try {
            configuration.node("player_count").set(count);
        } catch (final SerializationException exception) {
            throw new RuntimeException(exception);
        }

        Files.server.getJsonCustomFile().save();

        this.userManager = new UserManager(this.directory, getLogger());
        this.userManager.init();

        final EventRegistry registry = getEventRegistry();

        List.of(
                new PostConnectListener()
        ).forEach(listener -> listener.init(registry));
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final UserManager getUserManager() {
        return this.userManager;
    }

    @ApiStatus.Internal
    public static ChatterBox getInstance() {
        return instance;
    }

    public @NotNull final Path getDataPath() {
        return this.directory;
    }
}