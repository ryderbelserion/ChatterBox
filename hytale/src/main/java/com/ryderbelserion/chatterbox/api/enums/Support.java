package com.ryderbelserion.chatterbox.api.enums;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public enum Support {

    luckperms("LuckPerms:LuckPerms");

    private final String name;

    Support(@NotNull final String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        final PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString(this.name));

        return plugin != null && plugin.isEnabled();
    }
}
