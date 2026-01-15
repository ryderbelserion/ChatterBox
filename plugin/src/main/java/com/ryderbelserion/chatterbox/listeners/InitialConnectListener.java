package com.ryderbelserion.chatterbox.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.EventListener;
import com.ryderbelserion.chatterbox.users.UserManager;
import java.util.UUID;

public class InitialConnectListener implements EventListener<PlayerSetupConnectEvent> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final UserManager userManager = instance.getUserManager();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final UUID uuid = event.getUuid();

            this.userManager.addUser(uuid);
        });
    }

    @Override
    public Class<PlayerSetupConnectEvent> getEvent() {
        return PlayerSetupConnectEvent.class;
    }
}