package com.ryderbelserion.chatterbox.paper.listeners;

import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Map;

public class TrafficListener implements Listener {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final DiscordManager discordManager = this.platform.getDiscordManager();

    private final ConfigManager configManager = this.platform.getConfigManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final DiscordConfig config = this.configManager.getDiscord();

        if (config.isPlayerAlertsEnabled()) {
            final Player player = event.getPlayer();

            final PlayerAlertConfig alertConfig = config.getAlertConfig();

            alertConfig.sendDiscord(player, this.discordManager.getGuild(), PlayerAlert.JOIN_ALERT, Map.of(
                    "{player}", player.getName()
            ));
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