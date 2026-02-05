package com.ryderbelserion.chatterbox.paper;

import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.listeners.CacheListener;
import com.ryderbelserion.chatterbox.paper.listeners.TrafficListener;
import com.ryderbelserion.chatterbox.paper.listeners.chat.ChatListener;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ChatterBox extends JavaPlugin {

    private static ChatterBox instance;

    private ChatterBoxPaper platform;
    private FusionPaper fusion;

    @Override
    public void onEnable() {
        instance = this;

        this.fusion = new FusionPaper(this, getFile().toPath());
        this.fusion.init();

        this.platform = new ChatterBoxPaper(this, this.fusion);
        this.platform.init();

        final PluginManager pluginManager = getServer().getPluginManager();

        List.of(
                new TrafficListener(),

                new CacheListener(),

                new ChatListener()
        ).forEach(event -> pluginManager.registerEvents(event, this));
    }

    @Override
    public void onDisable() {
        if (this.platform != null) {
            this.platform.shutdown();
        }
    }

    public @NotNull final ChatterBoxPaper getPlatform() {
        return this.platform;
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }

    @ApiStatus.Internal
    public static ChatterBox getInstance() {
        return instance;
    }
}