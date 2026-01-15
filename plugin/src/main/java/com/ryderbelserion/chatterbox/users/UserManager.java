package com.ryderbelserion.chatterbox.users;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.objects.User;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class UserManager {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final FileManager fileManager = this.instance.getFileManager();

    private final Map<UUID, User> users = new HashMap<>();

    private final HytaleLogger logger;
    private final Path path;

    public UserManager(@NotNull final Path path, @NotNull final HytaleLogger logger) {
        this.path = path.resolve("users");
        this.logger = logger;
    }

    public void init() {
        if (Files.notExists(this.path)) {
            try {
                Files.createDirectory(this.path);
            } catch (final IOException exception) {
                this.logger.atWarning().log("Could not create %s directory.".formatted(this.path));
            }
        }
    }

    public void addUser(@NotNull final PlayerRef player) {
        final String username = player.getUsername();
        final UUID uuid = player.getUuid();
        final String locale = player.getLanguage();

        final Path file = this.path.resolve("%s.json".formatted(uuid));

        final boolean hasPlayedBefore = Files.exists(file);

        if (!hasPlayedBefore) {
            try {
                Files.createFile(file);
            } catch (final IOException exception) {
                this.logger.atWarning().log("Could not create %s directory.".formatted(file));
            }
        }

        this.fileManager.addFile(file, FileType.JSON, action -> action.removeAction(FileAction.EXTRACT_FILE));

        this.fileManager.getJsonFile(file).ifPresent(action -> {
            final BasicConfigurationNode configuration = action.getConfiguration();

            try {
                configuration.node("display", "username").set(username);
                configuration.node("display", "language").set(locale);
                configuration.node("display", "uuid").set(uuid);

                if (!hasPlayedBefore) {
                    configuration.node("creation", "date").set(Calendar.getInstance().getTimeInMillis());

                    final BasicConfigurationNode server = com.ryderbelserion.chatterbox.api.enums.Files.server.getJsonConfig();

                    configuration.node("creation", "number").set(server.node("player_count").getInt(0) + 1);
                }
            } catch (final SerializationException exception) {
                throw new RuntimeException(exception);
            }

            action.save();
        });

        final User user = new User(uuid, username);

        user.setLocale(locale);

        this.users.put(uuid, user);
    }

    public Optional<User> getUser(@NotNull final UUID uuid) {
        return Optional.of(this.users.get(uuid));
    }
}