package com.ryderbelserion.chatterbox.hytale.commands.subs.admin;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleSenderAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class BroadcastCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxHytale platform = this.instance.getPlatform();

    private final HytaleSenderAdapter adapter = this.platform.getSenderAdapter();

    private final RequiredArg<String> message;

    public BroadcastCommand() {
        super("broadcast", "Broadcasts a message to the server!", false);

        requirePermission(Permissions.broadcast.getPermissionNode());

        this.message = withRequiredArg("message", "The message to broadcast to the server!", ArgTypes.GREEDY_STRING);
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        if (!this.message.provided(context)) {
            this.adapter.sendMessage(context.sender(), Messages.msg_cannot_be_blank);

            return;
        }

        final String message = this.message.get(context);

        this.adapter.broadcast(context.sender(), Messages.broadcast_format, Map.of(
                "{message}", message
        ));
    }
}