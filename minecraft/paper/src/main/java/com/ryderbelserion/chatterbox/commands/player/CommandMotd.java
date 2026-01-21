package com.ryderbelserion.chatterbox.commands.player;

import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.commands.AnnotationFeature;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class CommandMotd extends AnnotationFeature {

    @Override
    public void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser) {
        parser.parse(this);
    }

    @Command("chatmanager motd")
    @CommandDescription("Shows the message of the day!")
    @Permission(value = "chatmanager.motd", mode = Permission.Mode.ALL_OF)
    public void motd(final CommandSender sender) {
        this.adapter.sendMessage(sender, Messages.message_of_the_day, Map.of(
                "{player}", sender.getName()
        ));
    }
}