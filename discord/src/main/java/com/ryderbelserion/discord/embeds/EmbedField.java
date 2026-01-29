package com.ryderbelserion.discord.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class EmbedField {

    private final EmbedBuilder builder;

    public EmbedField(@NotNull final EmbedBuilder builder) {
        this.builder = builder;
    }

    public void field(@NotNull final String title, @NotNull final String body, final boolean inline) {
        this.builder.addField(title, body, inline);
    }

    public void field(@NotNull final MessageEmbed.Field field, final boolean inline) {
        final String name = field.getName();
        final String value = field.getValue();

        field(name != null ? name : "", value != null ? value : "", inline);
    }

    public void field(@NotNull final String title, @NotNull final String body) {
        field(title, body, false);
    }

    public void empty(final boolean inline) {
        this.builder.addBlankField(inline);
    }
}