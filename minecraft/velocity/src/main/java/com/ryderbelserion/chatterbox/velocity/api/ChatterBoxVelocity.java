package com.ryderbelserion.chatterbox.velocity.api;

import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.fusion.FusionVelocity;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChatterBoxVelocity extends ChatterBoxPlugin<Audience, Component, ScheduledTask> {

    private final ChatterBox instance;
    private final ProxyServer server;

    public ChatterBoxVelocity(@NotNull final FusionVelocity fusion, @NotNull final ChatterBox instance) {
        super(fusion);

        this.instance = instance;
        this.server = this.instance.getServer();
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
        return this.server.getPlayerCount();
    }

    @Override
    public void runTask(@NotNull final Consumer<ScheduledTask> consumer, final long seconds, final long delay) {
        final Scheduler scheduler = this.server.getScheduler();

        final Scheduler.TaskBuilder builder = scheduler.buildTask(
                this.instance,
                consumer
        );

        if (seconds > 0) {
            builder.repeat(seconds, TimeUnit.SECONDS);
        }

        if (delay > 0) {
            builder.delay(delay, TimeUnit.SECONDS);
        }

        builder.schedule();
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