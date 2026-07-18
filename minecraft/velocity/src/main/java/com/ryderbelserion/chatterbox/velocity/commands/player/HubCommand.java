package com.ryderbelserion.chatterbox.velocity.commands.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.common.enums.messages.Messages;
import com.ryderbelserion.chatterbox.velocity.api.ChatterBoxCommand;
import com.ryderbelserion.fusion.velocity.commands.context.VelocityCommandContext;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HubCommand extends ChatterBoxCommand {

    @Override
    public void run(@NotNull final VelocityCommandContext context) {
        final CommandSource source = context.getSource();

        if (!context.isPlayer()) {
            Messages.must_be_player.sendMessage(source);

            return;
        }

        final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

        final String serverName = config.node("root", "hub", "server").getString("");

        final Player player = context.getPlayer();

        if (serverName.isBlank()) {
            Messages.server_name_blank.sendMessage(source);

            return;
        }

        final Optional<RegisteredServer> registeredServer = this.server.getServer(serverName);

        if (registeredServer.isEmpty()) {
            Messages.server_doesnt_exist.sendMessage(source);

            return;
        }

        final Optional<ServerConnection> optional = player.getCurrentServer();

        final String name = optional.isPresent() ? optional.get().getServer().getServerInfo().getName() : "";

        if (name.isBlank()) {
            Messages.server_name_blank.sendMessage(source);

            return;
        }

        if (name.equals(serverName)) {
            Messages.server_already_there.sendMessage(source);

            return;
        }

        player.createConnectionRequest(registeredServer.get()).connect().whenComplete((result, throwable) -> {
            if (result.isSuccessful()) {
                Messages.server_transfer_success.sendMessage(source, Map.of(
                        "{server}", serverName
                ));

                return;
            }

            Messages.server_transfer_failed.sendMessage(source, Map.of(
                    "{server}", serverName
            ));
        });
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSource> literal() {
        return BrigadierCommand.literalArgumentBuilder("hub").requires(this::requirement)
                .executes(context -> {
                    run(new VelocityCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                Permissions.hub.getContext(),

                new PermissionContext(
                        "chatterbox.hub",
                        "Sends you to the hub server!"
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSource context) {
        boolean hasPermission = false;

        for (final PermissionContext permissionContext : getPermissions()) {
            final String permission = permissionContext.getPermission();

            hasPermission = context.hasPermission(permission);

            if (hasPermission) {
                break;
            }
        }

        return hasPermission;
    }
}