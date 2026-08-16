package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.chatterbox.api.adapters.IServerAdapter;
import com.ryderbelserion.chatterbox.api.configs.IConfigManager;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.api.storage.IStorageHolder;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.files.FileManager;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.nio.file.Path;
import java.util.Map;

public abstract class ChatterBox<S, C, TR> {

    public static final String namespace = "chatterbox";
    public static final FusionKey default_locale = FusionKey.key(namespace, "default");

    protected final MessageRegistry messageRegistry;
    protected final FileManager fileManager;
    protected final FusionCore<S, C, TR> fusion;

    protected final Path dataPath;

    public ChatterBox(@NotNull final FusionCore fusion) {
        this.fusion = fusion;

        this.dataPath = this.fusion.getDataPath();

        this.fileManager = this.fusion.getFileManager();

        this.messageRegistry = this.fusion.getMessageRegistry();
    }

    public void sendTitle(
            @NotNull final S sender, final boolean notifyServer,
            @NotNull final String title, @NotNull final String subtitle, final int duration, final int fadeIn, final int fadeOut,
            @NotNull final Map<String, String> placeholders
    ) {

    }

    public abstract @NonNull IContextRegistry getContextRegistry();

    public abstract @NonNull MessageRegistry getMessageRegistry();

    public abstract @NonNull IStorageHolder getStorageHolder();

    public abstract @NonNull IServerAdapter getServerAdapter();

    public abstract @NonNull IUserRegistry getUserRegistry();

    public abstract @NonNull IConfigManager getConfigManager();

    public abstract @NonNull Platform getPlatform();

    public abstract boolean hasPermission(@NonNull final String permission, @NonNull final S sender);

    public abstract Map<String, String> getPlaceholders(@Nullable final IUser user, @NotNull final String playerName);

    public abstract void loadMessages();

    public abstract void init();

    public abstract void reload();

    public abstract void post();

    public abstract void shutdown();

    public final FileManager getFileManager() {
        return this.fileManager;
    }

    public final FusionCore getFusion() {
        return this.fusion;
    }

    public final Path getDataPath() {
        return this.dataPath;
    }

    public final Path getUserPath() {
        return this.dataPath.resolve("users");
    }
}