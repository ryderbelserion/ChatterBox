package com.ryderbelserion.discord.commands.interfaces;

import com.ryderbelserion.discord.commands.CommandEngine;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandFlow {

    void addCommand(@NotNull final CommandEngine engine);

    void addCommand(@NotNull final CommandEngine engine, @NotNull final OptionData optionData);

    void addCommand(@NotNull final CommandEngine engine, @NotNull final List<OptionData> optionDataList);

    void addCommand(@NotNull final CommandEngine engine, @NotNull final OptionType type, @NotNull final String name, @NotNull final String description);

    void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine);

    void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine, @NotNull final OptionData optionData);

    void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine, @NotNull final List<OptionData> optionDataList);

    void addGuildCommand(@NotNull final Guild guild, @NotNull final CommandEngine engine, @NotNull final OptionType type, @NotNull final String name, @NotNull final String description);

    void addGuildCommands(@NotNull final Guild guild, @NotNull final CommandEngine... engines);

    void addCommands(@NotNull final CommandEngine... engines);

    void purgeGuildCommands(@NotNull final Guild guild);

    void purgeGlobalCommands();
}