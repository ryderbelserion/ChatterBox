package com.ryderbelserion.chatterbox.messages.objects;

import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.messages.objects.IMessage;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.ryderbelserion.chatterbox.api.utils.StringUtils;
import com.ryderbelserion.fusion.files.FileException;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message implements IMessage<IMessageReceiver> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform platform = this.instance.getPlugin();

    private final CommentedConfigurationNode customFile = Configs.config.getYamlConfig();
    private final String defaultValue;
    private final String value;

    public Message(@NotNull final CommentedConfigurationNode configuration, @NotNull final String defaultValue, @NotNull final Object... path) {
        this.defaultValue = defaultValue;

        final CommentedConfigurationNode root = configuration.node(path);

        this.value = root.isList() ? StringUtils.toString(getStringList(root)) : root.getString(this.defaultValue); // store pre-fetch the value from the default Messages.yml
    }

    @Override
    public void send(@NotNull final IMessageReceiver player, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final String prefix = this.customFile.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        this.platform.sendMessage(player, this.value, map);
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