package com.ryderbelserion.chatterbox.common.api.objects;

import com.ryderbelserion.chatterbox.api.adapters.IMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class MessageAdapter implements IMessageAdapter {

    private final String value;

    public MessageAdapter(@NotNull final CommentedConfigurationNode configuration, @NotNull final String defaultValue, @NotNull final Object... path) {
        final CommentedConfigurationNode root = configuration.node(path);

        this.value = root.isList() ? StringUtils.toString(StringUtils.getStringList(root, defaultValue)) : root.getString(defaultValue); // store pre-fetch the value from the default Messages.yml
    }

    @Override
    public @NotNull final String getValue() {
        return value;
    }
}