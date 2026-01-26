package com.ryderbelserion.chatterbox.hytale.api.registry;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleUserAdapter;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HytaleUserRegistry implements IUserRegistry<PlayerRef> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform platform = this.instance.getPlatform();

    private final FusionHytale fusion = this.instance.getFusion();

    private final FileManager fileManager = this.fusion.getFileManager();

    private final Path userPath = this.platform.getUserPath();

    private final Map<UUID, HytaleUserAdapter> users = new HashMap<>();

    @Override
    public void init() {
        if (Files.notExists(this.userPath)) {
            try {
                Files.createDirectory(this.userPath);
            } catch (final IOException exception) {
                this.fusion.log(Level.WARNING, "Could not create %s directory".formatted(this.userPath));
            }
        }

        this.users.put(ChatterBoxPlugin.CONSOLE_UUID, new HytaleUserAdapter());
    }

    @Override
    public void addUser(@NonNull final PlayerRef player) {
        final String username = player.getUsername();
        final String locale = player.getLanguage();
        final UUID uuid = player.getUuid();

        final Path file = this.userPath.resolve("%s.json".formatted(uuid));

        final boolean hasPlayedBefore = Files.exists(file);

        if (!hasPlayedBefore) {
            try {
                Files.createFile(file);
            } catch (final IOException exception) {
                this.fusion.log(Level.WARNING, "Could not create %s directory".formatted(file));
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
                }
            } catch (final SerializationException exception) {
                throw new RuntimeException(exception);
            }

            action.save();
        });

        final HytaleUserAdapter user = new HytaleUserAdapter(player);

        user.setLocale(locale);

        this.users.putIfAbsent(uuid, user);
    }

    @Override
    public void removeUser(@NotNull final UUID uuid) {
        this.fileManager.removeFile(this.userPath.resolve("%s.json".formatted(uuid)));

        this.users.remove(uuid);
    }

    @Override
    public Optional<HytaleUserAdapter> getUser(@NotNull UUID uuid) {
        return Optional.of(this.users.get(uuid));
    }

    @Override
    public @NotNull final IUser getConsole() {
        return this.users.get(ChatterBoxPlugin.CONSOLE_UUID);
    }
}