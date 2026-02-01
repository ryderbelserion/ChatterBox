package com.ryderbelserion.chatterbox.common.enums;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Optional;

public enum FileKeys {

    discord("config.yml", "discord"),

    config("config.yml"),
    chat("chat.yml");

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FileManager fileManager = this.plugin.getFileManager();
    private final Path path = this.plugin.getDataPath();

    private final Path location; // the file location
    private final Path folder;

    FileKeys(@NotNull final String fileName, @NotNull final String folder) {
        this.folder = this.path.resolve(folder);
        this.location = this.folder.resolve(fileName);
    }

    FileKeys(@NotNull final String name) {
        this.location = this.path.resolve(name);
        this.folder = this.path;
    }

    public @NotNull final BasicConfigurationNode getJsonConfig() {
        return getJsonCustomFile().getConfiguration();
    }

    public JsonCustomFile getJsonCustomFile() {
        @NotNull final Optional<JsonCustomFile> customFile = this.fileManager.getJsonFile(this.location);

        if (customFile.isEmpty()) {
            throw new FileException("Could not find custom file for " + this.location);
        }

        return customFile.get();
    }

    public @NotNull final CommentedConfigurationNode getYamlConfig() {
        return getYamlCustomFile().getConfiguration();
    }

    public @NotNull final YamlCustomFile getYamlCustomFile() {
        @NotNull final Optional<YamlCustomFile> customFile = this.fileManager.getYamlFile(this.location);

        if (customFile.isEmpty()) {
            throw new FileException("Could not find custom file for " + this.location);
        }

        return customFile.get();
    }

    public @NotNull final Path getPath() {
        return this.location;
    }
}