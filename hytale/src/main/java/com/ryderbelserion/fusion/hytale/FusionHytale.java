package com.ryderbelserion.fusion.hytale;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.hytale.utils.ColorUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FusionHytale extends FusionKyori<IMessageReceiver> {

    private final HytaleLogger logger;

    public FusionHytale(@NotNull final HytaleLogger logger, @NotNull final Path source, @NotNull final Path path) {
        super(source, path);

        this.logger = logger;
    }

    public Message asMessage(@NotNull final IMessageReceiver receiver, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        return ColorUtils.toHytale(asComponent(receiver, message, placeholders));
    }

    public Message asMessage(@NotNull final IMessageReceiver receiver, @NotNull final String message) {
        return asMessage(receiver, message, new HashMap<>());
    }

    @Override
    public String papi(@Nullable final IMessageReceiver sender, @NotNull final String message) {
        return message;
    }

    @Override
    public void log(@NotNull final Level level, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        if (!this.isVerbose()) return;

        final String safeMessage = replacePlaceholders(message, placeholders);

        switch (level) {
            case WARNING -> this.logger.atWarning().log(safeMessage);
            case ERROR -> this.logger.atSevere().log(safeMessage);
            case INFO -> this.logger.atInfo().log(safeMessage);
        }
    }

    @Override
    public boolean isModReady(@NotNull final FusionKey key) {
        final PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString(key.getValue()));

        return plugin != null && plugin.isEnabled();
    }
}