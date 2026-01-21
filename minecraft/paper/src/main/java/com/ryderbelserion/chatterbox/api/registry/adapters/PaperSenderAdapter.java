package com.ryderbelserion.chatterbox.api.registry.adapters;

import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.registry.PaperMessageRegistry;
import com.ryderbelserion.chatterbox.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaperSenderAdapter extends ISenderAdapter<ChatterBoxPlatform, CommandSender> {

    private final PaperMessageRegistry messageRegistry;
    private final PaperUserRegistry userRegistry;
    private final FusionPaper fusion;

    public PaperSenderAdapter(@NotNull final ChatterBoxPlatform platform) {
        super();

        this.messageRegistry = platform.getMessageRegistry();
        this.userRegistry = platform.getUserRegistry();

        this.fusion = (FusionPaper) platform.getFusion();
    }

    @Override
    public UUID getUniqueId(@NotNull final CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getUniqueId();
        }

        return ChatterBoxPlugin.CONSOLE_UUID;
    }

    @Override
    public String getName(@NotNull final CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getName();
        }

        return ChatterBoxPlugin.CONSOLE_NAME;
    }

    @Override
    public void sendMessage(@NotNull final CommandSender sender, @NotNull final Key id, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final CommentedConfigurationNode configuration = Configs.config.getYamlConfig();

        final String prefix = configuration.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(this.fusion.asComponent(sender, this.messageRegistry.getMessage(id).getValue(), map));

            return;
        }

        final UUID uuid = player.getUniqueId();

        this.userRegistry.getUser(uuid).ifPresent(user -> sender.sendMessage(this.fusion.asComponent(player, this.messageRegistry.getMessageByLocale(user.getLocaleKey(), id).getValue(), map)));
    }

    @Override
    public boolean isConsole(@NotNull final CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }
}