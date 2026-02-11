package com.ryderbelserion.chatterbox.common.configs;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.apache.logging.log4j.spi.StandardLevel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

public class FilterConfig {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FusionKyori fusion = this.plugin.getFusion();

    private StandardLevel level = StandardLevel.INFO;
    private final List<String> messages;
    private final boolean isEnabled;
    private final boolean useRegex;

    public FilterConfig(@NotNull final BasicConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.messages = StringUtils.getStringList(configuration.node("blocked"), List.of());
        this.useRegex = configuration.node("use-regex").getBoolean(false);

        try {
            this.level = configuration.node("minimum-level").get(StandardLevel.class, StandardLevel.INFO);
        } catch (final SerializationException exception) {
            this.fusion.log(Level.WARNING, "Failed to fetch minimum log level from server.json, Defaulting to INFO level.");
        }
    }

    public @NotNull final List<String> getMessages() {
        return this.messages;
    }

    public @NotNull final StandardLevel getLevel() {
        return this.level;
    }

    public final boolean isUseRegex() {
        return this.useRegex;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }
}