package com.ryderbelserion.chatterbox.listeners.chat;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.api.enums.Support;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.api.utils.StringUtils;
import com.ryderbelserion.chatterbox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class ChatListener implements EventListener<PlayerChatEvent> {

    @Override
    public void init(final EventRegistry registry) {
        registry.registerGlobal(PlayerChatEvent.class, event -> {
            final CommentedConfigurationNode config = Configs.chat.getYamlConfig();

            if (config.node("chat", "format", "toggle").getBoolean(true)) {
                String format = config.node("chat", "format", "default").getString("{player} <gold>-> <reset>{message}");

                final Map<String, String> placeholders = new HashMap<>();

                final PlayerRef player = event.getSender();

                placeholders.put("{player}", player.getUsername());
                placeholders.put("{message}", event.getContent());

                if (Support.luckperms.isEnabled()) {
                    final LuckPerms luckperms = LuckPermsProvider.get();

                    final User user = luckperms.getPlayerAdapter(PlayerRef.class).getUser(player);

                    final String primaryGroup = user.getPrimaryGroup();

                    final String group = config.node("chat", "format", "groups", primaryGroup.toLowerCase()).getString("");

                    if (!group.isBlank()) {
                        format = group;
                    }

                    final CachedMetaData data = user.getCachedData().getMetaData();

                    final String prefix = data.getPrefix();
                    final String suffix = data.getSuffix();

                    placeholders.put("{prefix}", prefix == null ? "N/A" : prefix);
                    placeholders.put("{suffix}", suffix == null ? "N/A" : suffix);
                }

                final String safeFormat = format;

                event.setFormatter((_, _) -> ColorUtils.toHytale(MiniMessage.miniMessage().deserialize(StringUtils.replacePlaceholders(safeFormat, placeholders))));
            }
        });
    }

    @Override
    public Class<PlayerChatEvent> getEvent() {
        return PlayerChatEvent.class;
    }
}