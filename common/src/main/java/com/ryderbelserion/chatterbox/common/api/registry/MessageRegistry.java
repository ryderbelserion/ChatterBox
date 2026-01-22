package com.ryderbelserion.chatterbox.common.api.registry;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.registry.IMessageRegistry;
import com.ryderbelserion.chatterbox.common.api.objects.MessageAdapter;
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

public class MessageRegistry implements IMessageRegistry<MessageAdapter> {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FusionKyori fusion = this.plugin.getFusion();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Path path = this.plugin.getDataPath();

    private final Map<Key, Map<Key, MessageAdapter>> messages = new HashMap<>();

    @Override
    public MessageRegistry init() {
        this.messages.clear();

        final List<Path> paths = this.fileManager.getFilesByPath(this.path.resolve("locale"), ".yml", 1);

        paths.add(this.path.resolve("messages.yml")); // add to list

        for (final Path path : paths) {
            this.fileManager.getYamlFile(path).ifPresentOrElse(file -> {
                final String fileName = file.getFileName();

                final Key key = Key.key(ChatterBox.namespace, fileName.equalsIgnoreCase("messages.yml") ? "default" : fileName.toLowerCase());

                final CommentedConfigurationNode configuration = file.getConfiguration();

                addMessage(key, Messages.reload_plugin, new MessageAdapter(configuration, "{prefix}<yellow>You have reloaded the plugin!", "messages", "reload-plugin"));

                addMessage(key, Messages.no_permission, new MessageAdapter(configuration, "{prefix}<red>You do not have permission to use that command!", "messages", "player", "no-permission"));

                addMessage(key, Messages.must_be_player, new MessageAdapter(configuration, "{prefix}<red>You must be a player to use this command.", "messages", "player", "requirements", "must-be-player"));

                addMessage(key, Messages.must_be_console_sender, new MessageAdapter(configuration, "{prefix}<red>You must be using console to use this command.", "messages", "player", "requirements", "must-be-console-sender"));

                addMessage(key, Messages.target_not_online, new MessageAdapter(configuration, "{prefix}<red>{player} <gray>is not online.", "messages", "player", "target-not-online"));

                addMessage(key, Messages.target_same_player, new MessageAdapter(configuration, "{prefix}<red>You cannot use this command on yourself.", "messages", "player", "target-same-player"));

                addMessage(key, Messages.inventory_not_empty, new MessageAdapter(configuration, "{prefix}<red>Inventory is not empty, Please clear up some room.", "messages", "player", "inventory-not-empty"));

                addMessage(key, Messages.message_of_the_day, new MessageAdapter(configuration, StringUtils.toString(List.of(
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

        return this;
    }

    @Override
    public void addMessage(@NotNull final Key locale, @NotNull final Key key, @NotNull final MessageAdapter message) {
        this.fusion.log(Level.INFO, "Registering the message @ %s for %s".formatted( locale.asString(), key.asString()));

        final Map<Key, MessageAdapter> keys = this.messages.getOrDefault(locale, new HashMap<>());

        keys.put(key, message);

        this.messages.put(locale, keys);
    }

    @Override
    public void removeMessage(@NotNull final Key key) {
        this.messages.remove(key);
    }

    @Override
    public MessageAdapter getMessageByLocale(@NotNull final Key locale, @NotNull final Key key) {
        return this.messages.getOrDefault(locale, this.messages.get(Messages.default_locale)).get(key);
    }

    @Override
    public MessageAdapter getMessage(@NotNull final Key key) {
        return this.messages.get(Messages.default_locale).get(key);
    }
}