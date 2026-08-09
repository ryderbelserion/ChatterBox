package com.ryderbelserion.chatterbox.common.configs;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.apache.logging.log4j.spi.StandardLevel;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.List;

@NullMarked
public final class FilterConfig {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FusionKyori fusion = this.plugin.getFusion();

    private StandardLevel level = StandardLevel.INFO;
    private final List<String> messages;
    private final boolean isEnabled;
    private final boolean useRegex;

    public FilterConfig(final BasicConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.messages = StringUtils.getStringList(configuration.node("blocked"), List.of());
        this.useRegex = configuration.node("use-regex").getBoolean(false);

        try {
            this.level = configuration.node("minimum-level").get(StandardLevel.class, StandardLevel.INFO);
        } catch (final SerializationException exception) {
            this.fusion.log(Level.WARNING, "Failed to fetch minimum log level from server.json, Defaulting to INFO level.");
        }
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public StandardLevel getLevel() {
        return this.level;
    }

    public boolean isUseRegex() {
        return this.useRegex;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }
}