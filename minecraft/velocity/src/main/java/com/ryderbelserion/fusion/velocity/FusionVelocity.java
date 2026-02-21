package com.ryderbelserion.fusion.velocity;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import java.nio.file.Path;
import java.util.Map;

public class FusionVelocity extends FusionKyori<Audience> {

    private final Logger logger;

    public FusionVelocity(@NotNull final Logger logger, @NotNull final Path source, @NotNull final Path path) {
        super(source, path);

        this.logger = logger;
    }

    @Override
    public void log(@NotNull final Level level, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        if (!this.isVerbose()) return;

        final String safeMessage = replacePlaceholders(message, placeholders);

        switch (level) {
            case WARNING -> this.logger.warn(safeMessage);
            case ERROR -> this.logger.error(safeMessage);
            case INFO -> this.logger.info(safeMessage);
        }
    }

    @Override
    public final boolean isModReady(@NotNull final FusionKey key) {
        return false;
    }

    @Override
    public @NotNull final String papi(@Nullable final Audience sender, @NotNull final String message) {
        return message;
    }
}