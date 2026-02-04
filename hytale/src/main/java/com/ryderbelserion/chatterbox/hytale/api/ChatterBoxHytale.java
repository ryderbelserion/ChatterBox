package com.ryderbelserion.chatterbox.hytale.api;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleContextRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleMessageRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleSenderAdapter;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.UUID;

public class ChatterBoxHytale extends ChatterBoxPlugin<IMessageReceiver, Message> {

    private HytaleMessageRegistry messageRegistry;
    private HytaleContextRegistry contextRegistry;
    private HytaleSenderAdapter userAdapter;
    private HytaleUserRegistry userRegistry;

    public ChatterBoxHytale(@NotNull final FusionHytale fusion) {
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

    @Override
    public final int getPlayerCount() {
        return Universe.get().getPlayerCount();
    }

    @Override
    public void broadcast(@NotNull final IMessageReceiver sender, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        final FusionHytale fusion = (FusionHytale) this.fusion;

        sender.sendMessage(fusion.asMessage(sender, message, placeholders));
    }

    @Override
    public void broadcast(@NotNull final String message, @NotNull final Map<String, String> placeholders) {
        broadcast(Universe.get(), message, placeholders);
    }

    @Override
    public void sendTitle(
            @NotNull final IMessageReceiver sender,
            final boolean alertServer,
            @NotNull final String title, @NotNull final String subtitle, final int duration, final int fadeIn, final int fadeOut,
            @NotNull final Map<String, String> placeholders
    ) {
        final Universe universe = Universe.get();

        final FusionHytale fusion = (FusionHytale) this.fusion;

        if (alertServer) {
            final Message header = fusion.asMessage(sender,
                    title,
                    placeholders
            );

            final Message footer = fusion.asMessage(
                    sender,
                    subtitle,
                    placeholders
            );

            universe.getPlayers().forEach(reference -> {
                final UUID uuid = reference.getWorldUuid();

                if (uuid != null) {
                    final World world = universe.getWorld(uuid);

                    if (world != null) {
                        world.execute(() -> EventTitleUtil.showEventTitleToPlayer(
                                reference,
                                header,
                                footer,
                                true,
                                null,
                                duration,
                                fadeIn,
                                fadeOut)
                        );
                    }
                }
            });

            return;
        }

        if (!(sender instanceof PlayerRef player)) {
            return;
        }

        final UUID uuid = player.getWorldUuid();

        if (uuid != null) {
            final World world = universe.getWorld(uuid);

            if (world != null) {
                final Message header = fusion.asMessage(sender,
                        title,
                        placeholders
                );

                final Message footer = fusion.asMessage(
                        sender,
                        title,
                        placeholders
                );

                world.execute(() -> EventTitleUtil.showEventTitleToPlayer(
                        player,
                        header,
                        footer,
                        true,
                        null,
                        duration,
                        fadeIn,
                        fadeOut)
                );
            }
        }
    }
}