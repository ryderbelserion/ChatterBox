package com.ryderbelserion.chatterbox.velocity.listeners;

import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.chatterbox.velocity.api.ChatterBoxVelocity;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityUserRegistry;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;

public class TrafficListener {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final ChatterBoxVelocity platform = this.plugin.getPlatform();

    private final VelocityUserRegistry userRegistry = this.platform.getUserRegistry();

    @Subscribe
    public void onPlayerJoin(ServerPostConnectEvent event) {
        final Player player = event.getPlayer();

        this.userRegistry.addUser(player);
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        final Player player = event.getPlayer();

        this.userRegistry.removeUser(player.getUniqueId());
    }
}