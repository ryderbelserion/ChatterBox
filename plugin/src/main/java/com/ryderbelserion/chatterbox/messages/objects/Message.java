package com.ryderbelserion.chatterbox.messages.objects;

import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.api.enums.Configs;
import com.ryderbelserion.chatterbox.api.messages.objects.IMessage;
import com.ryderbelserion.chatterbox.api.utils.StringUtils;
import com.ryderbelserion.chatterbox.utils.ColorUtils;
import com.ryderbelserion.fusion.files.FileException;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message implements IMessage<PlayerRef, CommandSender> {

    private final CommentedConfigurationNode customFile = Configs.config.getYamlConfig();
    private final String defaultValue;
    private final String value;

    public Message(@NotNull final CommentedConfigurationNode configuration, @NotNull final String defaultValue, @NotNull final Object... path) {
        this.defaultValue = defaultValue;

        final CommentedConfigurationNode root = configuration.node(path);

        this.value = root.isList() ? StringUtils.toString(getStringList(root)) : root.getString(this.defaultValue); // store pre-fetch the value from the default Messages.yml
    }

    @Override
    public void sendPlayer(@NonNull final PlayerRef player, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final String prefix = this.customFile.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        player.sendMessage(ColorUtils.parse(StringUtils.replacePlaceholders(this.value, map)));
    }

    @Override
    public void send(@NonNull final CommandSender player, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final String prefix = this.customFile.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        player.sendMessage(ColorUtils.parse(StringUtils.replacePlaceholders(this.value, map)));
    }

    private @NotNull List<String> getStringList(@NotNull final CommentedConfigurationNode node) {
        try {
            final List<String> list = node.getList(String.class);

            return list != null ? list : List.of(this.defaultValue);
        } catch (SerializationException exception) {
            throw new FileException(String.format("Failed to serialize %s!", node.path()), exception);
        }
    }
}