package com.ryderbelserion.chatterbox.hytale.api.registry;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.hytale.api.registry.adapters.HytaleUserAdapter;
import org.jspecify.annotations.NullMarked;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class HytaleUserRegistry implements IUserRegistry<PlayerRef> {

    private final Map<UUID, HytaleUserAdapter> users = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.users.put(ChatterBoxPlugin.CONSOLE_UUID, new HytaleUserAdapter());
    }

    @Override
    public HytaleUserAdapter addUser(final PlayerRef player) {
        final HytaleUserAdapter user = new HytaleUserAdapter(player);

        user.setLocale(player.getLanguage()).init();

        this.users.putIfAbsent(player.getUuid(), user);

        return user;
    }

    @Override
    public HytaleUserAdapter removeUser(final UUID uuid) {
        return this.users.remove(uuid);
    }

    @Override
    public Optional<HytaleUserAdapter> getUser(final UUID uuid) {
        return Optional.ofNullable(this.users.get(uuid));
    }

    @Override
    public HytaleUserAdapter getConsole() {
        return this.users.get(ChatterBoxPlugin.CONSOLE_UUID);
    }
}