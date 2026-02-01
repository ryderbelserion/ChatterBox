package com.ryderbelserion.discord.api.embeds;

import com.ryderbelserion.discord.api.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Consumer;

public class Embed {

    private final EmbedBuilder builder = new EmbedBuilder();
    private final EmbedField fields = new EmbedField(this.builder);

    /**
     * Sets the title of the embed.
     *
     * @param title the text in the title.
     * @return the embed class with updated information.
     */
    public Embed title(@NotNull final String title) {
        this.builder.setTitle(title);

        return this;
    }

    /**
     * Sets the footer using text/icon
     *
     * @param text the text in the footer.
     * @param icon the icon in the footer.
     * @return the embed class with updated information.
     */
    public Embed footer(@NotNull final String text, @Nullable final String icon) {
        if (icon == null) {
            this.builder.setFooter(text);

            return this;
        }

        this.builder.setFooter(text, icon);

        return this;
    }

    /**
     * Sets the footer using text/icon
     *
     * @param text the text in the footer.
     * @return the embed class with updated information.
     */
    public Embed footer(@NotNull final String text) {
        return this.footer(text, null);
    }

    /**
     * Sets the footer using the user object.
     *
     * @param user the user in the footer.
     * @return the embed class with updated information.
     */
    public Embed footer(@NotNull final User user) {
        return footer("Requested by: %s".formatted(user.getAsMention()), user.getEffectiveAvatarUrl());
    }

    /**
     * Set the footer using the user object.
     *
     * @param user - The member in question.
     */
    public Embed footer(@NotNull final User user, @NotNull final Guild guild) {
        final Member member = guild.getMemberById(user.getId());

        return footer("Requested by: %s".formatted(user.getAsMention()), member == null ? user.getEffectiveAvatarUrl() : member.getEffectiveAvatarUrl());
    }

    /**
     * Sets the description of the embed.
     *
     * @param text the text to use.
     * @return the embed class with updated information.
     */
    public Embed description(@NotNull final String text) {
        this.builder.setDescription(text);

        return this;
    }

    /**
     * Set the thumbnail using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    public Embed thumbnail(@NotNull final User user, @NotNull final Guild guild) {
        final Member member = guild.getMemberById(user.getId());

        return thumbnail(member == null ? user.getEffectiveAvatarUrl() : member.getEffectiveAvatarUrl());
    }

    /**
     * Sets the thumbnail using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    public Embed thumbnail(@NotNull final User user) {
        return thumbnail(user.getEffectiveAvatarUrl());
    }

    /**
     * Sets the thumbnail using a url.
     *
     * @param url the url to use.
     * @return the embed class with updated information.
     */
    public Embed thumbnail(@NotNull final String url) {
        if (url.isBlank()) return this;

        this.builder.setThumbnail(url);

        return this;
    }

    /**
     * Set the image using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    public Embed image(@NotNull final User user, @NotNull final Guild guild) {
        final Member member = guild.getMemberById(user.getId());

        return image(member == null ? user.getEffectiveAvatarUrl() : member.getEffectiveAvatarUrl());
    }

    /**
     * Sets the embed image using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    public Embed image(@NotNull final User user) {
        this.builder.setImage(user.getEffectiveAvatarUrl());

        return this;
    }

    /**
     * Sets the embed image using a url.
     *
     * @param url the url to use.
     * @return the embed class with updated information.
     */
    public Embed image(@NotNull final String url) {
        if (url.isBlank()) return this;

        this.builder.setImage(url);

        return this;
    }

    /**
     * Sets the author using name/url
     *
     * @param name the name to use.
     * @param url the url to use.
     * @return the embed class with updated information.
     */
    public Embed author(@NotNull final String name, @NotNull final String url) {
        this.builder.setAuthor(name, null, url);

        return this;
    }

    /**
     * Set the author using the user object.
     *
     * @param user - The member in question.
     * @param guild - Used to fetch the member's guild avatar otherwise fetches global avatar.
     */
    public Embed author(@NotNull final User user, @NotNull final Guild guild) {
        final Member member = guild.getMemberById(user.getId());

        final String avatar = member == null ? user.getEffectiveAvatarUrl() : member.getEffectiveAvatarUrl();

        return author(user.getEffectiveName(), avatar);
    }

    /**
     * Sets the author using a user object.
     *
     * @param user the user to use.
     * @return the embed class with updated information.
     */
    public Embed author(@NotNull final User user) {
        return author(user.getEffectiveName(), user.getEffectiveAvatarUrl());
    }

    /**
     * Sets the color of the embed.
     *
     * @param color the color to use.
     * @return the embed class with updated information.
     */
    public Embed color(@NotNull final String color)  {
        this.builder.setColor(ColorUtils.toColor(color));

        return this;
    }

    /**
     * Set a color using one of our pre-set colors.
     *
     * @param color - A preset enum of colors.
     */
    public Embed color(@NotNull final EmbedColor color) {
        this.builder.setColor(color.getColor());

        return this;
    }

    /**
     * Sets the timezone in the embed.
     *
     * @param timezone the timezone to use for embeds.
     * @return the embed class with updated information.
     */
    public Embed timestamp(@NotNull final String timezone) {
        this.builder.setTimestamp(LocalDateTime.now().atZone(ZoneId.of(timezone)));

        return this;
    }

    public Embed timestamp() {
        return timestamp("America/New_York");
    }

    /**
     * Add multiple fields to the embed.
     *
     * @param fields the list of fields to add.
     * @return the embed class with updated information.
     */
    public Embed fields(@NotNull final Consumer<EmbedField> fields) {
        fields.accept(this.fields);

        return this;
    }

    /**
     * @return the built embed.
     */
    public @NotNull final MessageEmbed build() {
        return this.builder.build();
    }
}