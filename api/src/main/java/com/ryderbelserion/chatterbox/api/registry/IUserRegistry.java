package com.ryderbelserion.chatterbox.api.registry;

import com.ryderbelserion.chatterbox.api.user.IUser;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface IUserRegistry<S> {

    void init();

    IUser addUser(final S player);

    IUser removeUser(final UUID uuid);

    Optional<? extends IUser> getUser(final UUID uuid);

    IUser getConsole();

}