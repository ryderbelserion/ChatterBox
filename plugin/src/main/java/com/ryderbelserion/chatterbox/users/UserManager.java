package com.ryderbelserion.chatterbox.users;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.rydderbelserion.chatterbox.ChatterBoxPlugin;
import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.users.objects.User;
import com.ryderbelserion.chatterbox.api.users.IUserManager;
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

public class UserManager implements IUserManager<PlayerRef, User> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final HytaleLogger logger = this.instance.getLogger();

    private final ChatterBoxPlugin plugin = this.instance.getPlugin();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Path userPath = this.plugin.getUserPath();

    private final Map<UUID, User> users = new HashMap<>();

    public void init() {
        if (Files.notExists(this.userPath)) {
            try {
                Files.createDirectory(this.userPath);
            } catch (final IOException exception) {
                this.logger.atWarning().log("Could not create %s directory.".formatted(this.userPath));
            }
        }
    }

    @Override
    public void addUser(@NotNull final PlayerRef player) {
        final String username = player.getUsername();
        final String locale = player.getLanguage();
        final UUID uuid = player.getUuid();

        final Path file = this.userPath.resolve("%s.json".formatted(uuid));

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

                    final BasicConfigurationNode server = Configs.server.getJsonConfig();

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

    @Override
    public Optional<User> getUser(@NotNull final UUID uuid) {
        return Optional.of(this.users.get(uuid));
    }
}