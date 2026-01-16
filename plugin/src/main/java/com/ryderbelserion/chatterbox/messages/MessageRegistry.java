package com.ryderbelserion.chatterbox.messages;

import com.hypixel.hytale.logger.HytaleLogger;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.AbstractChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.messages.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.utils.StringUtils;
import com.ryderbelserion.chatterbox.messages.objects.Message;
import com.ryderbelserion.fusion.files.FileManager;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRegistry implements IMessageRegistry<Message> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final HytaleLogger logger = this.instance.getLogger();

    private final ChatterBoxPlatform plugin = this.instance.getPlugin();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Path path = this.plugin.getDataPath();

    private final Map<Key, Map<Key, Message>> messages = new HashMap<>();

    @Override
    public void addMessage(@NotNull final Key locale, @NotNull final Key key, @NotNull final Message message) {
        this.logger.atInfo().log("Registering the message @ %s for %s", locale.asString(), key.asString());

        final Map<Key, Message> keys = this.messages.getOrDefault(locale, new HashMap<>());

        keys.put(key, message);

        this.messages.put(locale, keys);
    }

    @Override
    public void removeMessage(@NotNull final Key key) {
        this.messages.remove(key);
    }

    @Override
    public Message getMessageByLocale(@NotNull final Key locale, @NotNull final Key key) {
        return this.messages.getOrDefault(locale, this.messages.get(Messages.default_locale)).get(key);
    }

    @Override
    public Message getMessage(@NotNull final Key key) {
        return this.messages.get(Messages.default_locale).get(key);
    }

    @Override
    public void init() {
        this.messages.clear();

        final List<Path> paths = this.fileManager.getFiles(this.path.resolve("locale"), ".yml", 1);

        paths.add(this.path.resolve("messages.yml")); // add to list

        for (final Path path : paths) {
            this.fileManager.getYamlFile(path).ifPresentOrElse(file -> {
                final String fileName = file.getFileName();

                final Key key = Key.key(AbstractChatterBox.namespace, fileName.equalsIgnoreCase("messages.yml") ? "default" : fileName.toLowerCase());

                final CommentedConfigurationNode configuration = file.getConfiguration();

                addMessage(key, Messages.reload_plugin, new Message(configuration, "{prefix}<yellow>You have reloaded the plugin!", "messages", "reload-plugin"));

                addMessage(key, Messages.no_permission, new Message(configuration, "{prefix}<red>You do not have permission to use that command!", "messages", "player", "no-permission"));

                addMessage(key, Messages.message_of_the_day, new Message(configuration, StringUtils.toString(List.of(
                        "<gray>------------------------------------",
                        "",
                        "<green>Welcome to the server <blue>{player}</blue>!",
                        "",
                        "<green>If you need any help, Please message online staff!",
                        "",
                        "<green>You can change this message in the messages.yml or the locale folder.",
                        "<gray>------------------------------------"
                )), "messages", "motd"));
            }, () -> this.logger.atWarning().log("Path %s not found in cache.".formatted(path)));
        }
    }
}