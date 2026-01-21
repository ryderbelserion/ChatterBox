package com.ryderbelserion.chatterbox.common.api.adapters;

import com.ryderbelserion.chatterbox.api.adapters.IPlayerAdapter;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class PlayerAdapter<P> implements IPlayerAdapter<P> {

    private final com.ryderbelserion.chatterbox.api.registry.IUserRegistry<?> userRegistry;
    private final IContextRegistry<P> contextManager;

    public PlayerAdapter(@NotNull final com.ryderbelserion.chatterbox.api.registry.IUserRegistry<?> userRegistry, @NotNull final IContextRegistry<P> contextManager) {
        this.userRegistry = userRegistry;
        this.contextManager = contextManager;
    }

    @Override
    public @NotNull final Optional<? extends IUser> getUser(@NotNull final P player) {
        return this.userRegistry.getUser(this.contextManager.getUUID(player));
    }
}