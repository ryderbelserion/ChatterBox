package com.ryderbelserion.chatterbox.paper.commands.admin.chat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.api.enums.user.UserState;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.enums.messages.Messages;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.identity.Identity;
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

        final UUID uuid = sender.get(Identity.UUID).orElse(ChatterBoxPlugin.CONSOLE_UUID);
        final String username = sender.get(Identity.NAME).orElse("Console");

        if (context.hasArgument("message")) {
            context.getStringArgument("message").ifPresent(message -> {
                if (message.isBlank()) {
                    Messages.msg_cannot_be_blank.sendMessage(sender);

                    return;
                }
                final String asString = uuid.toString();

                final Map<String, String> placeholders = Map.of(
                        "{message}", message,
                        "{player}", username
                );

                if (sender instanceof Player player) { // if player, send to me.
                    Messages.staff_chat_format.sendMessage(player, placeholders);

                    Messages.staff_chat_format.sendMessage(this.server.getConsoleSender(), placeholders);
                } else { // console sender
                    Messages.staff_chat_format.sendMessage(sender, placeholders);
                }

                for (final Player target : this.server.getOnlinePlayers()) {
                    final UUID id = target.getUniqueId();

                    if (id.toString().equals(asString)) continue;

                    if (!Permissions.staff_chat.hasPermission(target)) continue;

                    Messages.staff_chat_format.sendMessage(target, placeholders);
                }
            });

            return;
        }

        if (!context.isPlayer()) {
            Messages.must_be_player.sendMessage(sender);

            return;
        }

        final Player player = context.getPlayer();

        this.userRegistry.getUser(uuid).ifPresentOrElse(user -> {
            final boolean isStaffChat = user.hasUserState(UserState.staff_chat);

            if (isStaffChat) {
                user.removeUserState(UserState.staff_chat);
            } else {
                user.addUserState(UserState.staff_chat);
            }

            final Messages message = isStaffChat ? Messages.staff_chat_disabled : Messages.staff_chat_enabled;

            message.sendMessage(player);
        }, () -> Messages.staff_chat_cannot_enable.sendMessage(player));
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