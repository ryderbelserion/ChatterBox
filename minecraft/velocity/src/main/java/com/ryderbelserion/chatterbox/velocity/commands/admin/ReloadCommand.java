package com.ryderbelserion.chatterbox.velocity.commands.admin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.chatterbox.velocity.api.ChatterCommand;
import com.ryderbelserion.fusion.commands.context.VelocityCommandContext;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ReloadCommand extends ChatterCommand {

    public ReloadCommand(@NotNull final ChatterBox plugin) {
        super(plugin);
    }

    @Override
    public void run(@NotNull final VelocityCommandContext context) {
        this.plugin.getPlatform().reload();
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSource> literal() {
        return BrigadierCommand.literalArgumentBuilder("reload").requires(this::requirement)
                .executes(context -> {
                    run(new VelocityCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "chatterbox.reload",
                        "Reloads the plugin!"
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSource context) {
        return context.hasPermission(getPermissions().getFirst().getPermission());
    }
}