package com.ryderbelserion.chatterbox.api;

import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.Universe;
import com.rydderbelserion.chatterbox.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class ChatterBoxPlatform extends ChatterBoxPlugin<IMessageReceiver> {

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
     */
    @Override
    public void sendMessage(@NotNull final IMessageReceiver sender, @NotNull final Component component) {
        sender.sendMessage(ColorUtils.toHytale(component));
    }

    @Override
    public Path getServerUsersFolder() {
        return Universe.get().getPath();
    }
}