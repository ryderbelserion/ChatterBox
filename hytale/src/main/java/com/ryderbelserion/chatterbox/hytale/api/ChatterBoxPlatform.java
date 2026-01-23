package com.ryderbelserion.chatterbox.hytale.api;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleContextRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleMessageRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleSenderAdapter;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxPlatform extends ChatterBoxPlugin<IMessageReceiver, Message> {

    private HytaleMessageRegistry messageRegistry;
    private HytaleContextRegistry contextRegistry;
    private HytaleSenderAdapter userAdapter;
    private HytaleUserRegistry userRegistry;

    public ChatterBoxPlatform(@NotNull final FusionKyori<IMessageReceiver> fusion) {
        super(fusion);
    }

    @Override
    public void init() {
        super.init();

        this.contextRegistry = new HytaleContextRegistry();

        this.userRegistry = new HytaleUserRegistry();
        this.userRegistry.init();

        this.messageRegistry = new HytaleMessageRegistry();
        this.messageRegistry.init();

        this.userAdapter = new HytaleSenderAdapter(this);

        post();
    }

    @Override
    public void post() {
        super.post();
    }

    @Override
    public @NotNull final HytaleContextRegistry getContextRegistry() {
        return this.contextRegistry;
    }

    @Override
    public @NotNull final HytaleMessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }

    @Override
    public @NotNull final HytaleSenderAdapter getSenderAdapter() {
        return this.userAdapter;
    }

    @Override
    public @NotNull final HytaleUserRegistry getUserRegistry() {
        return this.userRegistry;
    }

    @Override
    public @NotNull final Platform getPlatform() {
        return Platform.HYTALE;
    }
}