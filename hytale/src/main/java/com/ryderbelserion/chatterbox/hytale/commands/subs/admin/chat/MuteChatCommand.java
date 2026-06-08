package com.ryderbelserion.chatterbox.hytale.commands.subs.admin.chat;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.Universe;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.server.ServerState;
import com.ryderbelserion.chatterbox.common.api.adapters.ServerAdapter;
import com.ryderbelserion.chatterbox.hytale.ChatterBox;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleSenderAdapter;
import org.jetbrains.annotations.NotNull;

public class MuteChatCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxHytale platform = this.instance.getPlatform();

    private final ServerAdapter serverAdapter = this.platform.getServerAdapter();

    private final HytaleSenderAdapter adapter = this.platform.getSenderAdapter();

    public MuteChatCommand() {
        super("mutechat", "Mutes the server chat!", false);

        requirePermission("chatterbox.command.mutechat");
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        final CommandSender sender = context.sender();

        final boolean isMuted = this.serverAdapter.hasState(ServerState.chat_muted);

        if (isMuted) {
            this.serverAdapter.removeState(ServerState.chat_muted);
        } else {
            this.serverAdapter.addState(ServerState.chat_muted);
        }

        Universe.get().sendMessage(this.adapter.getComponent(sender, isMuted ? Messages.server_unmuted_broadcast : Messages.server_muted_broadcast));

        this.adapter.sendMessage(sender, isMuted ? Messages.server_muted_sender : Messages.server_unmuted_sender);
    }
}