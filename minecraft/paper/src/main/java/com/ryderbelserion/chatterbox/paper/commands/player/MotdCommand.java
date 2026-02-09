package com.ryderbelserion.chatterbox.paper.commands.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.chatterbox.paper.api.ChatterCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotdCommand extends ChatterCommand {

    @Override
    public void run(@NotNull final PaperCommandContext context) {
        final CommandSender sender = context.getSender();

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

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("motd").requires(this::requirement)
                .executes(context -> {
                    run(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "chatterbox.motd",
                        "Shows the message of the day!"
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}