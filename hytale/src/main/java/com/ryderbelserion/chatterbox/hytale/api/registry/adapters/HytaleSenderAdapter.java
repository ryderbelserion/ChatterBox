package com.ryderbelserion.chatterbox.hytale.api.registry.adapters;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxHytale;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class HytaleSenderAdapter extends ISenderAdapter<Message, IMessageReceiver> {

    private final MessageRegistry messageRegistry;
    private final HytaleUserRegistry userRegistry;
    private final FusionHytale fusion;

    public HytaleSenderAdapter(@NotNull final ChatterBoxHytale platform) {
        super();

        this.messageRegistry = platform.getMessageRegistry();
        this.userRegistry = platform.getUserRegistry();

        this.fusion = (FusionHytale) platform.getFusion();
    }

    @Override
    public UUID getUniqueId(@NotNull final IMessageReceiver sender) {
        if (sender instanceof PlayerRef player) {
            return player.getUuid();
        }

        return ChatterBoxPlugin.CONSOLE_UUID;
    }

    @Override
    public String getName(@NotNull final IMessageReceiver sender) {
        if (sender instanceof PlayerRef player) {
            return player.getUsername();
        }

        return ChatterBoxPlugin.CONSOLE_NAME;
    }

    @Override
    public void sendMessage(@NotNull final IMessageReceiver sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Message component = getComponent(sender, id, placeholders);

        if (component.equals(Message.empty())) {
            return;
        }

        sender.sendMessage(component);
    }

    @Override
    public void broadcast(@NonNull final IMessageReceiver sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Message component = getComponent(sender, id, placeholders);

        if (component.equals(Message.empty())) {
            return;
        }

        Universe.get().sendMessage(component);
    }

    @Override
    public Message getComponent(@NotNull final IMessageReceiver sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        final String prefix = configuration.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        final AtomicReference<String> reference = new AtomicReference<>("");

        if (!(sender instanceof PlayerRef player)) {
            this.messageRegistry.getMessage(id).ifPresent(value -> reference.set(value.getValue()));

            return this.fusion.asMessage(sender, reference.get(), map);
        }

        final Optional<HytaleUserAdapter> optional = this.userRegistry.getUser(player.getUuid());

        if (optional.isEmpty()) {
            this.messageRegistry.getMessage(id).ifPresent(value -> reference.set(value.getValue()));

            return this.fusion.asMessage(player, reference.get(), map);
        }

        final HytaleUserAdapter user = optional.get();

        this.messageRegistry.getMessageByLocale(user.getLocaleKey(), id).ifPresent(value -> reference.set(value.getValue()));

        return this.fusion.asMessage(player, reference.get(), map);
    }

    @Override
    public boolean isConsole(@NotNull final IMessageReceiver sender) {
        return sender instanceof ConsoleSender;
    }
}