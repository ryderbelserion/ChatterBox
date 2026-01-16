package com.ryderbelserion.chatterbox.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Support;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PostConnectListener implements EventListener<PlayerConnectEvent> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform plugin = this.instance.getPlugin();

    private final MessageRegistry messageRegistry = this.instance.getMessageRegistry();

    private final UserManager userManager = instance.getUserManager();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();
            final UUID uuid = player.getUuid();
            final String playerName = player.getUsername();

            this.userManager.addUser(player);

            final CommentedConfigurationNode config = Configs.config.getYamlConfig();

            if (config.node("root", "motd", "toggle").getBoolean(false)) {
                final Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{player}", playerName);

                if (Support.luckperms.isEnabled()) {
                    final LuckPerms luckperms = LuckPermsProvider.get();

                    final User user = luckperms.getPlayerAdapter(PlayerRef.class).getUser(player);

                    final CachedMetaData data = user.getCachedData().getMetaData();

                    final String prefix = data.getPrefix();
                    final String suffix = data.getSuffix();

                    placeholders.put("{prefix}", prefix == null ? "N/A" : prefix);
                    placeholders.put("{suffix}", suffix == null ? "N/A" : suffix);
                }

                final int delay = config.node("root", "motd", "delay").getInt(0);

                if (delay > 0) {
                    HytaleServer.SCHEDULED_EXECUTOR.schedule(
                            () -> this.messageRegistry.getMessage(Messages.message_of_the_day).send(player, placeholders),
                            delay, TimeUnit.SECONDS
                    );

                    return;
                }

                this.messageRegistry.getMessage(Messages.message_of_the_day).send(player, placeholders);
            }

            if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) {
                final String output = config.node("root", "traffic", "join-message", "output").getString("<dark_gray>[<green>+</green>]</dark_gray> {player}");

                final Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{player}", playerName);

                if (Support.luckperms.isEnabled()) {
                    final LuckPerms luckperms = LuckPermsProvider.get();

                    final User user = luckperms.getPlayerAdapter(PlayerRef.class).getUser(player);

                    final CachedMetaData data = user.getCachedData().getMetaData();

                    final String prefix = data.getPrefix();
                    final String suffix = data.getSuffix();

                    placeholders.put("{prefix}", prefix == null ? "N/A" : prefix);
                    placeholders.put("{suffix}", suffix == null ? "N/A" : suffix);
                }

                for (final PlayerRef reference : Universe.get().getPlayers()) {
                    this.plugin.sendMessage(reference, output, placeholders);
                }
            }
        });

        registry.registerGlobal(AddPlayerToWorldEvent.class, event -> {
            final CommentedConfigurationNode config = Configs.config.getYamlConfig();

            if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) {
                event.setBroadcastJoinMessage(false);
            }
        });
    }

    @Override
    public Class<PlayerConnectEvent> getEvent() {
        return PlayerConnectEvent.class;
    }
}