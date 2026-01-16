package com.ryderbelserion.chatterbox.listeners;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Support;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.chatterbox.api.utils.StringUtils;
import com.ryderbelserion.chatterbox.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PostConnectListener implements EventListener<PlayerConnectEvent> {

    private final static String default_message = "<dark_gray>[<green>+</green>]</dark_gray> {player}";

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform plugin = this.instance.getPlugin();

    private final MessageRegistry messageRegistry = this.instance.getMessageRegistry();

    private final UserManager userManager = instance.getUserManager();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();
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

                final Optional<com.ryderbelserion.chatterbox.users.objects.User> chattyUser = this.userManager.getUser(player.getUuid());

                chattyUser.ifPresentOrElse(action -> execute(player, action.getComponent(Messages.message_of_the_day), placeholders, delay),
                        () -> execute(player, this.messageRegistry.getMessage(Messages.message_of_the_day), placeholders, delay));
            }

            String primaryGroup = "";

            if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) {
                final Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{player}", playerName);

                if (Support.luckperms.isEnabled()) {
                    final LuckPerms luckperms = LuckPermsProvider.get();

                    final User user = luckperms.getPlayerAdapter(PlayerRef.class).getUser(player);

                    primaryGroup = user.getPrimaryGroup().toLowerCase();

                    final CachedMetaData data = user.getCachedData().getMetaData();

                    final String prefix = data.getPrefix();
                    final String suffix = data.getSuffix();

                    placeholders.put("{prefix}", prefix == null ? "N/A" : prefix);
                    placeholders.put("{suffix}", suffix == null ? "N/A" : suffix);
                }

                final CommentedConfigurationNode title = primaryGroup.isBlank() ? config.node("root", "traffic", "join-message", "title") : config.node("root", "traffic", "join-message", "groups", primaryGroup, "title");

                if (title.node("toggle").getBoolean(false)) {
                    final Message header = this.plugin.getComponent(
                            player,
                            title.node("header").getString("Player has joined!"),
                            placeholders
                    );

                    final Message footer = this.plugin.getComponent(
                            player,
                            title.node("footer").getString("{player}"),
                            placeholders
                    );

                    final Universe universe = Universe.get();

                    final int duration = title.node("delay", "duration").getInt(5);
                    final int fadeIn = title.node("delay", "fade", "in").getInt(1);
                    final int fadeOut = title.node("delay", "fade", "out").getInt(1);

                    universe.getPlayers().forEach(reference -> {
                        final UUID uuid = reference.getWorldUuid();

                        if (uuid != null) {
                            final World world = universe.getWorld(uuid);

                            if (world != null) {
                                world.execute(() -> EventTitleUtil.showEventTitleToPlayer(reference, header, footer, true, null, duration,
                                        fadeIn,
                                        fadeOut));
                            }
                        }
                    });
                } else {
                    final CommentedConfigurationNode node = primaryGroup.isBlank() ? config.node("root", "traffic", "join-message", "output") : config.node("root", "traffic", "join-message", "groups", primaryGroup, "output");

                    final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                    Universe.get().sendMessage(this.plugin.getComponent(player, output, placeholders));
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

    private void execute(@NotNull final IMessageReceiver receiver, @NotNull final com.ryderbelserion.chatterbox.messages.objects.Message message, @NotNull final Map<String, String> placeholders, final int delay) {
        if (delay > 0) {
            HytaleServer.SCHEDULED_EXECUTOR.schedule(
                    () -> message.send(receiver, placeholders),
                    delay, TimeUnit.SECONDS
            );


            return;
        }

        message.send(receiver, placeholders);
    }
}