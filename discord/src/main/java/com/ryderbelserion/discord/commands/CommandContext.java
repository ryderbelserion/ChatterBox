package com.ryderbelserion.discord.commands;

import com.ryderbelserion.discord.commands.interfaces.CommandActor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CommandContext implements CommandActor {

    private final SlashCommandInteractionEvent event;

    public CommandContext(@NotNull final SlashCommandInteractionEvent event) {
        this.event = event;
    }

    @Override
    public final void reply(@NotNull final String message, final boolean ephemeral) {
        this.event.reply(message).setEphemeral(ephemeral).queue();
    }

    @Override
    public final void reply(@NotNull final MessageEmbed embed, final boolean ephemeral) {
        this.event.replyEmbeds(embed).setEphemeral(ephemeral).queue();
    }

    @Override
    public @NotNull final CommandContext defer(final boolean ephemeral) {
        this.event.deferReply(ephemeral).queue();

        return this;
    }

    @Override
    public @Nullable final OptionMapping getOption(@NotNull final String option) {
        return this.event.getOption(option);
    }

    @Override
    public @NotNull final User getAuthor() {
        return this.event.getUser();
    }

    @Override
    public final boolean isCreator(@NotNull final String id) {
        return id.equalsIgnoreCase(getCreator().getId());
    }

    @Override
    public @NotNull final User getCreator() {
        return Objects.requireNonNull(getJDA().getUserById("209853986646261762"));
    }

    @Override
    public @NotNull final SelfUser getBot() {
        return getJDA().getSelfUser();
    }

    @Override
    public @Nullable final Guild getGuild() {
        return this.event.getGuild();
    }

    @Override
    public @NotNull final JDA getJDA() {
        return this.event.getJDA();
    }
}