package com.ryderbelserion.discord.configs.features;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.enums.Environment;
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

public class ServerConfig {

    private final FusionCore fusion = FusionProvider.getInstance();

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

    public Embed buildEmbed(@NotNull final CommentedConfigurationNode configuration, @NotNull final Environment environment, @NotNull final Map<String, String> placeholders) {
        final Embed embed = new Embed();

        embed.title(this.fusion.replacePlaceholders(configuration.node("title").getString(environment == Environment.INITIALIZED ? "The server {server} is now online." : "The server {server} is now offline."), placeholders));
        embed.color(configuration.node("color").getString("#0eeb6a"));

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

    public void sendMessage(@NotNull final JDA jda, final long guildId, @NotNull final Environment environment, @NotNull final Map<String, String> placeholders) {
        final Guild guild = jda.getGuildById(guildId);

        if (guild == null) {
            return;
        }

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
                        channel.sendMessage(this.fusion.replacePlaceholders(this.offlineText, copy)).queue();

                        continue;
                    }

                    if (this.configuration.hasChild("embed", "offline")) {
                        final Embed embed = buildEmbed(this.configuration.node("embed", "offline"), environment, copy);

                        channel.sendMessageEmbeds(embed.build()).queue();
                    }
                }

                case INITIALIZED -> {
                    if (!this.onlineText.isBlank()) {
                        channel.sendMessage(this.fusion.replacePlaceholders(this.onlineText, copy)).queue();

                        continue;
                    }

                    if (this.configuration.hasChild("embed", "online")) {
                        final Embed embed = buildEmbed(this.configuration.node("embed", "online"), environment, copy);

                        channel.sendMessageEmbeds(embed.build()).queue();
                    }
                }
            }
        }
    }

    public void sendMessage(@NotNull final JDA jda, final long guildId, final Environment environment) {
        sendMessage(jda, guildId, environment, Map.of());
    }

    public @NotNull final String getServer() {
        return this.server;
    }
}