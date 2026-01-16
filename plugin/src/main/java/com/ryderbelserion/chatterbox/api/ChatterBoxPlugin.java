package com.ryderbelserion.chatterbox.api;

import com.hypixel.hytale.server.core.universe.Universe;
import com.ryderbelserion.chatterbox.api.enums.Configs;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChatterBoxPlugin extends AbstractChatterBox {

    /**
     * Builds the plugin class
     *
     * @param dataPath the plugin folder
     * @param modPath the location of the .jar file
     */
    public ChatterBoxPlugin(final Path dataPath, final Path modPath) {
        super(dataPath, modPath);
    }

    @Override
    public void init() {
        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                java.nio.file.Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        List.of(
                "server.json",

                "messages.yml",
                "config.yml"
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

        final JsonCustomFile customFile = Configs.server.getJsonCustomFile();

        final BasicConfigurationNode configuration = Configs.server.getJsonConfig();

        try {
            configuration.node("player_count").set(getServerUsers());
        } catch (final SerializationException exception) {
            throw new RuntimeException(exception);
        }

        customFile.save();
    }

    @Override
    public void reload() {
        this.fileManager.refresh(false).addFolder(this.dataPath.resolve("locale"), FileType.YAML);
    }

    @Override
    public final int getServerUsers() {
        return this.fileManager.getFiles(Universe.get().getPath(), "json", 1).size();
    }
}