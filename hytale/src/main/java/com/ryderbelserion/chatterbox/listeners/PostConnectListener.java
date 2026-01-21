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
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.api.registry.adapters.HytaleSenderAdapter;
import com.ryderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.enums.Support;
import com.ryderbelserion.chatterbox.api.listeners.EventListener;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PostConnectListener implements EventListener<PlayerConnectEvent> {

    private final static String default_message = "<dark_gray>[<green>+</green>]</dark_gray> {player}";

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform platform = this.instance.getPlugin();

    private final HytaleSenderAdapter adapter = this.platform.getSenderAdapter();

    private final HytaleUserRegistry userRegistry = this.platform.getUserRegistry();

    private final FusionHytale fusion = this.instance.getFusion();

    @Override
    public void init(final EventRegistry registry) {
        registry.register(getEvent(), event -> {
            final PlayerRef player = event.getPlayerRef();
            final String playerName = player.getUsername();

            this.userRegistry.addUser(player);

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

                execute(player, placeholders, config.node("root", "motd", "delay").getInt(0));
            }

            String group = "";

            if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) {
                final Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{player}", playerName);

                if (Support.luckperms.isEnabled()) {
                    final LuckPerms luckperms = LuckPermsProvider.get();

                    final User user = luckperms.getPlayerAdapter(PlayerRef.class).getUser(player);

                    group = user.getPrimaryGroup().toLowerCase();

                    final CachedMetaData data = user.getCachedData().getMetaData();

                    final String prefix = data.getPrefix();
                    final String suffix = data.getSuffix();

                    placeholders.put("{prefix}", prefix == null ? "N/A" : prefix);
                    placeholders.put("{suffix}", suffix == null ? "N/A" : suffix);
                }

                final Universe universe = Universe.get();

                if (!config.hasChild("root", "traffic", "join-message", "groups", group, "title")) {
                    final CommentedConfigurationNode configuration = config.node("root", "traffic", "join-message", "title");

                    if (configuration.node("toggle").getBoolean(false)) {
                        sendTitle(player, configuration, placeholders);

                        return;
                    }

                    final CommentedConfigurationNode node = config.node("root", "traffic", "join-message", "output");

                    final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                    universe.sendMessage(this.fusion.asMessage(player, output, placeholders));

                    return;
                }

                final CommentedConfigurationNode configuration = config.node("root", "traffic", "join-message", "groups", group, "title");

                if (config.node("root", "traffic", "join-message", "title", "toggle").getBoolean(false)) {
                    sendTitle(player, configuration, placeholders);

                    return;
                }

                final CommentedConfigurationNode node = config.node("root", "traffic", "join-message", "groups", group, "output");

                final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                universe.sendMessage(this.fusion.asMessage(player, output, placeholders));
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

    private void execute(@NotNull final IMessageReceiver receiver, @NotNull final Map<String, String> placeholders, final int delay) {
        if (delay > 0) {
            HytaleServer.SCHEDULED_EXECUTOR.schedule(
                    () -> this.adapter.sendMessage(receiver, Messages.message_of_the_day, placeholders),
                    delay, TimeUnit.SECONDS
            );


            return;
        }

        this.adapter.sendMessage(receiver, Messages.message_of_the_day, placeholders);
    }

    private void sendTitle(@NotNull final PlayerRef player, @NotNull final CommentedConfigurationNode title, @NotNull final Map<String, String> placeholders) {
        final Universe universe = Universe.get();

        final Message header = this.fusion.asMessage(player,
                title.node("header").getString("Player has joined!"),
                placeholders
        );

        final Message footer = this.fusion.asMessage(
                player,
                title.node("footer").getString("{player}"),
                placeholders
        );

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
    }
}