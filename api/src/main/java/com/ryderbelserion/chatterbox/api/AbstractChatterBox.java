package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.fusion.files.FileManager;
import java.nio.file.Path;

public abstract class AbstractChatterBox {

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
     * Start the mod
     */
    public abstract void init();

    /**
     * Reloads the mod
     */
    public abstract void reload();

    /**
     * Gets the total amount of users in the universe's world folder
     *
     * @return the total amount of users
     */
    public abstract int getServerUsers();

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