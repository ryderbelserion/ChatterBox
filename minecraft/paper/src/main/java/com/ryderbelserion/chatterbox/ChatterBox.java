package com.ryderbelserion.chatterbox;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ChatterBox extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this, getFile().toPath());
        this.fusion.init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}