package com.ryderbelserion.chatterbox.paper.api;

import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperContextRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperMessageRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.chatterbox.paper.commands.BaseCommand;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.folia.FoliaScheduler;
import com.ryderbelserion.fusion.paper.builders.folia.Scheduler;
import io.papermc.paper.util.Tick;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

public class ChatterBoxPaper extends ChatterBoxPlugin<CommandSender, Component, FoliaScheduler> {

    private final ChatterBox plugin;
    private final Server server;

    public ChatterBoxPaper(@NotNull final ChatterBox plugin, @NotNull final FusionPaper fusion) {
        super(fusion);

        this.plugin = plugin;
        this.server = this.plugin.getServer();
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

    @Override
    public final int getPlayerCount() {
        return this.server.getOnlinePlayers().size();
    }

    @Override
    public void runTask(@NotNull final Consumer<FoliaScheduler> consumer, final long seconds, final long delay) {
        this.fusion.log(Level.WARNING, String.valueOf(Tick.tick().fromDuration(Duration.ofSeconds(seconds))));

        if (seconds > 0) {
            new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
                @Override
                public void run() {
                    consumer.accept(this);
                }
            }.runAtFixedRate(delay, Tick.tick().fromDuration(Duration.ofSeconds(seconds))); // 20 ticks = 1 second

            return;
        }

        new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
            @Override
            public void run() {
                consumer.accept(this);
            }
        }.runDelayed(delay);
    }

    @Override
    public void broadcast(@NotNull final CommandSender sender, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        this.server.broadcast(this.fusion.asComponent(sender, message, placeholders));
    }

    @Override
    public void broadcast(@NotNull final String message, @NotNull final Map<String, String> placeholders) {
        broadcast(this.server.getConsoleSender(), message, placeholders);
    }

    @Override
    public void sendTitle(
            @NotNull final CommandSender sender,
            final boolean notifyServer,
            @NotNull final String title, @NotNull final String subtitle, final int duration, final int fadeIn, final int fadeOut,
            @NotNull final Map<String, String> placeholders
    ) {

    }
}