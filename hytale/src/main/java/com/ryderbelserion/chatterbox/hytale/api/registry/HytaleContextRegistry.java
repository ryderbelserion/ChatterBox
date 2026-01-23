package com.ryderbelserion.chatterbox.hytale.api.registry;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class HytaleContextRegistry implements IContextRegistry<PlayerRef> {

    @Override
    public @NotNull final UUID getUUID(@NotNull final PlayerRef player) {
        return player.getUuid();
    }
}