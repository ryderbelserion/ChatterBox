package com.ryderbelserion.chatterbox.hytale.listeners.chat;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.hytale.api.listeners.EventListener;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ChatListener implements EventListener<PlayerChatEvent> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxHytale platform = this.instance.getPlatform();

    private final DiscordManager discordManager = this.platform.getDiscordManager();

    private final HytaleUserRegistry userRegistry = this.platform.getUserRegistry();

    private final ConfigManager configManager = this.platform.getConfigManager();

    private final FusionHytale fusion = this.instance.getFusion();

    @Override
    public void init(final EventRegistry registry) {
        registry.registerGlobal(PlayerChatEvent.class, event -> {
            final CommentedConfigurationNode config = FileKeys.chat.getYamlConfig();

            final PlayerRef player = event.getSender();
            final String content = event.getContent();
            final UUID uuid = player.getUuid();

            final Map<String, String> placeholders = this.platform.getPlaceholders(this.userRegistry.getUser(uuid).orElse(null), player.getUsername());

            placeholders.put("{message}", content);

            if (config.node("chat", "format", "toggle").getBoolean(true)) {
                final AtomicReference<String> reference = new AtomicReference<>(config.node("chat", "format", "default").getString("{player} <gold>-> <reset>{message}"));

                this.userRegistry.getUser(uuid).ifPresent(user -> {
                    final GroupAdapter adapter = user.getGroupAdapter();

                    final String groupFormat = config.node("chat", "format", "groups", adapter.getPrimaryGroup().toLowerCase()).getString("");

                    if (!groupFormat.isBlank()) {
                        reference.set(groupFormat);
                    }
                });

                event.setFormatter((ref, msg) -> this.fusion.asMessage(player, reference.get(), placeholders));
            }

            final DiscordConfig discordConfig = this.configManager.getDiscord();

            if (discordConfig.isEnabled() && discordConfig.isPlayerAlertsEnabled()) {
                final PlayerAlertConfig alertConfig = discordConfig.getAlertConfig();

                alertConfig.sendDiscord(player, this.discordManager.getGuild(), PlayerAlert.MC_CHAT_ALERT, placeholders);
            }
        });
    }

    @Override
    public Class<PlayerChatEvent> getEvent() {
        return PlayerChatEvent.class;
    }
}