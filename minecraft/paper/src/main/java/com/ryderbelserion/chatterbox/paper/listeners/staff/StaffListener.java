package com.ryderbelserion.chatterbox.paper.listeners.staff;

import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.api.enums.user.UserState;
import com.ryderbelserion.chatterbox.common.enums.messages.Messages;
import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.Map;
import java.util.UUID;

public class StaffListener implements Listener {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

    private final Server server = this.plugin.getServer();

    @EventHandler(ignoreCancelled = true)
    public void onStaffChat(AsyncChatEvent event) {
        final String message = event.signedMessage().message();

        if (message.isBlank()) return;

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final String asString = uuid.toString();
        final String username = player.getName();

        this.userRegistry.getUser(uuid).ifPresent(origin -> {
            if (!origin.hasUserState(UserState.staff_chat)) {
                return;
            }

            if (!Permissions.staff_chat.hasPermission(player)) {
                origin.removeUserState(UserState.staff_chat);

                Messages.staff_chat_disabled.sendMessage(player);

                return;
            }

            Messages.staff_chat_format.sendMessage(player, Map.of(
                    "{message}", message,
                    "{player}", username
            ));

            for (final Player target : this.server.getOnlinePlayers()) {
                final UUID id = target.getUniqueId();

                if (id.toString().equals(asString)) continue;

                if (!Permissions.staff_chat.hasPermission(target)) continue;

                this.userRegistry.getUser(id).ifPresent(_ -> Messages.staff_chat_format.sendMessage(target, Map.of(
                        "{message}", message,
                        "{player}", username
                )));
            }

            Messages.staff_chat_format.sendMessage(this.server.getConsoleSender(), Map.of(
                    "{message}", message,
                    "{player}", username
            ));
        });
    }
}