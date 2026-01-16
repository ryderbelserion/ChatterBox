package com.ryderbelserion.chatterbox.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.users.UserManager;

public class DisconnectListener implements EventListener<PlayerDisconnectEvent> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform plugin = this.instance.getPlugin();

    private final UserManager userManager = instance.getUserManager();

    @Override
    public void init(EventRegistry registry) {
        registry.register(getEvent(), event -> this.userManager.removeUser(event.getPlayerRef().getUuid()));
    }

    @Override
    public Class<PlayerDisconnectEvent> getEvent() {
        return PlayerDisconnectEvent.class;
    }
}