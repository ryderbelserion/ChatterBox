package com.rydderbelserion.chatterbox;

import com.ryderbelserion.chatterbox.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.AbstractChatterBox;
import com.ryderbelserion.fusion.files.enums.FileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class ChatterBoxPlugin<S, T> extends AbstractChatterBox<S, T> {

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
                "server.json",

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

            final Path path = dataPath.resolve(file);

            this.fileManager.addFile(path, fileType);
        });

        this.fileManager.addFolder(this.dataPath.resolve("locale"), FileType.YAML);
    }

    @Override
    public void reload() {
        this.fileManager.refresh(false).addFolder(this.dataPath.resolve("locale"), FileType.YAML);
    }
}