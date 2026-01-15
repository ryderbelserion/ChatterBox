package com.ryderbelserion.chatterbox.users;

import com.hypixel.hytale.logger.HytaleLogger;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class UserManager {

    private final HytaleLogger logger;
    private final Path path;

    public UserManager(@NotNull final Path path, @NotNull final HytaleLogger logger) {
        this.path = path.resolve("users");
        this.logger = logger;
    }

    public void init() {
        create(this.path);
    }

    public void addUser(@NotNull final UUID uuid) {
        final Path user = this.path.resolve("%s.json".formatted(uuid.toString()));

        create(user);
    }

    private void create(@NotNull final Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (final IOException exception) {
                this.logger.atWarning().log("Could not create %s directory.".formatted(path));
            }
        }
    }
}