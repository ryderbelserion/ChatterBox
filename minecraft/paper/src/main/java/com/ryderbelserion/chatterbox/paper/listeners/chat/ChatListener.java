package com.ryderbelserion.chatterbox.paper.listeners.chat;

import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.api.enums.server.ServerState;
import com.ryderbelserion.chatterbox.api.enums.user.UserState;
import com.ryderbelserion.chatterbox.common.api.adapters.ServerAdapter;
import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.enums.messages.Messages;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.paper.listeners.chat.renderers.ChatRender;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ChatListener implements Listener {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final FusionPaper fusion = this.plugin.getFusion();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final ServerAdapter serverAdapter = this.platform.getServerAdapter();

    private final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

    private final DiscordManager discordManager = this.platform.getDiscordManager();

    private final ConfigManager configManager = this.platform.getConfigManager();

    @EventHandler(ignoreCancelled = true)
    public void onMuteChat(AsyncChatEvent event) {
        final Player player = event.getPlayer();

        if (this.serverAdapter.hasState(ServerState.chat_muted) && !Permissions.mute_chat_bypass.hasPermission(player)) {
            Messages.cannot_speak_while_muted.sendMessage(player);

            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        final Player player = event.getPlayer();

        if (this.serverAdapter.hasState(ServerState.chat_muted) && !Permissions.mute_chat_bypass.hasPermission(player)) {
            return;
        }

        final CommentedConfigurationNode configuration = FileKeys.chat.getYamlConfig();

        if (!configuration.node("chat", "format", "toggle").getBoolean(true)) return;

        final UUID uuid = player.getUniqueId();

        this.userRegistry.getUser(uuid).ifPresent(user -> {
            if (user.hasUserState(UserState.staff_chat)) {
                event.setCancelled(true); // cancel event, because staff chat!

                return;
            }

            final AtomicReference<String> reference = new AtomicReference<>(configuration.node("chat", "format", "default").getString("{player} <gold>-> <reset>{message}"));

            final Map<String, String> placeholders = new HashMap<>();

            final GroupAdapter adapter = user.getGroupAdapter();

            final Map<String, String> map = adapter.getPlaceholders();

            if (!map.isEmpty()) {
                placeholders.putAll(map);
            }

            final String groupFormat = configuration.node("chat", "format", "groups", adapter.getPrimaryGroup().toLowerCase()).getString("");

            if (!groupFormat.isBlank()) {
                reference.set(groupFormat);
            }

            event.renderer(new ChatRender(
                    this.fusion,
                    player,
                    reference.get(),
                    event.signedMessage(),
                    placeholders
            ));
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onDiscordChat(AsyncChatEvent event) {
        final Player player = event.getPlayer();

        if (this.serverAdapter.hasState(ServerState.chat_muted) && !Permissions.mute_chat_bypass.hasPermission(player)) {
            return;
        }

        final UUID uuid = player.getUniqueId();

        this.userRegistry.getUser(uuid).ifPresent(user -> {
            if (user.hasUserState(UserState.staff_chat)) { // return, because staff chat!
                return;
            }

            final DiscordConfig config = this.configManager.getDiscord();

            if (config.isEnabled() && config.isPlayerAlertsEnabled()) {
                final PlayerAlertConfig alertConfig = config.getAlertConfig();

                alertConfig.sendDiscord(player, this.discordManager.getGuild(), PlayerAlert.MC_CHAT_ALERT, Map.of(
                        "{player}", player.getName(),
                        "{message}", event.signedMessage().message()
                ));
            }
        });
    }
}