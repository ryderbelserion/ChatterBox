package com.ryderbelserion.chatterbox.common.configs.discord.features.alerts;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.enums.alerts.PlayerAlert;
import com.ryderbelserion.discord.api.utils.RoleUtils;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.RoleColors;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerAlertConfig {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final Map<String, List<String>> channels = new HashMap<>();
    private final CommentedConfigurationNode configuration;
    private final String timezone;

    public PlayerAlertConfig(@NotNull final String timezone, @NotNull final CommentedConfigurationNode configuration) {
        this.channels.put("chat_alert", StringUtils.getStringList(configuration.node("chat-alert", "channels")));
        this.channels.put("join_alert", StringUtils.getStringList(configuration.node("join-alert", "channels")));
        this.channels.put("quit_alert", StringUtils.getStringList(configuration.node("quit-alert", "channels")));

        this.configuration = configuration;
        this.timezone = timezone;
    }

    public void sendMinecraft(@NotNull final Member member, @NotNull final String id, @NotNull final String message, @NotNull final PlayerAlert alert, @NotNull final Map<String, String> placeholders) {
        if (this.channels.isEmpty()) {
            return;
        }

        final boolean hasChannel = this.channels.get("chat_alert").contains(id);

        if (!hasChannel) {
            return;
        }

        final Map<String, String> copy = new HashMap<>(placeholders);

        copy.putIfAbsent("{message}", this.fusion.replacePlaceholders(message, placeholders));

        RoleUtils.getHighestRole(member).ifPresent(role -> {
            copy.putIfAbsent("{role}", role.getName());

            final RoleColors color = role.getColors();

            final Color primary = color.getPrimary();

            if (primary != null) {
                copy.putIfAbsent("{primary_color}", String.format("#%06x", primary.getRGB() & 0xFFFFFF));
            }

            if (color.isGradient()) {
                final Color secondary = color.getSecondary();

                if (secondary != null) {
                    copy.putIfAbsent("{secondary_color}", String.format("#%06x", secondary.getRGB() & 0xFFFFFF));
                }
            }
        });

        copy.putIfAbsent("{player}", member.getEffectiveName());

        switch (alert) {
            case DC_CHAT_ALERT -> { // discord->server
                final CommentedConfigurationNode configuration = this.configuration.node("chat-alert");

                if (configuration.hasChild("minecraft")) {
                    final CommentedConfigurationNode minecraft = configuration.node("minecraft");

                    if (minecraft.hasChild("message")) {
                        this.plugin.broadcast(minecraft.node("message").getString("{player} > {message}"), copy);
                    }
                }
            }
        }
    }

    public void sendMinecraft(@NotNull final Member member, @NotNull final String id, @NotNull final String message, @NotNull final PlayerAlert alert) {
        sendMinecraft(member, id, message, alert, Map.of());
    }

    public <S> void sendDiscord(@NotNull final S sender, @NotNull final Guild guild, @NotNull final PlayerAlert status, @NotNull final Map<String, String> placeholders) {
        if (this.channels.isEmpty()) {
            return;
        }

        final Map<String, String> copy = new HashMap<>(placeholders);

        final ZonedDateTime time = LocalDateTime.now().atZone(ZoneId.of(this.timezone));

        copy.putIfAbsent("{timestamp}", time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));

        List<String> channels = new ArrayList<>();
        MessageEmbed embed = null;
        String message = "";

        switch (status) {
            case MC_CHAT_ALERT -> {
                channels.addAll(this.channels.get("chat_alert"));

                final CommentedConfigurationNode configuration = this.configuration.node("chat-alert");

                if (configuration.hasChild("discord")) {
                    final CommentedConfigurationNode discord = configuration.node("discord");

                    if (discord.hasChild("message")) {
                        message = this.fusion.parse(sender, discord.node("message").getString("{player} > {message}"), placeholders);
                    }

                    if (discord.hasChild("embed")) {
                        embed = buildEmbed(sender, discord.node("embed"), copy).build();
                    }
                }
            }

            case QUIT_ALERT -> {
                channels.addAll(this.channels.get("quit_alert"));

                final CommentedConfigurationNode configuration = this.configuration.node("quit-alert");

                if (configuration.hasChild("message")) {
                    message = this.fusion.parse(sender, configuration.node("message").getString("{player} has quit!"), placeholders);
                }

                if (configuration.hasChild("embed")) {
                    embed = buildEmbed(sender, configuration.node("embed"), copy).build();
                }
            }

            case JOIN_ALERT -> {
                channels.addAll(this.channels.get("join_alert"));

                final CommentedConfigurationNode configuration = this.configuration.node("join-alert");

                if (configuration.hasChild("message")) {
                    message = this.fusion.parse(sender, configuration.node("message").getString("{player} has joined!"), placeholders);
                }

                if (configuration.hasChild("embed")) {
                    embed = buildEmbed(sender, configuration.node("embed"), copy).build();
                }
            }
        }

        for (final String id : channels) {
            final TextChannel channel = guild.getTextChannelById(id);

            if (channel == null) {
                continue;
            }

            if (!message.isBlank()) {
                channel.sendMessage(message).queue();

                return;
            }

            if (embed != null) {
                channel.sendMessageEmbeds(embed).queue();
            }
        }
    }

    public <S> void sendDiscord(@NotNull final S sender, @NotNull final Guild guild, final PlayerAlert status) {
        sendDiscord(sender, guild, status, Map.of());
    }

    public <S> Embed buildEmbed(@NotNull final S sender, @NotNull final CommentedConfigurationNode configuration, @NotNull final Map<String, String> placeholders) {
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
}