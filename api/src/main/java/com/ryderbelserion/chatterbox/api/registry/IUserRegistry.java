package com.ryderbelserion.chatterbox.api.registry;

import com.ryderbelserion.chatterbox.api.user.IUser;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;
import java.util.UUID;

public interface IUserRegistry<S> {

    void init();

    void addUser(@NotNull final S player);

    IUser removeUser(@NotNull final UUID uuid);

    Optional<? extends IUser> getUser(@NotNull final UUID uuid);

    IUser getConsole();

}