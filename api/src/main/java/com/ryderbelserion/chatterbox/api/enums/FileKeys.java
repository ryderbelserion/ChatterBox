package com.ryderbelserion.chatterbox.api.enums;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.core.files.types.configurate.YamlCustomFile;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Optional;

@NullMarked
public enum FileKeys {

    discord("config.yml", "discord"),
    alerts("alerts.yml", "discord"),

    server("server.json"),
    config("config.yml"),
    chat("chat.yml");

    private final FusionKyori fusion = (FusionKyori) FusionProvider.api();
    private final FileManager fileManager = this.fusion.getFileManager();
    private final Path path = this.fusion.getDataPath();

    private final Path location; // the file location
    private final Path folder;

    FileKeys(final String fileName, final String folder) {
        this.folder = this.path.resolve(folder);
        this.location = this.folder.resolve(fileName);
    }

    FileKeys(final String name) {
        this.location = this.path.resolve(name);
        this.folder = this.path;
    }

    public  final BasicConfigurationNode getJsonConfig() {
        return getJsonCustomFile().getConfiguration();
    }

    public JsonCustomFile getJsonCustomFile() {
         final Optional<JsonCustomFile> customFile = this.fileManager.getJsonFile(this.location);

        if (customFile.isEmpty()) {
            throw new FusionException("Could not find custom file for " + this.location);
        }

        return customFile.get();
    }

    public final CommentedConfigurationNode getYamlConfig() {
        return getYamlCustomFile().getConfiguration();
    }

    public final YamlCustomFile getYamlCustomFile() {
         final Optional<YamlCustomFile> customFile = this.fileManager.getYamlFile(this.location);

        if (customFile.isEmpty()) {
            throw new FusionException("Could not find custom file for " + this.location);
        }

        return customFile.get();
    }

    public final Path getPath() {
        return this.location;
    }
}