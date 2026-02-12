package com.ryderbelserion.chatterbox.velocity;

import com.google.inject.Inject;
import com.ryderbelserion.chatterbox.velocity.api.ChatterBoxVelocity;
import com.ryderbelserion.fusion.FusionVelocity;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.nio.file.Path;

@Plugin(id = "chatterbox", name = "ChatterBox", version = "1.0.0",
        url = "https://github.com/ChatterBox", description = "Spice up your chat!", authors = {"ryderbelserion"})
public class ChatterBox {

    private static ChatterBox instance;

    private final PluginDescription description;
    private final FusionVelocity fusion;
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public ChatterBox(@NotNull final ProxyServer server, @NotNull final Logger logger, @NotNull final PluginDescription description, @DataDirectory final Path directory) {
        instance = this;

        this.description = description;
        this.server = server;
        this.logger = logger;

        this.fusion = new FusionVelocity(this.logger, description.getSource().orElseThrow(), directory);
        this.fusion.init();
    }

    private ChatterBoxVelocity platform;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.platform = new ChatterBoxVelocity(this.fusion, this);
        this.platform.init();
    }

    @Subscribe
    public void onProxyShutDown(ProxyShutdownEvent event) {
        if (this.platform != null) {
            this.platform.shutdown();
        }
    }

    public @NotNull final PluginDescription getDescription() {
        return this.description;
    }

    public @NotNull final ChatterBoxVelocity getPlatform() {
        return this.platform;
    }

    public @NotNull final FusionVelocity getFusion() {
        return this.fusion;
    }

    public @NotNull final ProxyServer getServer() {
        return this.server;
    }

    public static ChatterBox getInstance() {
        return instance;
    }
}