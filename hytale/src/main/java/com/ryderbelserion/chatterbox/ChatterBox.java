package com.ryderbelserion.chatterbox;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.registry.HytaleMessageRegistry;
import com.ryderbelserion.chatterbox.commands.BaseCommand;
import com.ryderbelserion.chatterbox.listeners.DisconnectListener;
import com.ryderbelserion.chatterbox.listeners.PostConnectListener;
import com.ryderbelserion.chatterbox.listeners.chat.ChatListener;
import com.rydderbelserion.chatterbox.common.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
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
    private HytaleMessageRegistry messageRegistry;
    private UserManager userManager;

    @Override
    protected void start() {
        final Path dataPath = getDataDirectory();
        final Path source = getFile();

        this.fusion = new FusionHytale(getLogger(), source, dataPath.getParent().resolve(dataPath.getFileName().toString().split("_")[0]));
        this.fusion.init();

        this.plugin = new ChatterBoxPlatform(this.fusion);
        this.plugin.init();

        this.messageRegistry = new HytaleMessageRegistry();
        this.messageRegistry.init();

        this.userManager = new UserManager();
        this.userManager.init();

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

    public void reload() {
        this.fusion.reload();

        this.plugin.reload();

        this.messageRegistry.init();
    }

    public @NotNull final ChatterBoxPlatform getPlugin() {
        return this.plugin;
    }

    public @NotNull final FusionHytale getFusion() {
        return this.fusion;
    }

    public @NotNull final MessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }

    public @NotNull final UserManager getUserManager() {
        return this.userManager;
    }

    @ApiStatus.Internal
    public static ChatterBox getInstance() {
        return instance;
    }
}