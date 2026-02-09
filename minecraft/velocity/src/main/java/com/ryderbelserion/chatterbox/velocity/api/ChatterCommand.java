package com.ryderbelserion.chatterbox.velocity.api;

import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.fusion.commands.VelocityCommand;
import org.jetbrains.annotations.NotNull;

public abstract class ChatterCommand extends VelocityCommand {

    protected final ChatterBox plugin;

    public ChatterCommand(@NotNull final ChatterBox plugin) {
        this.plugin = plugin;
    }
}