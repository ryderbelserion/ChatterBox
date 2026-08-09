package com.ryderbelserion.chatterbox.common.configs.discord.features;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.enums.Environment;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.collections4.map.HashedMap;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

@NullMarked
public final class ServerConfig {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final CommentedConfigurationNode configuration;

    private final List<String> channels;
    private final String offlineText;
    private final String onlineText;
    private final String server;
    private final String timezone;

    public ServerConfig(final String timezone, final String server, final CommentedConfigurationNode configuration) {
        this.channels = StringUtils.getStringList(configuration.node("channels"), List.of());
        this.timezone = timezone;
        this.server = server;

        this.onlineText = configuration.node("online").getString("");
        this.offlineText = configuration.node("offline").getString("");

        this.configuration = configuration;
    }

    public List<String> getChannels() {
        return this.channels;
    }

    public String getOfflineText() {
        return this.offlineText;
    }

    public String getOnlineText() {
        return this.onlineText;
    }

    public <S> void sendMessage(@Nullable final S sender, final Guild guild, final Environment environment, final Map<String, String> placeholders) {
        if (this.channels.isEmpty()) {
            return;
        }

        final Map<String, String> copy = new HashedMap<>(placeholders);

        final ZonedDateTime time = LocalDateTime.now().atZone(ZoneId.of(this.timezone));

        copy.putIfAbsent("{timestamp}", time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));

        MessageEmbed embed = null;
        String text = "";

        switch (environment) {
            case SHUTDOWN -> {
                if (!this.offlineText.isBlank()) {
                    text = this.fusion.parse(sender, this.offlineText, copy);
                }

                if (this.configuration.hasChild("embed", "offline")) {
                    embed = buildEmbed(sender, this.configuration.node("embed", "offline"), copy).build();
                }
            }

            case INITIALIZED -> {
                if (!this.onlineText.isBlank()) {
                    text = this.fusion.parse(sender, this.onlineText, copy);
                }

                if (this.configuration.hasChild("embed", "online")) {
                    embed = buildEmbed(sender, this.configuration.node("embed", "online"), copy).build();
                }
            }
        }

        for (final String id : this.channels) {
            final TextChannel channel = guild.getTextChannelById(id);

            if (channel == null) {
                continue;
            }

            if (!text.isBlank()) {
                channel.sendMessage(text).queue();

                continue;
            }

            if (embed != null) {
                channel.sendMessageEmbeds(embed).queue();
            }
        }
    }

    public <S> void sendMessage(@Nullable final S sender, final Guild guild, final Environment environment) {
        sendMessage(sender, guild, environment, Map.of());
    }

    public <S> Embed buildEmbed(@Nullable final S sender, final CommentedConfigurationNode configuration, final Map<String, String> placeholders) {
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

    public String getServer() {
        return this.server;
    }
}