package com.ryderbelserion.chatterbox.hytale.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleUserAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class DisconnectListener implements EventListener<PlayerDisconnectEvent> {

    private final static String default_message = "<dark_gray>[<red>-</red>]</dark_gray> {player}";

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxHytale platform = this.instance.getPlatform();

    private final HytaleUserRegistry registry = this.platform.getUserRegistry();

    private final FusionHytale fusion = this.instance.getFusion();

    @Override
    public void init(EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();

            final HytaleUserAdapter user = this.registry.removeUser(player.getUuid());

            final Map<String, String> placeholders = new HashMap<>(this.platform.getPlaceholders(user, player.getUsername()));

            final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

            if (config.node("root", "traffic", "quit-message", "toggle").getBoolean(true)) {
                final Universe universe = Universe.get();
                final String group = placeholders.getOrDefault("{group}", "").toLowerCase();

                if (group.isBlank() || !config.hasChild("root", "traffic", "quit-message", "groups", group, "title")) { // this is sent if the group is not found.
                    final CommentedConfigurationNode configuration = config.node("root", "traffic", "quit-message", "title");

                    if (configuration.node("toggle").getBoolean(false)) {
                        this.platform.sendTitle(
                                player,
                                true,
                                configuration.node("header").getString("Player has quit!"),
                                configuration.node("footer").getString("{player}"),
                                configuration.node("delay", "duration").getInt(5),
                                configuration.node("fade", "in").getInt(1),
                                configuration.node("fade", "out").getInt(1),
                                placeholders
                        );

                        return;
                    }

                    final CommentedConfigurationNode node = config.node("root", "traffic", "quit-message", "output");

                    final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                    universe.sendMessage(this.fusion.asMessage(player, output, placeholders));

                    return;
                }

                final CommentedConfigurationNode configuration = config.node("root", "traffic", "quit-message", "groups", group, "title");

                if (config.node("root", "traffic", "quit-message", "title", "toggle").getBoolean(false)) { // the title is sent if the group is found, and the toggle is true.
                    this.platform.sendTitle(
                            player,
                            true,
                            configuration.node("header").getString("Player has quit!"),
                            configuration.node("footer").getString("{player}"),
                            configuration.node("delay", "duration").getInt(5),
                            configuration.node("fade", "in").getInt(1),
                            configuration.node("fade", "out").getInt(1),
                            placeholders
                    );

                    return;
                }

                final CommentedConfigurationNode node = config.node("root", "traffic", "quit-message", "groups", group, "output"); // send this if the toggle for title is false.

                final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                universe.sendMessage(this.fusion.asMessage(player, output, placeholders));
            }
        });
    }

    @Override
    public Class<PlayerDisconnectEvent> getEvent() {
        return PlayerDisconnectEvent.class;
    }
}