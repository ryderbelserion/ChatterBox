package com.ryderbelserion.chatterbox.common.enums;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.ChatterBoxProvider;
import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Optional;

public enum Configs {

    config("config.yml"),
    chat("chat.yml"),

    server("server.json");

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Path path;

    Configs(@NotNull final String name) {
        this.path = this.plugin.getDataPath().resolve(name);
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