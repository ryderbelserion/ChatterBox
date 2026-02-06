package com.ryderbelserion.chatterbox.paper.commands.player;

import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.chatterbox.paper.commands.AnnotationFeature;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class MotdCommand extends AnnotationFeature {

    @Override
    public void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser) {
        parser.parse(this);
    }

    @Command("chatterbox motd")
    @CommandDescription("Shows the message of the day!")
    @Permission(value = "chatterbox.motd", mode = Permission.Mode.ALL_OF)
    public void motd(final CommandSender sender) {
        final Map<String, String> placeholders = new HashMap<>();

        placeholders.put("{player}", sender.getName());

        if (sender instanceof Player player) {
            this.userRegistry.getUser(player.getUniqueId()).ifPresent(user -> {
                final GroupAdapter adapter = user.getGroupAdapter();

                final Map<String, String> map = adapter.getPlaceholders();

                if (!map.isEmpty()) {
                    placeholders.putAll(map);
                }
            });
        }

        this.adapter.sendMessage(sender, Messages.message_of_the_day, placeholders);
    }
}