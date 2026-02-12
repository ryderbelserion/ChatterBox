package com.ryderbelserion.chatterbox.velocity.api.registry;

import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class VelocityContextRegistry implements IContextRegistry<Player> {

    @Override
    public @NotNull final UUID getUUID(@NotNull final Player player) {
        return player.getUniqueId();
    }
}