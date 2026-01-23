package com.ryderbelserion.chatterbox.hytale;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.hytale.commands.BaseCommand;
import com.ryderbelserion.chatterbox.hytale.listeners.DisconnectListener;
import com.ryderbelserion.chatterbox.hytale.listeners.PostConnectListener;
import com.ryderbelserion.chatterbox.hytale.listeners.chat.ChatListener;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.List;

public class ChatterBox extends JavaPlugin {

    private static ChatterBox instance;

    public ChatterBox(@NonNullDecl final JavaPluginInit init) {
        super(init);

        instance = this;
    }

    private FusionHytale fusion;

    private ChatterBoxPlatform plugin;

    @Override
    protected void start() {
        final Path dataPath = getDataDirectory();
        final Path source = getFile();

        this.fusion = new FusionHytale(getLogger(), source, dataPath.getParent().resolve(dataPath.getFileName().toString().split("_")[0]));
        this.fusion.init();

        this.plugin = new ChatterBoxPlatform(this.fusion);
        this.plugin.init();

        final EventRegistry registry = getEventRegistry();

        List.of(
                // traffic listeners
                new PostConnectListener(),
                new DisconnectListener(),

                // chat listeners
                new ChatListener()
        ).forEach(listener -> listener.init(registry));

        getCommandRegistry().registerCommand(new BaseCommand());
    }

    public @NotNull final ChatterBoxPlatform getPlatform() {
        return this.plugin;
    }

    public @NotNull final FusionHytale getFusion() {
        return this.fusion;
    }

    @ApiStatus.Internal
    public static ChatterBox getInstance() {
        return instance;
    }
}