package com.ryderbelserion.chatterbox.velocity.api.registry.adapters;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.velocity.api.ChatterBoxVelocity;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityUserRegistry;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.velocity.FusionVelocity;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class VelocitySenderAdapter extends ISenderAdapter<Component, CommandSource> {

    private final VelocityUserRegistry userRegistry;
    private final MessageRegistry messageRegistry;
    private final FusionVelocity fusion;
    private final ProxyServer server;

    public VelocitySenderAdapter(@NotNull final ChatterBoxVelocity platform) {
        super();

        this.userRegistry = platform.getUserRegistry();

        this.fusion = (FusionVelocity) platform.getFusion();
        this.messageRegistry = this.fusion.getMessageRegistry();
        this.server = platform.getServer();
    }

    @Override
    public UUID getUniqueId(@NotNull final CommandSource sender) {
        if (sender instanceof Player player) {
            return player.getUniqueId();
        }

        return ChatterBoxPlugin.CONSOLE_UUID;
    }

    @Override
    public String getName(@NotNull final CommandSource sender) {
        if (sender instanceof Player player) {
            return player.getUsername();
        }

        return ChatterBoxPlugin.CONSOLE_NAME;
    }

    @Override
    public void sendMessage(@NotNull final CommandSource sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Component component = getComponent(sender, id, placeholders);

        if (component.equals(Component.empty())) {
            return;
        }

        sender.sendMessage(component);
    }

    @Override
    public void broadcast(@NonNull final CommandSource sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Component component = getComponent(sender, id, placeholders);

        if (component.equals(Component.empty())) {
            return;
        }

        this.server.sendMessage(component);
    }

    @Override
    public Component getComponent(@NotNull final CommandSource sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        final String prefix = configuration.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        final AtomicReference<String> reference = new AtomicReference<>("");

        if (!(sender instanceof Player player)) {
            this.messageRegistry.getMessage(id).ifPresent(value -> reference.set(value.getValue()));

            return this.fusion.asComponent(sender, reference.get(), map);
        }

        final Optional<VelocityUserAdapter> optional = this.userRegistry.getUser(player.getUniqueId());

        if (optional.isEmpty()) {
            this.messageRegistry.getMessage(id).ifPresent(value -> reference.set(value.getValue()));

            return this.fusion.asComponent(player, reference.get(), map);
        }

        final VelocityUserAdapter user = optional.get();

        this.messageRegistry.getMessageByLocale(user.getLocaleKey(), id).ifPresent(value -> reference.set(value.getValue()));

        return this.fusion.asComponent(player, reference.get(), map);
    }

    @Override
    public String getMessage(@NotNull final CommandSource sender, @NotNull final FusionKey id, @NotNull final Map<String, String> placeholders) {
        final List<String> values = new ArrayList<>();

        this.messageRegistry.getMessage(id).ifPresent(value -> values.add(value.getValue()));

        if (values.isEmpty()) {
            return "";
        }

        final Map<String, String> map = new HashMap<>(placeholders);

        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        final String prefix = configuration.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        return this.fusion.replacePlaceholders(this.fusion.papi(sender, values.getFirst()), map);
    }

    @Override
    public boolean isConsole(@NotNull final CommandSource sender) {
        return sender instanceof ConsoleCommandSource;
    }
}