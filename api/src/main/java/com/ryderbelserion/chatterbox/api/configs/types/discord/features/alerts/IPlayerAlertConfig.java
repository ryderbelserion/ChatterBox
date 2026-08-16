package com.ryderbelserion.chatterbox.api.configs.types.discord.features.alerts;

import com.ryderbelserion.chatterbox.api.enums.discord.PlayerAlert;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;

@NullMarked
public interface IPlayerAlertConfig<M, G, E> {

    void sendMinecraft(
            final M member,
            final String id,
            final String message,
            final PlayerAlert alert,
            final Map<String, String> placeholders
    );

    default void sendMinecraft(
            final M member,
            final String id,
            final String message,
            final PlayerAlert alert
    ) {
        sendMinecraft(member, id, message, alert, Map.of());
    }

    <S> void sendDiscord(
            final S sender,
            final G guild,
            final PlayerAlert alert,
            final Map<String, String> placeholders
    );

    default <S> void sendDiscord(
            final S sender,
            final G guild,
            final PlayerAlert alert
    ) {
        sendDiscord(sender, guild, alert, Map.of());
    }

    <S> E buildEmbed(
            final S sender,
            final CommentedConfigurationNode configuration,
            final Map<String, String> placeholders
    );
}