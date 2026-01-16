package com.ryderbelserion.chatterbox.api.messages;

import com.ryderbelserion.chatterbox.api.AbstractKey;
import com.ryderbelserion.chatterbox.api.messages.objects.IMessage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface IMessageRegistry<M extends IMessage> {

    void addMessage(@NotNull final AbstractKey locale, @NotNull final AbstractKey key, @NotNull final M message);

    void removeMessage(@NotNull final AbstractKey key);

    M getMessageByLocale(@NotNull final AbstractKey locale, @NotNull final AbstractKey key);

    M getMessage(@NotNull final AbstractKey key);

    @ApiStatus.Internal
    void init();

}