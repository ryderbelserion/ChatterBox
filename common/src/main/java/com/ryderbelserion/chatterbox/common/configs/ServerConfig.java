package com.ryderbelserion.chatterbox.common.configs;

import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;

public class ServerConfig {

    private FilterConfig filterConfig;

    public ServerConfig init() {
        final BasicConfigurationNode config = FileKeys.server.getJsonConfig();

        this.filterConfig = new FilterConfig(config.node("settings", "log-filter"));

        return this;
    }

    public @NotNull final FilterConfig getFilterConfig() {
        return this.filterConfig;
    }
}