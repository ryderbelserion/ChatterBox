package com.ryderbelserion.chatterbox.api.messages;

import com.ryderbelserion.chatterbox.api.messages.objects.IMessage;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface IMessageRegistry<M extends IMessage> {

    void addMessage(@NotNull final Key locale, @NotNull final Key key, @NotNull final M message);

    void removeMessage(@NotNull final Key key);

    M getMessageByLocale(@NotNull final Key locale, @NotNull final Key key);

    M getMessage(@NotNull final Key key);

    @ApiStatus.Internal
    void init();

}