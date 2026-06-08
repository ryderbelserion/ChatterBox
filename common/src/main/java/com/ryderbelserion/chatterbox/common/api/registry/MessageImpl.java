package com.ryderbelserion.chatterbox.common.api.registry;

import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.YamlMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.List;

public class MessageImpl {

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final FusionKyori fusion = this.plugin.getFusion();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Path path = this.plugin.getDataPath();

    private final MessageRegistry registry;

    public MessageImpl(@NonNull final MessageRegistry registry) {
        this.registry = registry;
    }
    
    public void reload() {
        init();
    }

    public void init() {
        this.registry.init(action -> {
            final List<Path> paths = this.fileManager.getFilesByPath(this.path.resolve("locale"), ".yml", 1);

            paths.add(this.path.resolve("messages.yml")); // add to list

            final Platform platform = this.plugin.getPlatform();

            for (final Path path : paths) {
                this.fileManager.getYamlFile(path).ifPresentOrElse(file -> {
                    final String fileName = file.getFileName();

                    final FusionKey key = FusionKey.key(ChatterBox.namespace, fileName.equalsIgnoreCase("messages.yml") ? "default" : fileName.toLowerCase());

                    final CommentedConfigurationNode configuration = file.getConfiguration();

                    action.addKey(key, Messages.reload_plugin, new YamlMessageAdapter(configuration, "{prefix}<yellow>You have reloaded the plugin!", "messages", "reload-plugin"));

                    action.addKey(key, Messages.no_permission, new YamlMessageAdapter(configuration, "{prefix}<red>You do not have permission to use that command!", "messages", "player", "no-permission"));

                    action.addKey(key, Messages.must_be_player, new YamlMessageAdapter(configuration, "{prefix}<red>You must be a player to use this command.", "messages", "player", "requirements", "must-be-player"));

                    action.addKey(key, Messages.must_be_console_sender, new YamlMessageAdapter(configuration, "{prefix}<red>You must be using console to use this command.", "messages", "player", "requirements", "must-be-console-sender"));

                    action.addKey(key, Messages.target_not_online, new YamlMessageAdapter(configuration, "{prefix}<red>{player} <gray>is not online.", "messages", "player", "target-not-online"));

                    action.addKey(key, Messages.target_same_player, new YamlMessageAdapter(configuration, "{prefix}<red>You cannot use this command on yourself.", "messages", "player", "target-same-player"));

                    action.addKey(key, Messages.sender_format, new YamlMessageAdapter(configuration, "<bold><red>(!)</red> <white>[</white><yellow>You</yellow></bold> <light_purple>-></light_purple> <yellow>{player}</yellow><bold><white>]</white></bold> <green>{message}",
                            "messages", "msg", "sender-format"));
                    action.addKey(key, Messages.receiver_format, new YamlMessageAdapter(configuration, "<bold><red>(!)</red> <white>[</white></bold><yellow>{player} <light_purple>-></light_purple> You</yellow><bold><white>]</white></bold> <green>{message}",
                            "messages", "msg", "receiver-format"));

                    action.addKey(key, Messages.cannot_msg_yourself, new YamlMessageAdapter(configuration, "{prefix}<red>You cannot message yourself!", "messages", "msg", "cannot-message-yourself"));
                    action.addKey(key, Messages.msg_cannot_be_blank, new YamlMessageAdapter(configuration, "{prefix}<red>You cannot send a blank message!", "messages", "msg", "cannot-be-blank"));

                    switch (platform) {
                        case VELOCITY -> {
                            action.addKey(key, Messages.server_name_blank, new YamlMessageAdapter(configuration, "{prefix}<red>Server name cannot be blank!", "messages", "hub", "server-name-blank"));
                            action.addKey(key, Messages.server_doesnt_exist, new YamlMessageAdapter(configuration, "{prefix}<red>Server does not exist in velocity.toml, or is not registered/online.", "messages", "hub", "server-doesnt-exist"));
                            action.addKey(key, Messages.server_transfer_success, new YamlMessageAdapter(configuration, "{prefix}<green>Successfully forwarded you to {server}.", "messages", "hub", "server-transfer-successful"));
                            action.addKey(key, Messages.server_transfer_failed, new YamlMessageAdapter(configuration, "{prefix}<red>Failed to forward you to {server}.", "messages", "hub", "server-transfer-failed"));
                            action.addKey(key, Messages.server_already_there, new YamlMessageAdapter(configuration, "{prefix}<red>You are already on that server.", "messages", "hub", "server-already-there"));
                        }

                        case MINECRAFT, HYTALE -> {
                            action.addKey(key, Messages.inventory_not_empty, new YamlMessageAdapter(configuration, "{prefix}<red>Inventory is not empty, Please clear up some room.", "messages", "player", "inventory-not-empty"));

                            action.addKey(key, Messages.server_muted_broadcast, new YamlMessageAdapter(configuration, "{prefix}{player} <red>has muted the chat, You cannot speak!", "messages", "mute", "chat", "server-muted-broadcast"));
                            action.addKey(key, Messages.server_unmuted_broadcast, new YamlMessageAdapter(configuration, "{prefix}{player} <red>has unmuted the chat, You can now speak!", "messages", "mute", "chat", "server-unmuted-broadcast"));
                            action.addKey(key, Messages.server_muted_sender, new YamlMessageAdapter(configuration, "{prefix}<red>You have muted the server chat!", "messages", "mute", "chat", "server-muted-sender"));
                            action.addKey(key, Messages.server_unmuted_sender, new YamlMessageAdapter(configuration, "{prefix}<red>You have unmuted the server chat!", "messages", "mute", "chat", "server-unmuted-sender"));

                            action.addKey(key, Messages.cannot_speak_while_muted, new YamlMessageAdapter(configuration, "{prefix}<red>You cannot speak while the server chat is muted!", "messages", "mute", "chat", "cannot-speak-while-muted"));

                            action.addKey(key, Messages.message_of_the_day, new YamlMessageAdapter(configuration, StringUtils.toString(List.of(
                                    "<gray>------------------------------------",
                                    "",
                                    "<green>Welcome to the server <blue>{player}</blue>!",
                                    "",
                                    "<green>If you need any help, Please message online staff!",
                                    "",
                                    "<green>You can change this message in the messages.yml or the locale folder.",
                                    "<gray>------------------------------------"
                            )), "messages", "motd"));
                        }
                    }
                }, () -> this.fusion.log(Level.INFO, "Path %s not found in cache.".formatted(path)));
            }
        });
    }
}