package com.ryderbelserion.chatterbox.paper.listeners;

import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.common.api.discord.DiscordManager;
import com.ryderbelserion.chatterbox.common.configs.discord.DiscordConfig;
import com.ryderbelserion.chatterbox.common.configs.discord.features.alerts.PlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.common.managers.ConfigManager;
import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperUserAdapter;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.folia.FoliaScheduler;
import com.ryderbelserion.fusion.paper.builders.folia.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class TrafficListener implements Listener {

    private final static String default_message = "<dark_gray>[<green>+</green>]</dark_gray> {player}";

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final FusionPaper fusion = this.plugin.getFusion();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

    private final DiscordManager discordManager = this.platform.getDiscordManager();

    private final ConfigManager configManager = this.platform.getConfigManager();

    private final PaperSenderAdapter adapter = this.platform.getSenderAdapter();

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final PaperUserAdapter user = this.userRegistry.addUser(player);
        final DiscordConfig discordConfig = this.configManager.getDiscord();

        final Map<String, String> placeholders = new HashMap<>(this.platform.getPlaceholders(user, user.getUsername()));

        if (discordConfig.isPlayerAlertsEnabled()) {
            final PlayerAlertConfig alertConfig = discordConfig.getAlertConfig();

            alertConfig.sendDiscord(player, this.discordManager.getGuild(), PlayerAlert.JOIN_ALERT, placeholders);
        }

        final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

        if (config.node("root", "motd", "toggle").getBoolean(false)) {
            final int delay = config.node("root", "motd", "delay").getInt(0);

            if (delay > 0) {
                new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
                    @Override
                    public void run() {
                        adapter.sendMessage(player, Messages.message_of_the_day, placeholders);
                    }
                }.runDelayed(delay);
            }
        }

        if (config.node("root", "traffic", "join-message", "toggle").getBoolean(true)) {
            final String group = placeholders.getOrDefault("{group}", "").toLowerCase();

            if (group.isBlank() || !config.hasChild("root", "traffic", "join-message", "groups", group, "title")) {  // this is sent if the group is not found.
                final CommentedConfigurationNode configuration = config.node("root", "traffic", "join-message", "title");

                if (configuration.node("toggle").getBoolean(false)) {
                    event.joinMessage(null); // yeet join message if sending title

                    this.platform.sendTitle(
                            player,
                            true,
                            configuration.node("header").getString("Player has joined!"),
                            configuration.node("footer").getString("{player}"),
                            configuration.node("delay", "duration").getInt(5),
                            configuration.node("fade", "in").getInt(1),
                            configuration.node("fade", "out").getInt(1),
                            placeholders
                    );

                    return;
                }

                final CommentedConfigurationNode node = config.node("root", "traffic", "join-message", "output");

                final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                event.joinMessage(this.fusion.asComponent(player, output, placeholders));

                return;
            }

            final CommentedConfigurationNode configuration = config.node("root", "traffic", "join-message", "groups", group, "title");

            if (config.node("root", "traffic", "join-message", "title", "toggle").getBoolean(false)) { // the title is sent if the group is found, and the toggle is true.
                event.joinMessage(null); // yeet join message if sending title

                this.platform.sendTitle(
                        player,
                        true,
                        configuration.node("header").getString("Player has joined!"),
                        configuration.node("footer").getString("{player}"),
                        configuration.node("delay", "duration").getInt(5),
                        configuration.node("fade", "in").getInt(1),
                        configuration.node("fade", "out").getInt(1),
                        placeholders
                );

                return;
            }

            final CommentedConfigurationNode node = config.node("root", "traffic", "join-message", "groups", group, "output"); // send this if the toggle for title is false.

            final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

            event.joinMessage(this.fusion.asComponent(player, output, placeholders));
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final PaperUserAdapter user = this.userRegistry.removeUser(player.getUniqueId());

        final Map<String, String> placeholders = new HashMap<>(this.platform.getPlaceholders(user, user.getUsername()));

        final DiscordConfig discordConfig = this.configManager.getDiscord();

        if (discordConfig.isPlayerAlertsEnabled()) {
            final PlayerAlertConfig alertConfig = discordConfig.getAlertConfig();

            alertConfig.sendDiscord(player, this.discordManager.getGuild(), PlayerAlert.QUIT_ALERT, placeholders);
        }

        final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

        if (config.node("root", "traffic", "quit-message", "toggle").getBoolean(true)) {
            final String group = placeholders.getOrDefault("{group}", "").toLowerCase();

            if (group.isBlank() || !config.hasChild("root", "traffic", "quit-message", "groups", group, "title")) { // this is sent if the group is not found.
                final CommentedConfigurationNode configuration = config.node("root", "traffic", "quit-message", "title");

                if (configuration.node("toggle").getBoolean(false)) {
                    this.platform.sendTitle(
                            player,
                            true,
                            configuration.node("header").getString("Player has quit!"),
                            configuration.node("footer").getString("{player}"),
                            configuration.node("delay", "duration").getInt(5),
                            configuration.node("fade", "in").getInt(1),
                            configuration.node("fade", "out").getInt(1),
                            placeholders
                    );

                    return;
                }

                final CommentedConfigurationNode node = config.node("root", "traffic", "quit-message", "output");

                final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

                event.quitMessage(this.fusion.asComponent(player, output, placeholders));

                return;
            }

            final CommentedConfigurationNode configuration = config.node("root", "traffic", "quit-message", "groups", group, "title");

            if (config.node("root", "traffic", "quit-message", "title", "toggle").getBoolean(false)) { // the title is sent if the group is found, and the toggle is true.
                this.platform.sendTitle(
                        player,
                        true,
                        configuration.node("header").getString("Player has quit!"),
                        configuration.node("footer").getString("{player}"),
                        configuration.node("delay", "duration").getInt(5),
                        configuration.node("fade", "in").getInt(1),
                        configuration.node("fade", "out").getInt(1),
                        placeholders
                );

                return;
            }

            final CommentedConfigurationNode node = config.node("root", "traffic", "quit-message", "groups", group, "output"); // send this if the toggle for title is false.

            final String output = node.isList() ? StringUtils.toString(StringUtils.getStringList(node, default_message)) : node.getString(default_message);

            event.quitMessage(this.fusion.asComponent(player, output, placeholders));
        }
    }
}