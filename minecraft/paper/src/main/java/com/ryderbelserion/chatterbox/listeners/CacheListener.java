package com.ryderbelserion.chatterbox.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CacheListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        /*final Player player = event.getPlayer();

        this.userRegistry.addUser(player);

        final Map<String, String> placeholders = Map.of(
                "{player}", player.getName()
        );

        event.joinMessage(this.messageRegistry.getMessage(Messages.join_message).getComponent(player, placeholders));

        final CommentedConfigurationNode config = Files.config.getYamlConfig();

        if (config.node("root", "motd", "toggle").getBoolean(false)) {
            final int delay = config.node("root", "motd", "delay").getInt(0);

            if (delay != -1) {
                new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
                    @Override
                    public void run() {
                        messageRegistry.getMessage(Messages.message_of_the_day).send(player, placeholders);
                    }
                }.runDelayed(delay);
            }
        }*/
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        /*final Player player = event.getPlayer();

        event.quitMessage(this.messageRegistry.getMessage(Messages.quit_message).getComponent(player, Map.of(
                "{player}", player.getName()
        )));

        this.userRegistry.removeUser(player);*/
    }
}