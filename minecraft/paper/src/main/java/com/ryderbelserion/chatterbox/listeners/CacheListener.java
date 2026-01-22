package com.ryderbelserion.chatterbox.listeners;

import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.fusion.paper.builders.folia.FoliaScheduler;
import com.ryderbelserion.fusion.paper.builders.folia.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;

public class CacheListener implements Listener {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final ChatterBoxPlatform platform = this.plugin.getPlatform();

    private final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

    private final PaperSenderAdapter adapter = this.platform.getSenderAdapter();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.userRegistry.addUser(player);

        final Map<String, String> placeholders = Map.of(
                "{player}", player.getName()
        );

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
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.userRegistry.removeUser(player.getUniqueId());
    }
}