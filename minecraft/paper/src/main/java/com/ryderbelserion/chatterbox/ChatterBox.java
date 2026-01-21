package com.ryderbelserion.chatterbox;

import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ChatterBox extends JavaPlugin {

    private FusionPaper fusion;

    private ChatterBoxPlatform platform;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this, getFile().toPath());
        this.fusion.init();

        this.platform = new ChatterBoxPlatform(this.fusion);
        this.platform.init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public @NotNull final ChatterBoxPlatform getPlatform() {
        return this.platform;
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}