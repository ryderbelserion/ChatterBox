package com.ryderbelserion.chatterbox.velocity.api.registry;

import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.velocity.api.registry.adapters.VelocityUserAdapter;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.*;

public class VelocityUserRegistry implements IUserRegistry<Player> {

    private final Map<UUID, VelocityUserAdapter> users = new HashMap<>();

    @Override
    public void init() {
        this.users.put(ChatterBoxPlugin.CONSOLE_UUID, new VelocityUserAdapter());
    }

    @Override
    public VelocityUserAdapter addUser(@NonNull final Player player) {
        final Optional<Locale> locale = player.get(Identity.LOCALE);

        final UUID uuid = player.getUniqueId();

        final VelocityUserAdapter user = new VelocityUserAdapter(player);

        user.setLocale(locale.orElse(Locale.US).toString());

        this.users.putIfAbsent(uuid, user);

        return user;
    }

    @Override
    public VelocityUserAdapter removeUser(@NotNull final UUID uuid) {
        return this.users.remove(uuid);
    }

    @Override
    public Optional<VelocityUserAdapter> getUser(@NotNull UUID uuid) {
        return Optional.of(this.users.get(uuid));
    }

    @Override
    public @NotNull final IUser getConsole() {
        return this.users.get(ChatterBoxPlugin.CONSOLE_UUID);
    }
}