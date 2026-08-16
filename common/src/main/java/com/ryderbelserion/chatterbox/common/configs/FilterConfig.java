package com.ryderbelserion.chatterbox.common.configs;

import com.ryderbelserion.chatterbox.api.configs.types.IFilterConfig;
import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.apache.logging.log4j.spi.StandardLevel;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.List;

@NullMarked
public final class FilterConfig implements IFilterConfig<StandardLevel> {

    private final FusionApi fusion = FusionProvider.api();

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
            this.fusion.log(Level.warn, "Failed to fetch minimum log level from server.json, Defaulting to INFO level.");
        }
    }

    @Override
    public List<String> getMessages() {
        return this.messages;
    }

    @Override
    public StandardLevel getLevel() {
        return this.level;
    }

    @Override
    public boolean isUseRegex() {
        return this.useRegex;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}