package com.ryderbelserion.chatterbox.hytale.api.registry.adapters;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.hytale.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleMessageRegistry;
import com.ryderbelserion.chatterbox.hytale.api.registry.HytaleUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HytaleSenderAdapter extends ISenderAdapter<ChatterBoxPlatform, Message, IMessageReceiver> {

    private final HytaleMessageRegistry messageRegistry;
    private final HytaleUserRegistry userRegistry;
    private final FusionHytale fusion;

    public HytaleSenderAdapter(@NotNull final ChatterBoxPlatform platform) {
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
    public void sendMessage(@NotNull final IMessageReceiver sender, @NotNull final Key id, @NotNull final Map<String, String> placeholders) {
        sender.sendMessage(getComponent(sender, id, placeholders));
    }

    @Override
    public Message getComponent(@NotNull final IMessageReceiver sender, @NotNull final Key id, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        final String prefix = configuration.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        if (!(sender instanceof PlayerRef player)) {
            return this.fusion.asMessage(sender, this.messageRegistry.getMessage(id).getValue(), map);
        }

        final Optional<HytaleUserAdapter> optional = this.userRegistry.getUser(player.getUuid());

        if (optional.isEmpty()) return this.fusion.asMessage(sender, this.messageRegistry.getMessage(id).getValue(), map);

        final HytaleUserAdapter user = optional.get();

        return this.fusion.asMessage(player, this.messageRegistry.getMessageByLocale(user.getLocaleKey(), id).getValue(), map);
    }

    @Override
    public boolean isConsole(@NotNull final IMessageReceiver sender) {
        return sender instanceof ConsoleSender;
    }
}