package com.ryderbelserion.chatterbox.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.EventListener;
import com.ryderbelserion.chatterbox.users.UserManager;

public class PostConnectListener implements EventListener<PlayerConnectEvent> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final UserManager userManager = instance.getUserManager();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> this.userManager.addUser(event.getPlayerRef()));
    }

    @Override
    public Class<PlayerConnectEvent> getEvent() {
        return PlayerConnectEvent.class;
    }
}