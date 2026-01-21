package com.ryderbelserion.chatterbox.api.adapters;

import com.ryderbelserion.chatterbox.api.user.IUser;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public interface IPlayerAdapter<T> {

    @NotNull Optional<? extends IUser> getUser(@NotNull final T player);

}