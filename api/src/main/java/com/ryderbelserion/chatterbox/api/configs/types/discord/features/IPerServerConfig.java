package com.ryderbelserion.chatterbox.api.configs.types.discord.features;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.List;
import java.util.Map;

@NullMarked
public interface IPerServerConfig<EV, G, E> {

    <S> void sendMessage(
            final S sender,
            final G guild,
            final EV environment,
            final Map<String, String> placeholders
    );

    default <S> void sendMessage(
            final S sender,
            final G guild,
            final EV environment
    ) {
        sendMessage(sender, guild, environment, Map.of());
    }

    <S> E buildEmbed(
            final S sender,
            final CommentedConfigurationNode configuration,
            final Map<String, String> placeholders
    );

    List<String> getChannels();

    String getOfflineText();

    String getOnlineText();

    String getServer();
}