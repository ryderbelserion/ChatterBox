package com.ryderbelserion.chatterbox.velocity.commands.admin.staff;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.velocity.api.ChatterBoxCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.velocity.commands.context.VelocityCommandContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StaffChatCommand extends ChatterBoxCommand {

    @Override
    public void run(@NotNull final VelocityCommandContext context) {
        final CommandSource sender = context.getSender();

        final UUID uuid = sender.get(Identity.UUID).orElse(ChatterBoxPlugin.CONSOLE_UUID);
        final String username = sender.get(Identity.NAME).orElse("Console");

        if (!context.hasArgument("message")) {
            this.adapter.sendMessage(sender, Messages.msg_cannot_be_blank);

            return;
        }

        context.getStringArgument("message").ifPresent(message -> {
            if (message.isBlank()) {
                this.adapter.sendMessage(sender, Messages.msg_cannot_be_blank);

                return;
            }

            final String asString = uuid.toString();

            final Map<String, String> placeholders = Map.of(
                    "{message}", message,
                    "{player}", username
            );

            if (sender instanceof Player player) { // if player, send to me.
                this.adapter.sendMessage(player, Messages.staff_chat_format, placeholders);

                this.adapter.sendMessage(this.server.getConsoleCommandSource(), Messages.staff_chat_format, placeholders);
            } else { // console sender
                this.adapter.sendMessage(sender, Messages.staff_chat_format, placeholders);
            }

            for (final Player target : this.server.getAllPlayers()) {
                final UUID id = target.getUniqueId();

                if (id.toString().equals(asString)) continue;

                if (!Permissions.staff_chat.hasPermission(target)) continue;

                this.adapter.sendMessage(target, Messages.staff_chat_format, placeholders);
            }
        });
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSource> literal() {
        final LiteralArgumentBuilder<CommandSource> root = BrigadierCommand.literalArgumentBuilder("staffchat").requires(this::requirement)
                .executes(context -> {
                    run(new VelocityCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                });

        final RequiredArgumentBuilder<CommandSource, String> arg1 = BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString()).suggests((context, builder) -> builder.buildFuture())
                .executes(context -> {
                    run(new VelocityCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                });

        return root.then(arg1).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(Permissions.staff_chat.getContext());
    }

    @Override
    public final boolean requirement(@NotNull final CommandSource context) {
        return context.hasPermission(getPermissions().getFirst().getPermission());
    }
}