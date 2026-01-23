package com.ryderbelserion.chatterbox.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DisconnectListener implements EventListener<PlayerDisconnectEvent> {

    private final static String default_message = "<dark_gray>[<red>-</red>]</dark_gray> {player}";

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform platform = this.instance.getPlatform();

    private final HytaleUserRegistry registry = this.platform.getUserRegistry();

    private final HytaleUserRegistry userRegistry = this.platform.getUserRegistry();

    private final FusionHytale fusion = this.instance.getFusion();

    @Override
    public void init(EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();

            this.registry.removeUser(player.getUuid());

            final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

            final AtomicReference<String> reference = new AtomicReference<>("");

            if (config.node("root", "traffic", "quit-message", "toggle").getBoolean(true)) {
                final Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{player}", player.getUsername());

                this.userRegistry.getUser(player.getUuid()).ifPresent(user -> {
                    final GroupAdapter adapter = user.getGroupAdapter();

                    final Map<String, String> map = adapter.getPlaceholders();

                    if (!map.isEmpty()) {
                        placeholders.putAll(map);
                    }

                    reference.set(adapter.getPrimaryGroup().toLowerCase());
                });

                final Universe universe = Universe.get();

                final String group = reference.get();

                if (!config.hasChild("root", "traffic", "quit-message", "groups", group, "title")) {
                    final CommentedConfigurationNode configuration = config.node("root", "traffic", "quit-message", "title");

                    if (configuration.node("toggle").getBoolean(false)) {
                        sendTitle(player, configuration, placeholders);

                        return;
                    }

                    final CommentedConfigurationNode node = config.node("root", "traffic", "quit-message", "output");

                    final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                    universe.sendMessage(this.fusion.asMessage(player, output, placeholders));

                    return;
                }

                final CommentedConfigurationNode configuration = config.node("root", "traffic", "quit-message", "groups", group, "title");

                if (config.node("root", "traffic", "quit-message", "title", "toggle").getBoolean(false)) {
                    sendTitle(player, configuration, placeholders);

                    return;
                }

                final CommentedConfigurationNode node = config.node("root", "traffic", "quit-message", "groups", group, "output");

                final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                universe.sendMessage(this.fusion.asMessage(player, output, placeholders));
            }
        });
    }

    @Override
    public Class<PlayerDisconnectEvent> getEvent() {
        return PlayerDisconnectEvent.class;
    }

    private void sendTitle(@NotNull final PlayerRef player, @NotNull final CommentedConfigurationNode title, @NotNull final Map<String, String> placeholders) {
        final Universe universe = Universe.get();

        final Message header = this.fusion.asMessage(player,
                title.node("header").getString("Player has quit!"),
                placeholders
        );

        final Message footer = this.fusion.asMessage(
                player,
                title.node("footer").getString("{player}"),
                placeholders
        );

        final int duration = title.node("delay", "duration").getInt(5);
        final int fadeIn = title.node("delay", "fade", "in").getInt(1);
        final int fadeOut = title.node("delay", "fade", "out").getInt(1);

        universe.getPlayers().forEach(reference -> {
            final UUID uuid = reference.getWorldUuid();

            if (uuid != null) {
                final World world = universe.getWorld(uuid);

                if (world != null) {
                    world.execute(() -> EventTitleUtil.showEventTitleToPlayer(reference, header, footer, true, null, duration,
                            fadeIn,
                            fadeOut));
                }
            }
        });
    }
}