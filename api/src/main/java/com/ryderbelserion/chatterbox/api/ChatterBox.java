package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.chatterbox.api.adapters.IPlayerAdapter;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public abstract class ChatterBox<S, T> {

    public static final String namespace = "chatterbox";

    protected final FusionKyori<S> fusion;
    protected final FileManager fileManager;

    protected final Path dataPath;
    protected final Path source;

    public ChatterBox(@NotNull final FusionKyori<S> fusion) {
        this.fusion = fusion;

        this.dataPath = this.fusion.getDataPath();

        this.fileManager = this.fusion.getFileManager();

        this.source = this.fileManager.getSource();
    }

    public abstract IMessageRegistry getMessageRegistry();

    public abstract IContextRegistry getContextRegistry();

    public abstract IUserRegistry getUserRegistry();

    public abstract <C> @NotNull IPlayerAdapter<C> getPlayerAdapter(@NotNull final Class<C> object);

    public abstract void init();

    public abstract void reload();

    public abstract void post();

    public final FileManager getFileManager() {
        return this.fileManager;
    }

    public final FusionKyori<S> getFusion() {
        return this.fusion;
    }

    public final Path getDataPath() {
        return this.dataPath;
    }

    public final Path getUserPath() {
        return this.dataPath.resolve("users");
    }

    public final Path getSource() {
        return this.source;
    }
}