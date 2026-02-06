package com.ryderbelserion.chatterbox.paper.listeners;

import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import com.ryderbelserion.fusion.paper.builders.folia.FoliaScheduler;
import com.ryderbelserion.fusion.paper.builders.folia.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;

public class TrafficListener implements Listener {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final DiscordManager discordManager = this.platform.getDiscordManager();

    private final ConfigManager configManager = this.platform.getConfigManager();

    private final PaperSenderAdapter adapter = this.platform.getSenderAdapter();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final DiscordConfig config = this.configManager.getDiscord();

        final Player player = event.getPlayer();

        final Map<String, String> placeholders = Map.of(
                "{player}", player.getName()
        );

        if (config.isPlayerAlertsEnabled()) {
            final PlayerAlertConfig alertConfig = config.getAlertConfig();

            alertConfig.sendDiscord(player, this.discordManager.getGuild(), PlayerAlert.JOIN_ALERT, placeholders);
        }

        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        if (configuration.node("root", "motd", "toggle").getBoolean(false)) {
            final int delay = configuration.node("root", "motd", "delay").getInt(0);

            if (delay > 0) {
                new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
                    @Override
                    public void run() {
                        adapter.sendMessage(player, Messages.message_of_the_day, placeholders);
                    }
                }.runDelayed(delay);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final DiscordConfig config = this.configManager.getDiscord();

        if (config.isPlayerAlertsEnabled()) {
            final Player player = event.getPlayer();

            final PlayerAlertConfig alertConfig = config.getAlertConfig();

            alertConfig.sendDiscord(player, this.discordManager.getGuild(), PlayerAlert.QUIT_ALERT, Map.of(
                    "{player}", player.getName()
            ));
        }
    }
}