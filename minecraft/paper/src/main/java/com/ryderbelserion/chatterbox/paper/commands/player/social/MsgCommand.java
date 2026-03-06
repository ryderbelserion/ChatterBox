package com.ryderbelserion.chatterbox.paper.commands.player.social;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class MsgCommand extends ChatterBoxCommand {

    @Override
    public void run(@NotNull final PaperCommandContext context) {
        final String arg1 = context.getStringArgument("player");
        final CommandSender sender = context.getSender();
        final String name = sender.getName();

        if (arg1.equalsIgnoreCase(name)) {
            this.adapter.sendMessage(sender, Messages.cannot_msg_yourself);

            return;
        }

        final Player player = this.server.getPlayerExact(arg1);

        if (player == null) {
            this.adapter.sendMessage(sender, Messages.target_not_online, Map.of(
                    "{player}",
                    arg1
            ));

            return;
        }

        final String arg2 = context.getStringArgument("msg");

        if (arg2.isBlank()) {
            this.adapter.sendMessage(sender, Messages.msg_cannot_be_blank);

            return;
        }

        this.adapter.sendMessage(sender, Messages.sender_format, Map.of(
                "{message}", arg2,
                "{player}", arg1
        ));

        this.adapter.sendMessage(player, Messages.receiver_format, Map.of(
                "{message}", arg2,
                "{player}", name
        ));
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("msg").requires(this::requirement);

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("player", StringArgumentType.string()).suggests((context, builder) -> {
            final CommandSender sender = context.getSource().getSender();
            final String name = sender.getName();
            final boolean isPlayer = sender instanceof Player;

            for (final Player player : this.server.getOnlinePlayers()) {
                final String playerName = player.getName();

                if (isPlayer && playerName.equalsIgnoreCase(name)) {
                    continue;
                }

                builder.suggest(playerName);
            }

            return builder.buildFuture();
        });

        final RequiredArgumentBuilder<CommandSourceStack, String> arg2 = argument("msg", StringArgumentType.string()).suggests((context, builder) -> builder.buildFuture())
                .executes(context -> {
                    run(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                });

        return root.then(arg1.then(arg2)).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "chatterbox.message",
                        "Sends a message to a player.",
                        PermissionType.TRUE
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}