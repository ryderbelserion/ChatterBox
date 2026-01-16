package com.ryderbelserion.chatterbox.listeners.chat;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.api.utils.StringUtils;
import com.ryderbelserion.chatterbox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;

public class ChatListener implements EventListener<PlayerChatEvent> {

    @Override
    public void init(final EventRegistry registry) {
        registry.registerGlobal(PlayerChatEvent.class, event -> {
            final CommentedConfigurationNode config = Configs.chat.getYamlConfig();

            if (config.node("chat", "format", "toggle").getBoolean(true)) {
                final String format = config.node("chat", "format", "default").getString("{player} <gold>-> <reset>{message}");

                final Map<String, String> placeholders = Map.of(
                        "{player}", event.getSender().getUsername(),
                        "{message}", event.getContent()
                );

                event.setFormatter((player, _) -> ColorUtils.toHytale(MiniMessage.miniMessage().deserialize(StringUtils.replacePlaceholders(format, placeholders))));
            }
        });
    }

    @Override
    public Class<PlayerChatEvent> getEvent() {
        return PlayerChatEvent.class;
    }
}