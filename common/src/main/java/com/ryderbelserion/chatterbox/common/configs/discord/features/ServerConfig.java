package com.ryderbelserion.chatterbox.common.configs.discord.features;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.collections4.map.HashedMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

public class ServerConfig {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final CommentedConfigurationNode configuration;

    private final List<String> channels;
    private final String offlineText;
    private final String onlineText;
    private final String server;
    private final String timezone;

    public ServerConfig(@NotNull final String timezone, @NotNull final String server, @NotNull final CommentedConfigurationNode configuration) {
        this.channels = StringUtils.getStringList(configuration.node("channels"), List.of());
        this.timezone = timezone;
        this.server = server;

        this.onlineText = configuration.node("online").getString("");
        this.offlineText = configuration.node("offline").getString("");

        this.configuration = configuration;
    }

    public @NotNull final List<String> getChannels() {
        return this.channels;
    }

    public @NotNull final String getOfflineText() {
        return this.offlineText;
    }

    public @NotNull final String getOnlineText() {
        return this.onlineText;
    }

    public <S> void sendMessage(@Nullable final S sender, @NotNull final Guild guild, @NotNull final Environment environment, @NotNull final Map<String, String> placeholders) {
        if (this.channels.isEmpty()) {
            return;
        }

        final Map<String, String> copy = new HashedMap<>(placeholders);

        final ZonedDateTime time = LocalDateTime.now().atZone(ZoneId.of(this.timezone));

        copy.putIfAbsent("{timestamp}", time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));

        for (final String id : this.channels) {
            final TextChannel channel = guild.getTextChannelById(id);

            if (channel == null) {
                continue;
            }

            switch (environment) {
                case SHUTDOWN -> {
                    if (!this.offlineText.isBlank()) {
                        channel.sendMessage(this.fusion.parse(sender, this.offlineText, copy)).queue();

                        continue;
                    }

                    if (this.configuration.hasChild("embed", "offline")) {
                        final Embed embed = buildEmbed(sender, this.configuration.node("embed", "offline"), copy);

                        channel.sendMessageEmbeds(embed.build()).queue();
                    }
                }

                case INITIALIZED -> {
                    if (!this.onlineText.isBlank()) {
                        channel.sendMessage(this.fusion.parse(sender, this.onlineText, copy)).queue();

                        continue;
                    }

                    if (this.configuration.hasChild("embed", "online")) {
                        final Embed embed = buildEmbed(sender, this.configuration.node("embed", "online"), copy);

                        channel.sendMessageEmbeds(embed.build()).queue();
                    }
                }
            }
        }
    }

    public <S> void sendMessage(@Nullable final S sender, @NotNull final Guild guild, final Environment environment) {
        sendMessage(sender, guild, environment, Map.of());
    }

    public <S> Embed buildEmbed(@Nullable final S sender, @NotNull final CommentedConfigurationNode configuration, @NotNull final Map<String, String> placeholders) {
        final Embed embed = new Embed();

        embed.title(this.fusion.parse(sender, configuration.node("title").getString(""), placeholders));

        embed.color(configuration.node("color").getString("#0eeb6a"));

        if (configuration.hasChild("description")) {
            embed.description(this.fusion.parse(sender, configuration.node("description").getString(""), placeholders));
        }

        if (configuration.hasChild("footer")) {
            embed.footer(this.fusion.parse(sender, configuration.node("footer").getString("{timestamp}"), placeholders));
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

    public @NotNull final String getServer() {
        return this.server;
    }
}