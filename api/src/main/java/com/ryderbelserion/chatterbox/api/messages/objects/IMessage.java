package com.ryderbelserion.chatterbox.api.messages.objects;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public interface IMessage<S> {

    void send(@NotNull final S player, @NotNull final Map<String, String> placeholders);

    default void send(@NotNull final S player) {
        send(player, new HashMap<>());
    }
}