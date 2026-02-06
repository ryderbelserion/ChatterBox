package com.ryderbelserion.chatterbox.paper.api.registry;

import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperUserAdapter;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PaperUserRegistry implements IUserRegistry<Player> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPaper platform = this.instance.getPlatform();

    private final FusionPaper fusion = this.instance.getFusion();

    private final FileManager fileManager = this.fusion.getFileManager();

    private final Path userPath = this.platform.getUserPath();

    private final Map<UUID, PaperUserAdapter> users = new HashMap<>();

    @Override
    public void init() {
        if (Files.notExists(this.userPath)) {
            try {
                Files.createDirectory(this.userPath);
            } catch (final IOException exception) {
                this.fusion.log(Level.WARNING, "Could not create %s directory".formatted(this.userPath));
            }
        }

        this.users.put(ChatterBoxPlugin.CONSOLE_UUID, new PaperUserAdapter());
    }

    @Override
    public PaperUserAdapter addUser(@NonNull final Player player) {
        final String username = player.getName();
        final String locale = player.locale().toString();

        final UUID uuid = player.getUniqueId();

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

        final PaperUserAdapter user = new PaperUserAdapter(player);

        user.setLocale(locale);

        this.users.putIfAbsent(uuid, user);

        return user;
    }

    @Override
    public PaperUserAdapter removeUser(@NotNull final UUID uuid) {
        this.fileManager.removeFile(this.userPath.resolve("%s.json".formatted(uuid)));

        return this.users.remove(uuid);
    }

    @Override
    public Optional<PaperUserAdapter> getUser(@NotNull UUID uuid) {
        return Optional.of(this.users.get(uuid));
    }

    @Override
    public @NotNull final IUser getConsole() {
        return this.users.get(ChatterBoxPlugin.CONSOLE_UUID);
    }
}