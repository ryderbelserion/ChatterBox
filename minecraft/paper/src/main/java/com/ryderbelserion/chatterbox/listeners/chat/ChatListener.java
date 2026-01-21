package com.ryderbelserion.chatterbox.listeners.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        //final CommentedConfigurationNode config = Files.chat.getYamlConfig();

        //if (!config.node("chat", "format", "toggle").getBoolean(false)) return;

        //event.renderer(new ChatRender(this.fusion, event.getPlayer(), config.node("chat", "format", "default").getString("%luckperms_prefix% {player} <gold>-> <reset>{message}"), event.signedMessage()));
    }
}