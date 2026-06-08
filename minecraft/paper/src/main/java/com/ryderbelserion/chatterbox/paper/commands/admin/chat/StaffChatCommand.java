package com.ryderbelserion.chatterbox.paper.commands.admin.chat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.api.enums.user.UserState;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class StaffChatCommand extends ChatterBoxCommand {

    @Override
    public void run(@NotNull final PaperCommandContext context) {
        final CommandSender sender = context.getSender();

        if (context.hasArgument("message")) {
            context.getStringArgument("message").ifPresent(message -> {
                if (message.isBlank()) {
                    this.adapter.sendMessage(sender, Messages.msg_cannot_be_blank);

                    return;
                }

                final UUID uuid = sender instanceof Player player ? player.getUniqueId() : ChatterBoxPlugin.CONSOLE_UUID;
                final String asString = uuid.toString();

                final String username = sender.getName();

                final Map<String, String> placeholders = Map.of(
                        "{message}", message,
                        "{player}", username
                );

                if (context.isPlayer()) { // if player, send to me.
                    final Player player = context.getPlayer();

                    this.adapter.sendMessage(player, Messages.staff_chat_format, placeholders);

                    this.adapter.sendMessage(this.server.getConsoleSender(), Messages.staff_chat_format, placeholders);
                } else { // console sender
                    this.adapter.sendMessage(sender, Messages.staff_chat_format, placeholders);
                }

                for (final Player target : this.server.getOnlinePlayers()) {
                    final UUID id = target.getUniqueId();

                    if (id.toString().equals(asString)) continue;

                    if (!Permissions.staff_chat.hasPermission(target)) continue;

                    this.adapter.sendMessage(target, Messages.staff_chat_format, placeholders);
                }
            });

            return;
        }

        if (!context.isPlayer()) {
            this.adapter.sendMessage(sender, Messages.must_be_player);

            return;
        }

        final Player player = context.getPlayer();

        this.userRegistry.getUser(player.getUniqueId()).ifPresentOrElse(user -> {
            final boolean isStaffChat = user.hasUserState(UserState.staff_chat);

            if (isStaffChat) {
                user.removeUserState(UserState.staff_chat);
            } else {
                user.addUserState(UserState.staff_chat);
            }

            this.adapter.sendMessage(player, isStaffChat ? Messages.staff_chat_disabled : Messages.staff_chat_enabled);
        }, () -> this.adapter.sendMessage(player, Messages.staff_chat_cannot_enable));
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("staffchat").requires(this::requirement)
                .executes(context -> {
                    run(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                });

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("message", StringArgumentType.greedyString()).suggests((context, builder) -> builder.buildFuture())
                .executes(context -> {
                    run(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                });

        return root.then(arg1).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(Permissions.staff_chat.getContext());
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}