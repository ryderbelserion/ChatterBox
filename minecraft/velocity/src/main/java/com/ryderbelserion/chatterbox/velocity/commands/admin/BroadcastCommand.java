package com.ryderbelserion.chatterbox.velocity.commands.admin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.velocity.api.ChatterCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.velocity.commands.context.VelocityCommandContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BroadcastCommand extends ChatterCommand {

    @Override
    public void run(@NotNull final VelocityCommandContext context) {
        final CommandSource sender = context.getSender();

        context.getStringArgument("message").ifPresentOrElse(message -> {
            this.server.getAllPlayers().forEach(player -> player.sendMessage(this.fusion.asComponent(sender, message)));
        }, () -> this.adapter.sendMessage(sender, Messages.msg_cannot_be_blank));
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSource> literal() {
        final LiteralArgumentBuilder<CommandSource> root = BrigadierCommand.literalArgumentBuilder("broadcast").requires(this::requirement);

        final RequiredArgumentBuilder<CommandSource, String> arg1 = BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.string()).suggests((_, builder) -> builder.buildFuture());

        return root.then(arg1.executes(context -> {
            run(new VelocityCommandContext(context));

            return Command.SINGLE_SUCCESS;
        })).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "chatterbox.broadcast",
                        "Sends a message to the server!"
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSource context) {
        return context.hasPermission(getPermissions().getFirst().getPermission());
    }
}