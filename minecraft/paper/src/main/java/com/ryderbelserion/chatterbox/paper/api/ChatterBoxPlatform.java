package com.ryderbelserion.chatterbox.paper.api;

import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperContextRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperMessageRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.chatterbox.paper.commands.BaseCommand;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxPlatform extends ChatterBoxPlugin<CommandSender, Component> {

    private final ChatterBox plugin;

    public ChatterBoxPlatform(@NotNull final ChatterBox plugin, @NotNull final FusionPaper fusion) {
        super(fusion);

        this.plugin = plugin;
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

        new BaseCommand(PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this.plugin));
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
    public @NotNull final PaperSenderAdapter getSenderAdapter() {
        return this.userAdapter;
    }

    @Override
    public @NotNull final PaperUserRegistry getUserRegistry() {
        return this.userRegistry;
    }

    @Override
    public @NotNull final Platform getPlatform() {
        return Platform.MINECRAFT;
    }
}