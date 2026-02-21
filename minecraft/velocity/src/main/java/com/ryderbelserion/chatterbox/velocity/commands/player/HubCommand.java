package com.ryderbelserion.chatterbox.velocity.commands.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.velocity.api.ChatterCommand;
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

public class HubCommand extends ChatterCommand {

    @Override
    public void run(@NotNull final VelocityCommandContext context) {
        final CommandSource source = context.getSource();

        if (!context.isPlayer()) {
            this.adapter.sendMessage(source, Messages.must_be_player);

            return;
        }

        final CommentedConfigurationNode config = FileKeys.config.getYamlConfig();

        final String serverName = config.node("root", "hub", "server").getString("");

        final Player player = context.getPlayer();

        if (serverName.isBlank()) {
            this.adapter.sendMessage(source, Messages.server_name_blank);

            return;
        }

        final Optional<RegisteredServer> registeredServer = this.server.getServer(serverName);

        if (registeredServer.isEmpty()) {
            this.adapter.sendMessage(source, Messages.server_doesnt_exist);

            return;
        }

        boolean isSameServer = false;

        final Optional<ServerConnection> optional = player.getCurrentServer();

        if (optional.isPresent()) {
            final ServerConnection connection = optional.get();

            final RegisteredServer server = connection.getServer();

            final String name = server.getServerInfo().getName();

            isSameServer = name.equals(serverName);
        }

        if (isSameServer) {
            this.adapter.sendMessage(source, Messages.server_already_there);

            return;
        }

        player.createConnectionRequest(registeredServer.get()).connect().whenComplete((result, throwable) -> {
            if (result.isSuccessful()) {
                this.adapter.sendMessage(source, Messages.server_transfer_success, Map.of(
                        "{server}", serverName
                ));

                return;
            }

            this.adapter.sendMessage(source, Messages.server_transfer_failed, Map.of(
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
                new PermissionContext(
                        "chatterbox.hub",
                        "Sends the player to the hub server!"
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSource context) {
        return context.hasPermission(getPermissions().getFirst().getPermission());
    }
}