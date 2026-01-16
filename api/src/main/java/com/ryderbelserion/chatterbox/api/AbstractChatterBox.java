package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChatterBox<S> {

    public static final String namespace = "chatterbox";

    protected final FileManager fileManager;

    protected final Path dataPath;
    protected final Path modPath;

    /**
     * The constructor
     *
     * @param dataPath the mod folder
     * @param modPath the location of the .jar file
     */
    public AbstractChatterBox(final Path dataPath, final Path modPath) {
        this.dataPath = dataPath.getParent().resolve(dataPath.getFileName().toString().split("_")[0]);
        this.modPath = modPath;

        this.fileManager = new FileManager(this.modPath, this.dataPath);
    }

    /**
     * Sends a message to the sender
     *
     * @param sender {@link S}
     * @param component {@link String}
     * @param placeholders a map of placeholders
     */
    public abstract void sendMessage(@NotNull final S sender, @NotNull final String component, @NotNull final Map<String, String> placeholders);

    /**
     * Sends a message to the sender
     *
     * @param sender {@link S}
     * @param component {@link String}
     */
    public void sendMessage(@NotNull final S sender, @NotNull final String component) {
        sendMessage(sender, component, new HashMap<>());
    }

    /**
     * Start the mod
     */
    public abstract void init();

    /**
     * Reloads the mod
     */
    public abstract void reload();

    public final FileManager getFileManager() {
        return this.fileManager;
    }

    public final Path getDataPath() {
        return this.dataPath;
    }

    public final Path getUserPath() {
        return this.dataPath.resolve("users");
    }

    public final Path getModPath() {
        return this.modPath;
    }
}