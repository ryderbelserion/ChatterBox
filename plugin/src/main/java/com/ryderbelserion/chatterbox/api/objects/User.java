package com.ryderbelserion.chatterbox.api.objects;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class User {

    private final String username;
    private final UUID uuid;

    private String locale;

    public User(@NotNull final UUID uuid, @NotNull final String username) {
        this.username = username;
        this.uuid = uuid;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public @NotNull final String getLocale() {
        return this.locale;
    }

    public @NotNull final String getUsername() {
        return this.username;
    }

    public @NotNull final UUID getUuid() {
        return this.uuid;
    }
}