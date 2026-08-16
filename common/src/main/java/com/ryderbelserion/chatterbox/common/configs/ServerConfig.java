package com.ryderbelserion.chatterbox.common.configs;

import com.ryderbelserion.chatterbox.api.configs.types.IServerConfig;
import com.ryderbelserion.chatterbox.api.enums.FileKeys;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.BasicConfigurationNode;

public final class ServerConfig implements IServerConfig {

    private FilterConfig filterConfig;

    @Override
    public @NonNull ServerConfig init() {
        final BasicConfigurationNode config = FileKeys.server.getJsonConfig();

        this.filterConfig = new FilterConfig(config.node("settings", "log-filter"));

        return this;
    }

    @Override
    public @NonNull FilterConfig getFilterConfig() {
        return this.filterConfig;
    }
}