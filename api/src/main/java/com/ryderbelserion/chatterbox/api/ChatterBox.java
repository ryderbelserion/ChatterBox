package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.chatterbox.api.adapters.IServerAdapter;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;

public abstract class ChatterBox<S> {

    public static final String namespace = "chatterbox";
    public static final FusionKey default_locale = FusionKey.key(namespace, "default");

    protected final FusionKyori<S, FileManager> fusion;
    protected final MessageRegistry messageRegistry;
    protected final FileManager fileManager;

    protected final Path dataPath;

    public ChatterBox(@NotNull final FusionKyori<S, FileManager> fusion) {
        this.fusion = fusion;

        this.dataPath = this.fusion.getDataPath();

        this.fileManager = this.fusion.getFileManager();

        this.messageRegistry = this.fusion.getMessageRegistry();
    }

    public abstract @NonNull IContextRegistry getContextRegistry();

    public abstract @NonNull MessageRegistry getMessageRegistry();

    public abstract @NonNull IServerAdapter getServerAdapter();

    public abstract @NonNull IUserRegistry getUserRegistry();

    public abstract @NonNull Platform getPlatform();

    public abstract boolean hasPermission(@NonNull final String permission, @NonNull final S sender);

    public abstract void loadMessages();

    public abstract void init();

    public abstract void reload();

    public abstract void post();

    public abstract void shutdown();

    public final FileManager getFileManager() {
        return this.fileManager;
    }

    public final FusionKyori getFusion() {
        return this.fusion;
    }

    public final Path getDataPath() {
        return this.dataPath;
    }

    public final Path getUserPath() {
        return this.dataPath.resolve("users");
    }
}