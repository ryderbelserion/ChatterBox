package com.ryderbelserion.discord.commands;

import com.ryderbelserion.discord.commands.interfaces.CommandFlow;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandHandler implements CommandFlow {

    private final JDA jda;

    public CommandHandler(@NotNull final JDA jda) {
        this.jda = jda;
    }

    @Override
    public void addCommand(@NotNull final CommandEngine engine) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).queue();
    }

    @Override
    public void addCommand(@NotNull final CommandEngine engine, @NotNull final OptionData optionData) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();
    }

    @Override
    public void addCommand(@NotNull final CommandEngine engine, @NotNull final List<OptionData> optionDataList) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionDataList).queue();
    }

    @Override
    public void addCommand(@NotNull final CommandEngine engine, @NotNull final OptionType type, @NotNull final String name, @NotNull final String description) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOption(type, name, description).queue();
    }

    @Override
    public void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine) {
        guild.upsertCommand(engine.getName(), engine.getDescription()).queue();
    }

    @Override
    public void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine, @NotNull final OptionData optionData) {
        guild.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();
    }

    @Override
    public void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine, @NotNull final List<OptionData> optionDataList) {
        guild.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionDataList).queue();
    }

    @Override
    public void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine, @NotNull final OptionType type, @NotNull final String name, @NotNull final String description) {
        guild.upsertCommand(engine.getName(), engine.getDescription()).addOption(type, name, description).queue();
    }

    @Override
    public void addGuildCommands(@NotNull final Guild guild, @NotNull final CommandEngine... engines) {
        for (final CommandEngine engine : engines) {
            addGuildCommand(guild, engine);
        }
    }

    @Override
    public void addCommands(@NotNull final CommandEngine... engines) {
        for (final CommandEngine engine : engines) {
            addCommand(engine);
        }
    }

    @Override
    public void purgeGuildCommands(@NotNull final Guild guild) {
        guild.updateCommands().queue();
    }

    @Override
    public void purgeGlobalCommands() {
        this.jda.updateCommands().queue();
    }
}