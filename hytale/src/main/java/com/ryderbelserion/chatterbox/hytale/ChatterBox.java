package com.ryderbelserion.chatterbox.hytale;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.commands.BaseCommand;
import com.ryderbelserion.chatterbox.hytale.listeners.DisconnectListener;
import com.ryderbelserion.chatterbox.hytale.listeners.PostConnectListener;
import com.ryderbelserion.chatterbox.hytale.listeners.chat.ChatListener;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.jspecify.annotations.NonNull;
import java.util.List;

public class ChatterBox extends JavaPlugin {

    private static ChatterBox instance;

    public ChatterBox(@NonNull final JavaPluginInit init) {
        super(init);

        instance = this;
    }

    private FusionHytale fusion;

    private ChatterBoxHytale plugin;

    @Override
    protected void start() {
        this.fusion = new FusionHytale(this, getFile());
        this.fusion.init().post();

        this.plugin = new ChatterBoxHytale(this, this.fusion);
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

    public @NonNull final ChatterBoxHytale getPlatform() {
        return this.plugin;
    }

    public @NonNull final FusionHytale getFusion() {
        return this.fusion;
    }

    public static ChatterBox getInstance() {
        return instance;
    }
}