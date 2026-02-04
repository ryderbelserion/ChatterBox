package com.ryderbelserion.chatterbox.common.configs.discord.features.alerts;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.collections4.map.HashedMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

public class PlayerAlertConfig {

    private final FusionCore fusion = FusionProvider.getInstance();

    private final CommentedConfigurationNode configuration;
    private final List<String> channels;
    private final String timezone;

    public PlayerAlertConfig(@NotNull final String timezone, @NotNull final CommentedConfigurationNode configuration) {
        this.channels = StringUtils.getStringList(configuration.node("channels"), List.of());

        this.configuration = configuration;
        this.timezone = timezone;
    }

    public void sendMessage(@NotNull final JDA jda, final long guildId, @NotNull final PlayerAlert status, @NotNull final Map<String, String> placeholders) {
        final Guild guild = jda.getGuildById(guildId);

        if (guild == null) {
            return;
        }

        if (this.channels.isEmpty()) {
            return;
        }

        final Map<String, String> copy = new HashedMap<>(placeholders);

        final ZonedDateTime time = LocalDateTime.now().atZone(ZoneId.of(this.timezone));

        for (final String id : this.channels) {
            final TextChannel channel = guild.getTextChannelById(id);

            if (channel == null) {
                continue;
            }

            switch (status) {
                case SR_CHAT_ALERT -> { // discord->server
                    final CommentedConfigurationNode configuration = this.configuration.node("chat-alert");

                    if (configuration.hasChild("minecraft")) {
                        final CommentedConfigurationNode minecraft = configuration.node("minecraft");

                        if (minecraft.hasChild("message")) {
                            //channel.sendMessage(minecraft.node("message").getString("{player} > {message}")).queue();

                            //todo() send to server
                        }
                    }
                }

                case DC_CHAT_ALERT -> { // server->discord
                    copy.putIfAbsent("{timestamp}", time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG))); // only useful for embeds

                    final CommentedConfigurationNode configuration = this.configuration.node("chat-alert");

                    if (configuration.hasChild("discord")) {
                        final CommentedConfigurationNode discord = configuration.node("discord");

                        if (discord.hasChild("message")) {
                            channel.sendMessage(discord.node("message").getString("{player} > {message}")).queue();

                            return;
                        }

                        if (discord.hasChild("embed")) {
                            buildEmbed(discord.node("embed"), copy);
                        }
                    }
                }

                case JOIN_ALERT -> { // server->discord
                    copy.putIfAbsent("{timestamp}", time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG))); // only useful for embeds
                }

                case QUIT_ALERT -> { // server->discord
                    copy.putIfAbsent("{timestamp}", time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG))); // only useful for embeds
                }
            }
        }
    }

    public void sendMessage(@NotNull final JDA jda, final long guildId, final PlayerAlert status) {
        sendMessage(jda, guildId, status, Map.of());
    }

    public Embed buildEmbed(@NotNull final CommentedConfigurationNode configuration, @NotNull final Map<String, String> placeholders) {
        final Embed embed = new Embed();

        embed.title(this.fusion.replacePlaceholders(
                configuration.node("title").getString(""), placeholders)
        );

        embed.color(configuration.node("color").getString("#0eeb6a"));

        if (configuration.hasChild("description")) {
            embed.description(this.fusion.replacePlaceholders(configuration.node("description").getString(""), placeholders));
        }

        if (configuration.hasChild("footer")) {
            embed.footer(this.fusion.replacePlaceholders(configuration.node("footer").getString("{timestamp}"), placeholders));
        }

        if (configuration.hasChild("media")) {
            final CommentedConfigurationNode media = configuration.node("media");

            if (media.hasChild("thumbnail")) {
                embed.thumbnail(media.node("thumbnail").getString(""));
            }

            if (media.hasChild("image")) {
                embed.image(media.node("image").getString(""));
            }
        }

        return embed;
    }
}