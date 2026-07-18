package com.ryderbelserion.chatterbox.paper.commands.admin.chat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.api.enums.server.ServerState;
import com.ryderbelserion.chatterbox.common.enums.messages.Messages;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class MuteChatCommand extends ChatterBoxCommand {

    @Override
    public void run(@NotNull final PaperCommandContext context) {
        final CommandSender sender = context.getSender();

        final boolean isMuted = this.serverAdapter.hasState(ServerState.chat_muted);

        if (isMuted) {
            this.serverAdapter.removeState(ServerState.chat_muted);
        } else {
            this.serverAdapter.addState(ServerState.chat_muted);
        }

        this.server.broadcast(this.adapter.getComponent(sender, isMuted ? Messages.server_unmuted_broadcast.getKey() : Messages.server_muted_broadcast.getKey(), Map.of(
                "{player}", sender.getName()
        )));

        this.adapter.sendMessage(sender, isMuted ? Messages.server_unmuted_sender.getKey() : Messages.server_muted_sender.getKey());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("mutechat").requires(this::requirement)
                .executes(context -> {
                    run(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                Permissions.mute_chat.getContext()
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}