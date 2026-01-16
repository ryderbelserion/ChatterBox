package com.ryderbelserion.chatterbox.api.users;

import com.ryderbelserion.chatterbox.api.users.objects.IUser;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;
import java.util.UUID;

public interface IUserManager<P, U extends IUser> {

    void init();

    void addUser(@NotNull final P player);

    Optional<U> getUser(@NotNull final UUID uuid);

}