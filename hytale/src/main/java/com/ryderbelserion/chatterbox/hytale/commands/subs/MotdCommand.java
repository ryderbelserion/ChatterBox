package com.ryderbelserion.chatterbox.hytale.commands.subs;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleSenderAdapter;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class MotdCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxHytale platform = this.instance.getPlatform();

    private final HytaleSenderAdapter adapter = this.platform.getSenderAdapter();

    private final HytaleUserRegistry userRegistry = this.platform.getUserRegistry();

    public MotdCommand() {
        super("motd", "Shows the message of the day!", false);

        requirePermission("chatterbox.command.motd");
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        final CommandSender sender = context.sender();

        final Map<String, String> placeholders = new HashMap<>();

        placeholders.put("{player}", sender.getDisplayName());

        this.userRegistry.getUser(sender.getUuid()).ifPresent(user -> {
            final GroupAdapter adapter = user.getGroupAdapter();

            final Map<String, String> map = adapter.getPlaceholders();

            if (!map.isEmpty()) {
                placeholders.putAll(map);
            }
        });

        this.adapter.sendMessage(sender, Messages.message_of_the_day, placeholders);
    }
}