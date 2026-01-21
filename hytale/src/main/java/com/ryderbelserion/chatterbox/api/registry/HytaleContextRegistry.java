package com.ryderbelserion.chatterbox.api.registry;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public class HytaleContextRegistry implements IContextRegistry<PlayerRef> {

    @Override
    public UUID getUUID(@NonNull final PlayerRef player) {
        return player.getUuid();
    }
}