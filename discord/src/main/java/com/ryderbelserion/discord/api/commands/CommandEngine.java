package com.ryderbelserion.discord.api.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class CommandEngine extends ListenerAdapter {

    private final String name;
    private final String description;

    public CommandEngine(@NotNull final String name,
                         @NotNull final String description) {
        this.name = name;
        this.description = description;
    }

    protected abstract void perform(@NotNull final CommandContext context);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        final CommandContext context = new CommandContext(event);

        if (!event.getName().equalsIgnoreCase(this.name)) return;

        final User user = context.getAuthor();

        if (user.isBot()) return; // don't let bots interact with the command

        perform(context);
    }

    public @NotNull final String getName() {
        return this.name;
    }

    public @NotNull final String getDescription() {
        return this.description;
    }
}