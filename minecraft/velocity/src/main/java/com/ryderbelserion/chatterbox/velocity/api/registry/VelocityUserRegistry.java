package com.ryderbelserion.chatterbox.velocity.api.registry;

import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.velocity.api.registry.adapters.VelocityUserAdapter;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.identity.Identity;
import org.jspecify.annotations.NullMarked;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class VelocityUserRegistry implements IUserRegistry<Player> {

    private final Map<UUID, VelocityUserAdapter> users = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.users.put(ChatterBoxPlugin.CONSOLE_UUID, new VelocityUserAdapter());
    }

    @Override
    public VelocityUserAdapter addUser(final Player player) {
        final VelocityUserAdapter user = new VelocityUserAdapter(player);

        user.setLocale(player.get(Identity.LOCALE).orElse(Locale.US).toString()).init();

        this.users.putIfAbsent(player.getUniqueId(), user);

        return user;
    }

    @Override
    public VelocityUserAdapter removeUser(final UUID uuid) {
        return this.users.remove(uuid);
    }

    @Override
    public Optional<VelocityUserAdapter> getUser(UUID uuid) {
        return Optional.of(this.users.get(uuid));
    }

    @Override
    public VelocityUserAdapter getConsole() {
        return this.users.get(ChatterBoxPlugin.CONSOLE_UUID);
    }
}