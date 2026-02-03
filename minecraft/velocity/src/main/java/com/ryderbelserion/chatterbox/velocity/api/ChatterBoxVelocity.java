package com.ryderbelserion.chatterbox.velocity.api;

import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.fusion.FusionVelocity;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class ChatterBoxVelocity extends ChatterBoxPlugin<Audience, Component> {

    private final ChatterBox instance;

    public ChatterBoxVelocity(@NotNull final FusionVelocity fusion, @NotNull final ChatterBox instance) {
        super(fusion);

        this.instance = instance;
    }

    @Override
    public @NotNull final IMessageRegistry getMessageRegistry() {
        return null;
    }

    @Override
    public @NotNull final IContextRegistry getContextRegistry() {
        return null;
    }

    @Override
    public @NotNull final ISenderAdapter getSenderAdapter() {
        return null;
    }

    @Override
    public @NotNull final IUserRegistry getUserRegistry() {
        return null;
    }

    @Override
    public @NotNull final Platform getPlatform() {
        return Platform.VELOCITY;
    }

    @Override
    public final int getPlayerCount() {
        return this.instance.getServer().getPlayerCount();
    }

    @Override
    public void sendTitle(
            @NotNull final Audience sender,
            final boolean notifyServer,
            @NotNull final String title, @NotNull final String subtitle, final int duration, final int fadeIn, final int fadeOut,
            @NotNull final Map<String, String> placeholders
    ) {

    }
}