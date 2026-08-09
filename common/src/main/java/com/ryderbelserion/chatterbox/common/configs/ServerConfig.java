package com.ryderbelserion.chatterbox.common.configs;

import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.BasicConfigurationNode;

public final class ServerConfig {

    private FilterConfig filterConfig;

    public ServerConfig init() {
        final BasicConfigurationNode config = FileKeys.server.getJsonConfig();

        this.filterConfig = new FilterConfig(config.node("settings", "log-filter"));

        return this;
    }

    public @NonNull FilterConfig getFilterConfig() {
        return this.filterConfig;
    }
}