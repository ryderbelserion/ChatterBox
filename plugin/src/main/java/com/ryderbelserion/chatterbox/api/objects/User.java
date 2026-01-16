package com.ryderbelserion.chatterbox.api.objects;

import com.ryderbelserion.chatterbox.api.users.objects.IUser;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class User implements IUser {

    private final String username;
    private final UUID uuid;

    private String locale;

    public User(@NotNull final UUID uuid, @NotNull final String username) {
        this.username = username;
        this.uuid = uuid;
    }

    @ApiStatus.Internal
    public void setLocale(@NotNull final String locale) {
        this.locale = locale;
    }

    @Override
    public @NotNull final String getUsername() {
        return this.username;
    }

    @Override
    public @NotNull final String getLocale() {
        return this.locale;
    }

    @Override
    public @NotNull final UUID getUuid() {
        return this.uuid;
    }
}