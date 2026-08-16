package com.ryderbelserion.chatterbox.common.configs.discord.features.alerts;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.configs.types.discord.features.alerts.IPlayerAlertConfig;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.chatterbox.api.enums.discord.PlayerAlert;
import com.ryderbelserion.discord.api.utils.RoleUtils;
import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.RoleColors;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jspecify.annotations.NullMarked;
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

@NullMarked
public final class PlayerAlertConfig implements IPlayerAlertConfig<Member, Guild, Embed> {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FusionApi fusion = FusionProvider.api();

    private final Map<String, List<String>> channels = new HashMap<>();
    private final CommentedConfigurationNode configuration;
    private final String timezone;

    public PlayerAlertConfig(final String timezone, final CommentedConfigurationNode configuration) {
        this.channels.put("chat_alert", StringUtils.getStringList(configuration.node("chat-alert", "channels")));
        this.channels.put("join_alert", StringUtils.getStringList(configuration.node("join-alert", "channels")));
        this.channels.put("quit_alert", StringUtils.getStringList(configuration.node("quit-alert", "channels")));

        this.configuration = configuration;
        this.timezone = timezone;
    }

    @Override
    public void sendMinecraft(final Member member, final String id, final String message, final PlayerAlert alert, final Map<String, String> placeholders) {
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

    @Override
    public <S> void sendDiscord(final S sender, final Guild guild, final PlayerAlert status, final Map<String, String> placeholders) {
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

    @Override
    public <S> Embed buildEmbed(final S sender, final CommentedConfigurationNode configuration, final Map<String, String> placeholders) {
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