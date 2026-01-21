package com.ryderbelserion.chatterbox.api.user;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public interface IUser {

    @NotNull UUID getUniqueId();

    @NotNull String getUsername();

    @NotNull Key getLocaleKey();

    @ApiStatus.Internal
    void setLocale(@NotNull final String locale);

    default @NotNull String getLocale() {
        return getLocaleKey().asString();
    }
}