package com.ryderbelserion.chatterbox.commands.subs;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.messages.MessageRegistry;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final MessageRegistry registry = this.instance.getMessageRegistry();

    public ReloadCommand() {
        super("reload", "Reloads the plugin", false);

        requirePermission("chatterbox.command.reload");
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        final CommandSender sender = context.sender();

        if (!sender.hasPermission("chatterbox.command.reload")) {
            this.registry.getMessage(Messages.no_permission).send(sender);

            return;
        }

        this.instance.reload();

        this.registry.getMessage(Messages.reload_plugin).send(sender);
    }
}