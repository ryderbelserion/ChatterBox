package com.ryderbelserion.chatterbox.api.users.objects;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface IUser<C, U> {

    void sendMessage(@NotNull final Key key, @NotNull final Map<String, String> placeholders);

    default void sendMessage(@NotNull final Key key) {
        sendMessage(key, new HashMap<>());
    }

    @ApiStatus.Internal
    C getComponent(@NotNull final Key key);

    String getUsername();

    String getLocale();

    UUID getUuid();

}