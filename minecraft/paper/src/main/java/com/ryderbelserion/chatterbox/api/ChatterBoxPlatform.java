package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.chatterbox.api.registry.*;
import com.ryderbelserion.chatterbox.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxPlatform extends ChatterBoxPlugin<CommandSender, Component> {

    public ChatterBoxPlatform(@NotNull final FusionPaper fusion) {
        super(fusion);
    }

    private PaperMessageRegistry messageRegistry;
    private PaperContextRegistry contextRegistry;
    private PaperSenderAdapter userAdapter;
    private PaperUserRegistry userRegistry;

    @Override
    public void init() {
        super.init();

        this.contextRegistry = new PaperContextRegistry();

        this.userRegistry = new PaperUserRegistry();
        this.userRegistry.init();

        this.messageRegistry = new PaperMessageRegistry();
        this.messageRegistry.init();

        this.userAdapter = new PaperSenderAdapter(this);

        post();
    }

    @Override
    public void post() {
        super.post();
    }

    @Override
    public @NotNull final PaperContextRegistry getContextRegistry() {
        return this.contextRegistry;
    }

    @Override
    public @NotNull final PaperMessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }

    @Override
    public @NotNull final PaperUserRegistry getUserRegistry() {
        return this.userRegistry;
    }

    @Override
    public @NotNull final PaperSenderAdapter getSenderAdapter() {
        return this.userAdapter;
    }
}