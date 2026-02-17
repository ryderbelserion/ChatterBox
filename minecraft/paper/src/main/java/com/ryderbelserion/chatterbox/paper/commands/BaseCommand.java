package com.ryderbelserion.chatterbox.paper.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.paper.api.ChatterCommand;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends ChatterCommand {

    @Override
    public void run(@NotNull final PaperCommandContext context) {}

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("cb").requires(this::requirement).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "chatterbox.use",
                        "Allows you to use /cb"
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}