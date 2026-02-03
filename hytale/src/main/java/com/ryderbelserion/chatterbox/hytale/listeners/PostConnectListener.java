package com.ryderbelserion.chatterbox.hytale.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleSenderAdapter;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.hytale.api.listeners.EventListener;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class PostConnectListener implements EventListener<PlayerConnectEvent> {

    private final static String default_message = "<dark_gray>[<green>+</green>]</dark_gray> {player}";

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform platform = this.instance.getPlatform();

    private final HytaleSenderAdapter adapter = this.platform.getSenderAdapter();

    private final HytaleUserRegistry userRegistry = this.platform.getUserRegistry();

    private final FusionHytale fusion = this.instance.getFusion();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();
            final String playerName = player.getUsername();

            this.userRegistry.addUser(player);

            final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

            if (config.node("root", "motd", "toggle").getBoolean(false)) {
                final Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{player}", playerName);

                this.userRegistry.getUser(player.getUuid()).ifPresent(user -> {
                    final GroupAdapter adapter = user.getGroupAdapter();

                    final Map<String, String> map = adapter.getPlaceholders();

                    if (!map.isEmpty()) {
                        placeholders.putAll(map);
                    }
                });

                execute(player, placeholders, config.node("root", "motd", "delay").getInt(0));
            }

            final AtomicReference<String> reference = new AtomicReference<>("");

            if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) {
                final Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{player}", playerName);

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

                if (!config.hasChild("root", "traffic", "join-message", "groups", group, "title")) {
                    final CommentedConfigurationNode configuration = config.node("root", "traffic", "join-message", "title");

                    if (configuration.node("toggle").getBoolean(false)) {
                        this.platform.sendTitle(
                                player,
                                true,
                                configuration.node("header").getString("Player has joined!"),
                                configuration.node("footer").getString("{player}"),
                                configuration.node("delay", "duration").getInt(5),
                                configuration.node("fade", "in").getInt(1),
                                configuration.node("fade", "out").getInt(1),
                                placeholders
                        );

                        return;
                    }

                    final CommentedConfigurationNode node = config.node("root", "traffic", "join-message", "output");

                    final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                    universe.sendMessage(this.fusion.asMessage(player, output, placeholders));

                    return;
                }

                final CommentedConfigurationNode configuration = config.node("root", "traffic", "join-message", "groups", group, "title");

                if (config.node("root", "traffic", "join-message", "title", "toggle").getBoolean(false)) {
                    this.platform.sendTitle(
                            player,
                            true,
                            configuration.node("header").getString("Player has joined!"),
                            configuration.node("footer").getString("{player}"),
                            configuration.node("delay", "duration").getInt(5),
                            configuration.node("fade", "in").getInt(1),
                            configuration.node("fade", "out").getInt(1),
                            placeholders
                    );

                    return;
                }

                final CommentedConfigurationNode node = config.node("root", "traffic", "join-message", "groups", group, "output");

                final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                universe.sendMessage(this.fusion.asMessage(player, output, placeholders));
            }
        });

        registry.registerGlobal(AddPlayerToWorldEvent.class, event -> {
            final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

            if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) {
                event.setBroadcastJoinMessage(false);
            }
        });
    }

    @Override
    public Class<PlayerConnectEvent> getEvent() {
        return PlayerConnectEvent.class;
    }

    private void execute(@NotNull final IMessageReceiver receiver, @NotNull final Map<String, String> placeholders, final int delay) {
        if (delay > 0) {
            HytaleServer.SCHEDULED_EXECUTOR.schedule(
                    () -> this.adapter.sendMessage(receiver, Messages.message_of_the_day, placeholders),
                    delay, TimeUnit.SECONDS
            );


            return;
        }

        this.adapter.sendMessage(receiver, Messages.message_of_the_day, placeholders);
    }
}