package com.rydderbelserion.chatterbox.common.messages.objects;

import com.rydderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.rydderbelserion.chatterbox.common.enums.Configs;
import com.ryderbelserion.chatterbox.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.messages.objects.IMessage;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.Map;

public class Message<S> implements IMessage<S> {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final CommentedConfigurationNode customFile = Configs.config.getYamlConfig();
    private final String value;

    public Message(@NotNull final CommentedConfigurationNode configuration, @NotNull final String defaultValue, @NotNull final Object... path) {

        final CommentedConfigurationNode root = configuration.node(path);

        this.value = root.isList() ? StringUtils.toString(StringUtils.getStringList(root, defaultValue)) : root.getString(defaultValue); // store pre-fetch the value from the default Messages.yml
    }

    @Override
    public void send(@NotNull final S sender, @NotNull final Map<String, String> placeholders) {
        final Map<String, String> map = new HashMap<>(placeholders);

        final String prefix = this.customFile.node("root", "prefix").getString(" <gold>ChatterBox <reset>");

        if (!prefix.isEmpty()) {
            map.putIfAbsent("{prefix}", prefix);
        }

        this.plugin.sendMessage(sender, this.value, map);
    }
}