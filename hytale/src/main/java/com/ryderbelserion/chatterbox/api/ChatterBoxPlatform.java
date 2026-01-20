package com.ryderbelserion.chatterbox.api;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.ryderbelserion.chatterbox.api.registry.HytaleMessageRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.users.UserManager;
import com.ryderbelserion.fusion.hytale.utils.ColorUtils;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.Map;

public class ChatterBoxPlatform extends ChatterBoxPlugin<IMessageReceiver, Message> {

    private HytaleMessageRegistry messageRegistry;
    private UserManager userManager;

    public ChatterBoxPlatform(@NotNull final FusionKyori<IMessageReceiver> fusion) {
        super(fusion);
    }

    @Override
    public void init() {
        super.init();

        this.messageRegistry = new HytaleMessageRegistry();
        this.messageRegistry.init();

        this.userManager = new UserManager();
        this.userManager.init();
    }

    @Override
    public @NotNull final HytaleMessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }

    @Override
    public @NotNull final UserManager getUserManager() {
        return this.userManager;
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
    public Message getComponent(@NonNull final IMessageReceiver sender, @NotNull final String component, @NotNull final Map<String, String> placeholders) {
        return ColorUtils.toHytale(this.fusion.asComponent(sender, component, placeholders));
    }
}