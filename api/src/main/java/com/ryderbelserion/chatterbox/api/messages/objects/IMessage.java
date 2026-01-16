package com.ryderbelserion.chatterbox.api.messages.objects;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public interface IMessage<P, S> {

    void sendPlayer(@NotNull final P player, @NotNull final Map<String, String> placeholders);

    default void sendPlayer(@NotNull final P player) {
        sendPlayer(player, new HashMap<>());
    }

    void send(@NotNull final S player, @NotNull final Map<String, String> placeholders);

    default void send(@NotNull final S player) {
        send(player, new HashMap<>());
    }
}