package com.ryderbelserion.chatterbox.commands.admin;

import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.commands.AnnotationFeature;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;

public class CommandReload extends AnnotationFeature {

    @Override
    public void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser) {
        parser.parse(this);
    }

    @Command("chatmanager reload")
    @CommandDescription("Reloads the plugin!")
    @Permission(value = "chatmanager.reload", mode = Permission.Mode.ALL_OF)
    public void reload(final CommandSender sender) {
        this.platform.reload();

        this.adapter.sendMessage(sender, Messages.reload_plugin);
    }
}