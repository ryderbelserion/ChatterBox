package com.ryderbelserion.chatterbox.api.enums;

import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Optional;

public enum Files {

    server("server.json");

    private final ChatterBox instance = ChatterBox.getInstance();

    private final FileManager fileManager = this.instance.getFileManager();

    private final Path path;

    Files(@NotNull final String name) {
        final Path directory = this.instance.getDataPath();

        this.path = directory.resolve(name);
    }

    public @NotNull final BasicConfigurationNode getJsonConfig() {
        return getJsonCustomFile().getConfiguration();
    }

    public JsonCustomFile getJsonCustomFile() {
        @NotNull final Optional<JsonCustomFile> customFile = this.fileManager.getJsonFile(this.path);

        if (customFile.isEmpty()) {
            throw new FileException("Could not find custom file for " + this.path);
        }

        return customFile.get();
    }

    public @NotNull final CommentedConfigurationNode getYamlConfig() {
        return getYamlCustomFile().getConfiguration();
    }

    public @NotNull final YamlCustomFile getYamlCustomFile() {
        @NotNull final Optional<YamlCustomFile> customFile = this.fileManager.getYamlFile(this.path);

        if (customFile.isEmpty()) {
            throw new FileException("Could not find custom file for " + this.path);
        }

        return customFile.get();
    }

    public @NotNull final Path getPath() {
        return this.path;
    }
}
