package com.rydderbelserion.chatterbox.common.messages;

import com.rydderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.AbstractChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.messages.IMessageRegistry;
import com.rydderbelserion.chatterbox.common.messages.objects.Message;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRegistry<S> implements IMessageRegistry<Message<S>> {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Path path = this.plugin.getDataPath();

    private final Map<Key, Map<Key, Message<S>>> messages = new HashMap<>();

    @Override
    public void addMessage(@NotNull final Key locale, @NotNull final Key key, @NotNull final Message<S> message) {
        this.fusion.log(Level.INFO, "Registering the message @ %s for %s".formatted( locale.asString(), key.asString()));

        final Map<Key, Message<S>> keys = this.messages.getOrDefault(locale, new HashMap<>());

        keys.put(key, message);

        this.messages.put(locale, keys);
    }

    @Override
    public void removeMessage(@NotNull final Key key) {
        this.messages.remove(key);
    }

    @Override
    public Message<S> getMessageByLocale(@NotNull final Key locale, @NotNull final Key key) {
        return this.messages.getOrDefault(locale, this.messages.get(Messages.default_locale)).get(key);
    }

    @Override
    public Message<S> getMessage(@NotNull final Key key) {
        return this.messages.get(Messages.default_locale).get(key);
    }

    @Override
    public void init() {
        this.messages.clear();

        final List<Path> paths = this.fileManager.getFilesByPath(this.path.resolve("locale"), ".yml", 1);

        paths.add(this.path.resolve("messages.yml")); // add to list

        for (final Path path : paths) {
            this.fileManager.getYamlFile(path).ifPresentOrElse(file -> {
                final String fileName = file.getFileName();

                final Key key = Key.key(AbstractChatterBox.namespace, fileName.equalsIgnoreCase("messages.yml") ? "default" : fileName.toLowerCase());

                final CommentedConfigurationNode configuration = file.getConfiguration();

                addMessage(key, Messages.reload_plugin, new Message<S>(configuration, "{prefix}<yellow>You have reloaded the plugin!", "messages", "reload-plugin"));

                addMessage(key, Messages.no_permission, new Message<S>(configuration, "{prefix}<red>You do not have permission to use that command!", "messages", "player", "no-permission"));

                addMessage(key, Messages.message_of_the_day, new Message<S>(configuration, StringUtils.toString(List.of(
                        "<gray>------------------------------------",
                        "",
                        "<green>Welcome to the server <blue>{player}</blue>!",
                        "",
                        "<green>If you need any help, Please message online staff!",
                        "",
                        "<green>You can change this message in the messages.yml or the locale folder.",
                        "<gray>------------------------------------"
                )), "messages", "motd"));
            }, () -> this.fusion.log(Level.INFO, "Path %s not found in cache.".formatted(path)));
        }
    }
}