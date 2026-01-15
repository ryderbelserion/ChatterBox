package com.ryderbelserion.chatterbox;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.ryderbelserion.chatterbox.listeners.InitialConnectListener;
import com.ryderbelserion.chatterbox.users.UserManager;
import com.ryderbelserion.fusion.files.FileManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public class ChatterBox extends JavaPlugin {

    private static ChatterBox instance;

    public ChatterBox(@NonNullDecl final JavaPluginInit init) {
        super(init);
    }

    private FileManager fileManager;
    private UserManager userManager;

    @Override
    protected void setup() {
        super.setup();

        final Path path = getDataDirectory();

        this.fileManager = new FileManager(getFile(), path);

        this.userManager = new UserManager(path, getLogger());
        this.userManager.init();

        final EventRegistry registry = getEventRegistry();

        List.of(
                new InitialConnectListener()
        ).forEach(listener -> listener.init(registry));

        instance = this;
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final UserManager getUserManager() {
        return this.userManager;
    }

    @ApiStatus.Internal
    public static ChatterBox getInstance() {
        return instance;
    }
}