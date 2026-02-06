package com.ryderbelserion.chatterbox.hytale.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleUserAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class PostConnectListener implements EventListener<PlayerConnectEvent> {

    private final static String default_message = "<dark_gray>[<green>+</green>]</dark_gray> {player}";

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxHytale platform = this.instance.getPlatform();

    private final HytaleUserRegistry userRegistry = this.platform.getUserRegistry();

    private final FusionHytale fusion = this.instance.getFusion();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();
            final String playerName = player.getUsername();

            final HytaleUserAdapter user = this.userRegistry.addUser(player);

            final Map<String, String> placeholders = new HashMap<>(this.platform.getPlaceholders(user, playerName));

            final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

            this.platform.sendMessageOfTheDay(config, player, placeholders); // send motd

            if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) { // module for join messages is enabled.
                final String group = placeholders.getOrDefault("{group}", "").toLowerCase();
                final Universe universe = Universe.get();

                if (group.isBlank() || !config.hasChild("root", "traffic", "join-message", "groups", group, "title")) {  // this is sent if the group is not found.
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

                if (config.node("root", "traffic", "join-message", "title", "toggle").getBoolean(false)) { // the title is sent if the group is found, and the toggle is true.
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

                final CommentedConfigurationNode node = config.node("root", "traffic", "join-message", "groups", group, "output"); // send this if the toggle for title is false.

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
}