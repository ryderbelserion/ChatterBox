package com.ryderbelserion.chatterbox.paper.api.registry;

import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperUserAdapter;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import net.kyori.adventure.identity.Identity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class PaperUserRegistry implements IUserRegistry<Player> {

    private final Map<UUID, PaperUserAdapter> users = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.users.put(ChatterBoxPlugin.CONSOLE_UUID, new PaperUserAdapter());
    }

    @Override
    public PaperUserAdapter addUser(final Player player) {
        final PaperUserAdapter user = new PaperUserAdapter(player);

        user.setLocale(player.get(Identity.LOCALE).orElse(Locale.US).toString()).init();

        this.users.putIfAbsent(player.getUniqueId(), user);

        return user;
    }

    @Override
    public PaperUserAdapter removeUser(final UUID uuid) {
        return this.users.remove(uuid);
    }

    @Override
    public Optional<PaperUserAdapter> getUser(UUID uuid) {
        return Optional.of(this.users.get(uuid));
    }

    @Override
    public PaperUserAdapter getConsole() {
        return this.users.get(ChatterBoxPlugin.CONSOLE_UUID);
    }
}