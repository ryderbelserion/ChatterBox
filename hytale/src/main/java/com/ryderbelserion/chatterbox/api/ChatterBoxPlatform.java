package com.ryderbelserion.chatterbox.api;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.rydderbelserion.chatterbox.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.utils.StringUtils;
import com.ryderbelserion.chatterbox.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;
import java.util.Map;

public class ChatterBoxPlatform extends ChatterBoxPlugin<IMessageReceiver, Message> {

    /**
     * Builds the plugin class
     *
     * @param dataPath the plugin folder
     * @param modPath  the location of the .jar file
     */
    public ChatterBoxPlatform(final Path dataPath, final Path modPath) {
        super(dataPath, modPath);
    }

    /**
     * Sends a message to the sender
     *
     * @param sender {@link IMessageReceiver}
     * @param component {@link Component}
     * @param placeholders a map of placeholders
     */
    @Override
    public void sendMessage(@NonNull final IMessageReceiver sender, @NotNull final String component, @NotNull final Map<String, String> placeholders) {
        sender.sendMessage(getComponent(sender, component, placeholders));
    }

    /**
     * Builds a component
     *
     * @param sender {@link IMessageReceiver}
     * @param component {@link Component}
     * @param placeholders a map of placeholders
     * @return {@link Message}
     */
    @Override
    public Message getComponent(@NonNull IMessageReceiver sender, @NotNull String component, @NotNull Map<String, String> placeholders) {
        return ColorUtils.toHytale(MiniMessage.miniMessage().deserialize(StringUtils.replacePlaceholders(component, placeholders)));
    }
}