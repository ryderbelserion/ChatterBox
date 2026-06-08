package com.ryderbelserion.chatterbox.velocity.api;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityContextRegistry;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityUserRegistry;
import com.ryderbelserion.chatterbox.velocity.api.registry.adapters.VelocitySenderAdapter;
import com.ryderbelserion.chatterbox.velocity.commands.BaseCommand;
import com.ryderbelserion.chatterbox.velocity.commands.admin.BroadcastCommand;
import com.ryderbelserion.chatterbox.velocity.commands.admin.ReloadCommand;
import com.ryderbelserion.chatterbox.velocity.commands.player.HubCommand;
import com.ryderbelserion.fusion.velocity.FusionVelocity;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChatterBoxVelocity extends ChatterBoxPlugin<CommandSource, ScheduledTask> {

    private final ChatterBox instance;
    private final ProxyServer server;

    public ChatterBoxVelocity(@NotNull final FusionVelocity fusion, @NotNull final ChatterBox instance) {
        super(fusion);

        this.instance = instance;
        this.server = this.instance.getServer();
    }

    private VelocityContextRegistry contextRegistry;
    private VelocitySenderAdapter userAdapter;
    private VelocityUserRegistry userRegistry;

    @Override
    public void init() {
        super.init();

        this.contextRegistry = new VelocityContextRegistry();

        this.userRegistry = new VelocityUserRegistry();
        this.userRegistry.init();

        this.userAdapter = new VelocitySenderAdapter(this);

        post();
    }

    @Override
    public void post() {
        super.post();

        final CommandManager commandManager = this.server.getCommandManager();

        List.of( // individual commands like /hub
                new HubCommand()
        ).forEach(key -> {
            final BrigadierCommand brigadier = key.getBrigadierCommand();

            commandManager.register(commandManager.metaBuilder(brigadier).build(), brigadier);
        });

        LiteralArgumentBuilder<CommandSource> root = new BaseCommand().registerPermissions().literal().createBuilder();

        List.of(
                new BroadcastCommand(),
                new ReloadCommand()
        ).forEach(key -> root.then(key.registerPermissions().literal()));

        final BrigadierCommand brigadier = new BrigadierCommand(root);

        commandManager.register(commandManager.metaBuilder(brigadier).build(), brigadier);

        this.instance.getFactory().make(this.instance, 30926);
    }

    @Override
    public @NotNull final VelocityContextRegistry getContextRegistry() {
        return this.contextRegistry;
    }

    @Override
    public @NotNull final VelocitySenderAdapter getSenderAdapter() {
        return this.userAdapter;
    }

    @Override
    public @NotNull final VelocityUserRegistry getUserRegistry() {
        return this.userRegistry;
    }

    @Override
    public @NotNull final Platform getPlatform() {
        return Platform.VELOCITY;
    }

    @Override
    public final boolean hasPermission(@NonNull final String permission, @NonNull final CommandSource sender) {
        return sender.hasPermission(permission);
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
            final @NonNull CommandSource sender, final boolean notifyServer,
            final @NotNull String title, final @NotNull String subtitle, final int duration, final int fadeIn, final int fadeOut,
            final @NotNull Map<String, String> placeholders
    ) {
        super.sendTitle(sender, notifyServer, title, subtitle, duration, fadeIn, fadeOut, placeholders);
    }
}