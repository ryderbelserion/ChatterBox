package com.ryderbelserion.chatterbox.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PostConnectListener implements EventListener<PlayerConnectEvent> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final MessageRegistry messageRegistry = this.instance.getMessageRegistry();

    private final UserManager userManager = instance.getUserManager();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();

            this.userManager.addUser(player);

            final CommentedConfigurationNode config = Configs.config.getYamlConfig();

            if (config.node("root", "motd", "toggle").getBoolean(false)) {
                final int delay = config.node("root", "motd", "delay").getInt(0);

                final Map<String, String> placeholders = Map.of(
                        "{player}", player.getUsername()
                );

                if (delay > 0) {
                    HytaleServer.SCHEDULED_EXECUTOR.schedule(
                            () -> this.messageRegistry.getMessage(Messages.message_of_the_day).send(player, placeholders),
                            delay, TimeUnit.SECONDS
                    );

                    return;
                }

                this.messageRegistry.getMessage(Messages.message_of_the_day).send(player, placeholders);
            }
        });
    }

    @Override
    public Class<PlayerConnectEvent> getEvent() {
        return PlayerConnectEvent.class;
    }
}