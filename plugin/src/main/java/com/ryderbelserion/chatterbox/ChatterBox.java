package com.ryderbelserion.chatterbox;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.commands.ReloadCommand;
import com.ryderbelserion.chatterbox.listeners.PostConnectListener;
import com.ryderbelserion.chatterbox.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ChatterBox extends JavaPlugin {

    private static ChatterBox instance;

    public ChatterBox(@NonNullDecl final JavaPluginInit init) {
        super(init);

        instance = this;
    }

    private ChatterBoxPlugin plugin;
    private MessageRegistry messageRegistry;
    private UserManager userManager;

    @Override
    protected void setup() {
        super.setup();

        this.plugin = new ChatterBoxPlugin(getDataDirectory(), getFile());
        this.plugin.init();

        this.messageRegistry = new MessageRegistry();
        this.messageRegistry.init();

        this.userManager = new UserManager();
        this.userManager.init();

        final EventRegistry registry = getEventRegistry();

        List.of(
                new PostConnectListener()
        ).forEach(listener -> listener.init(registry));

        getCommandRegistry().registerCommand(new ReloadCommand());
    }

    public void reload() {
        this.plugin.reload();

        this.messageRegistry.init();
    }

    public @NotNull final ChatterBoxPlugin getPlugin() {
        return this.plugin;
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