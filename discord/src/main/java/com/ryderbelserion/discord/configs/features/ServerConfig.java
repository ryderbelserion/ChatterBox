package com.ryderbelserion.discord.configs.features;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;

public class ServerConfig {

    private final List<String> channels;

    private final String server;

    private String offlineText = "The server {server} is now offline!";
    private String onlineText = "The server {server} is now online!";

    private Embed offlineEmbed = null;
    private Embed onlineEmbed = null;

    public ServerConfig(@NotNull final String server, @NotNull final CommentedConfigurationNode configuration) {
        this.channels = StringUtils.getStringList(configuration.node("channels"), List.of());
        this.server = server;

        if (configuration.hasChild("online")) {
            this.onlineText = configuration.node("online").getString("The server {server} is now online!");
        }

        if (configuration.hasChild("offline")) {
            this.offlineText = configuration.node("offline").getString("The server {server} is now offline!");
        }

        if (configuration.hasChild("offline")) {
            this.offlineEmbed = buildEmbed(configuration.node("offline"));
        }

        if (configuration.hasChild("online")) {
            this.onlineEmbed = buildEmbed(configuration.node("online"));
        }
    }

    public @NotNull final List<String> getChannels() {
        return this.channels;
    }

    public @Nullable final Embed getOfflineEmbed() {
        return this.offlineEmbed;
    }

    public @NotNull final String getOfflineText() {
        return this.offlineText;
    }

    public @Nullable final Embed getOnlineEmbed() {
        return this.onlineEmbed;
    }

    public @NotNull final String getOnlineText() {
        return this.onlineText;
    }

    public Embed buildEmbed(@NotNull final CommentedConfigurationNode configuration) {
        final Embed embed = new Embed();

        embed.title(configuration.node("title").getString("The server {server} status has changed."));
        embed.color(configuration.node("color").getString("#0eeb6a"));

        if (configuration.hasChild("footer")) {
            embed.footer(configuration.node("footer").getString("{timestamp}"));
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