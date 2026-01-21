package com.ryderbelserion.chatterbox.commands;

import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.chatterbox.commands.admin.CommandReload;
import com.ryderbelserion.chatterbox.commands.player.CommandMotd;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.injection.ParameterInjectorRegistry;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand {

    protected final ChatterBox instance = ChatterBox.getInstance();

    protected final ChatterBoxPlatform platform = this.instance.getPlatform();

    protected final PaperSenderAdapter adapter = this.platform.getSenderAdapter();

    private final AnnotationParser<CommandSourceStack> parser;

    public BaseCommand(@NotNull final PaperCommandManager<CommandSourceStack> manager) {
        final ParameterInjectorRegistry<CommandSourceStack> injector = manager.parameterInjectorRegistry();

        injector.registerInjector(CommandSender.class, (context, accessor) -> context.sender().getSender());
        injector.registerInjector(Player.class, (context, accessor) -> {
            final CommandSender sender = context.sender().getSender();

            if (sender instanceof Player player) {
                return player;
            }

            this.adapter.sendMessage(sender, Messages.must_be_player);

            return null;
        });

        this.parser = new AnnotationParser<>(manager, CommandSourceStack.class);

        register();
    }

    private void register() { // register our universal commands
        List.of(
                new CommandReload(),

                new CommandMotd()
        ).forEach(command -> command.registerFeature(this.parser));
    }

    public @NotNull final AnnotationParser<CommandSourceStack> getParser() {
        return this.parser;
    }
}