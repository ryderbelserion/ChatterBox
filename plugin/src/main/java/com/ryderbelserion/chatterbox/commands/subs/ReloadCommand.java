package com.ryderbelserion.chatterbox.commands.subs;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
import com.ryderbelserion.chatterbox.users.objects.User;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final MessageRegistry registry = this.instance.getMessageRegistry();

    private final UserManager userManager = this.instance.getUserManager();

    public ReloadCommand() {
        super("reload", "Reloads the plugin", false);

        requirePermission("chatterbox.command.reload");
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        final CommandSender sender = context.sender();

        if (!context.isPlayer()) {
            this.registry.getMessage(Messages.reload_plugin).send(sender);
        } else {
            this.userManager.getUser(sender.getUuid()).ifPresentOrElse(user -> user.sendMessage(Messages.reload_plugin), () -> this.registry.getMessage(Messages.reload_plugin).send(sender));
        }

        this.instance.reload();
    }
}