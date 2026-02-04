package com.ryderbelserion.chatterbox.paper.listeners;

import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TrafficListener implements Listener {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final DiscordManager discordManager = this.platform.getDiscordManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
    }
}