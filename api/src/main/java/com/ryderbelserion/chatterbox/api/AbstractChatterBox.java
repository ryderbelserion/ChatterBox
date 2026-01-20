package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.chatterbox.api.messages.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.messages.objects.IMessage;
import com.ryderbelserion.chatterbox.api.users.IUserManager;
import com.ryderbelserion.chatterbox.api.users.objects.IUser;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChatterBox<S, T> {

    public static final String namespace = "chatterbox";

    protected final FusionKyori<S> fusion;
    protected final FileManager fileManager;

    protected final Path dataPath;
    protected final Path source;

    public AbstractChatterBox(@NotNull final FusionKyori<S> fusion) {
        this.fusion = fusion;

        this.dataPath = this.fusion.getDataPath();

        this.fileManager = this.fusion.getFileManager();

        this.source = this.fileManager.getSource();
    }

    public abstract IMessageRegistry getMessageRegistry();

    public abstract IUserManager getUserManager();

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

    public IMessage getComponent(@NotNull final IUser user, @NotNull final Key key) {
        return this.getMessageRegistry().getMessage(key);
    }

    /**
     * Builds a component
     *
     * @param sender the message receiver
     * @param component the unparsed string
     * @param placeholders map of placeholders
     * @return {@link T}
     */
    public abstract T getComponent(@NotNull final S sender, @NotNull final String component, @NotNull final Map<String, String> placeholders);

    /**
     * Builds a component
     *
     * @param sender the message receiver
     * @param component the unparsed string
     * @return {@link T}
     */
    public T getComponent(@NotNull final S sender, @NotNull final String component) {
        return getComponent(sender, component, new HashMap<>());
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