package com.ryderbelserion.chatterbox.commands.subs;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
import org.jetbrains.annotations.NotNull;

public class MotdCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final MessageRegistry registry = this.instance.getMessageRegistry();

    private final UserManager userManager = this.instance.getUserManager();

    public MotdCommand() {
        super("motd", "Shows the message of the day!", false);

        requirePermission("chatterbox.command.motd");
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        final CommandSender sender = context.sender();

        if (!context.isPlayer()) {
            this.registry.getMessage(Messages.message_of_the_day).send(sender);
        } else {
            this.userManager.getUser(sender.getUuid()).ifPresentOrElse(user -> user.sendMessage(Messages.message_of_the_day), () -> this.registry.getMessage(Messages.message_of_the_day).send(sender));
        }

        this.instance.reload();
    }
}