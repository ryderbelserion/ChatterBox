package com.ryderbelserion.chatterbox.velocity.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.velocity.api.ChatterCommand;
import com.ryderbelserion.fusion.velocity.commands.context.VelocityCommandContext;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends ChatterCommand {

    @Override
    public @NotNull final LiteralCommandNode<CommandSource> literal() {
        return BrigadierCommand.literalArgumentBuilder("cbv").requires(this::requirement).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "chatterbox.use",
                        "Allows you to use /cbv"
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSource context) {
        return context.hasPermission(getPermissions().getFirst().getPermission());
    }

    @Override
    public void run(@NotNull final VelocityCommandContext context) {}
}