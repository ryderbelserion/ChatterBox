package com.ryderbelserion.chatterbox.velocity.api.registry.adapters;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.enums.FileKeys;
import com.ryderbelserion.chatterbox.velocity.api.ChatterBoxVelocity;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityMessageRegistry;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityUserRegistry;
import com.ryderbelserion.fusion.velocity.FusionVelocity;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VelocitySenderAdapter extends ISenderAdapter<ChatterBoxVelocity, Component, CommandSource> {

    private final VelocityMessageRegistry messageRegistry;
    private final VelocityUserRegistry userRegistry;
    private final FusionVelocity fusion;

    public VelocitySenderAdapter(@NotNull final ChatterBoxVelocity platform) {
        super();

        this.messageRegistry = platform.getMessageRegistry();
        this.userRegistry = platform.getUserRegistry();

        this.fusion = (FusionVelocity) platform.getFusion();
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
    public void sendMessage(@NotNull final CommandSource sender, @NotNull final Key id, @NotNull final Map<String, String> placeholders) {
        sender.sendMessage(getComponent(sender, id, placeholders));
    }

    @Override
    public Component getComponent(@NotNull final CommandSource sender, @NotNull final Key id, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        final String prefix = configuration.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        if (!(sender instanceof Player player)) {
            return this.fusion.asComponent(sender, this.messageRegistry.getMessage(id).getValue(), map);
        }

        final Optional<VelocityUserAdapter> optional = this.userRegistry.getUser(player.getUniqueId());

        if (optional.isEmpty()) return this.fusion.asComponent(player, this.messageRegistry.getMessage(id).getValue(), map);

        final VelocityUserAdapter user = optional.get();

        return this.fusion.asComponent(player, this.messageRegistry.getMessageByLocale(user.getLocaleKey(), id).getValue(), map);
    }

    @Override
    public boolean isConsole(@NotNull final CommandSource sender) {
        return sender instanceof ConsoleCommandSource;
    }
}