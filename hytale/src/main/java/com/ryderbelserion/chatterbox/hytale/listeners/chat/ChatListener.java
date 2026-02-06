package com.ryderbelserion.chatterbox.hytale.listeners.chat;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.hytale.api.listeners.EventListener;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ChatListener implements EventListener<PlayerChatEvent> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxHytale platform = this.instance.getPlatform();

    private final HytaleUserRegistry userRegistry = this.platform.getUserRegistry();

    private final FusionHytale fusion = this.instance.getFusion();

    @Override
    public void init(final EventRegistry registry) {
        registry.registerGlobal(PlayerChatEvent.class, event -> {
            final CommentedConfigurationNode config = FileKeys.chat.getYamlConfig();

            if (config.node("chat", "format", "toggle").getBoolean(true)) {
                final AtomicReference<String> reference = new AtomicReference<>(config.node("chat", "format", "default").getString("{player} <gold>-> <reset>{message}"));

                final Map<String, String> placeholders = new HashMap<>();

                final PlayerRef player = event.getSender();

                final UUID uuid = player.getUuid();

                placeholders.put("{player}", player.getUsername());
                placeholders.put("{message}", event.getContent());

                this.userRegistry.getUser(uuid).ifPresent(user -> {
                    final GroupAdapter adapter = user.getGroupAdapter();

                    final Map<String, String> map = adapter.getPlaceholders();

                    if (!map.isEmpty()) {
                        placeholders.putAll(map);
                    }

                    final String groupFormat = config.node("chat", "format", "groups", adapter.getPrimaryGroup().toLowerCase()).getString("");

                    if (!groupFormat.isBlank()) {
                        reference.set(groupFormat);
                    }
                });

                event.setFormatter((ref, msg) -> this.fusion.asMessage(player, reference.get(), placeholders));
            }
        });
    }

    @Override
    public Class<PlayerChatEvent> getEvent() {
        return PlayerChatEvent.class;
    }
}