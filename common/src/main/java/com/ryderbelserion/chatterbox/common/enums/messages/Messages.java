package com.ryderbelserion.chatterbox.common.enums.messages;

import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.YamlMessageAdapter;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;
import java.util.Map;
import static com.ryderbelserion.chatterbox.api.ChatterBox.namespace;

public enum Messages {

    reload_plugin("messages.reload-plugin", "{prefix}<yellow>You have reloaded the plugin!", "messages", "reload-plugin"),
    no_permission("messages.player.no-permission", "{prefix}<red>You do not have permission to use that command!", "messages", "player", "no-permission"),

    must_be_player("messages.player.requirements.must-be-player", "{prefix}<red>You must be a player to use this command.", "messages", "player", "requirements", "must-be-player"),
    must_be_console_sender("messages.player.requirements.must-be-console-sender", "{prefix}<red>You must be using console to use this command.", "messages", "player", "requirements", "must-be-console-sender"),

    target_not_online("messages.player.target-not-online", "{prefix}<red>{player} <gray>is not online.", "messages", "player", "target-not-online"),
    target_same_player("messages.player.target-same-player", "{prefix}<red>You cannot use this command on yourself.", "messages", "player", "target-same-player"),

    sender_format("messages.msg.sender-format", "<bold><red>(!)</red> <white>[</white><yellow>You</yellow></bold> <light_purple>-></light_purple> <yellow>{player}</yellow><bold><white>]</white></bold> <green>{message}", "messages", "msg", "sender-format"),
    receiver_format("messages.msg.receiver-format", "<bold><red>(!)</red> <white>[</white></bold><yellow>{player} <light_purple>-></light_purple> You</yellow><bold><white>]</white></bold> <green>{message}", "messages", "msg", "receiver-format"),

    cannot_msg_yourself("messages.msg.cannot-message-yourself", "{prefix}<red>You cannot message yourself!", "messages", "msg", "cannot-message-yourself"),
    msg_cannot_be_blank("messages.msg.cannot-be-blank", "{prefix}<red>You cannot send a blank message!", "messages", "msg", "cannot-be-blank"),

    broadcast_format("messages.broadcast.format", " <red>[<dark_red>Alert</dark_red>]</red> {message}", "messages", "broadcast", "format"),

    staff_chat_enabled("messages.staff.chat_enabled", "{prefix}<green>You have enabled Staff Chat!", "messages", "staff", "chat_enabled"),
    staff_chat_disabled("messages.staff.chat_disabled", "{prefix}<red>You have disabled Staff Chat!", "messages", "staff", "chat_disabled"),
    staff_chat_format("messages.staff.chat_format", "<yellow>[<green>StaffChat</green>]</yellow> <green>{player}</green> <gray>></gray> <blue>{message}</blue>", "messages", "staff", "chat_format"),
    staff_chat_cannot_enable("messages.staff.chat_cannot_enable", "{prefix}<red>You cannot enable Staff Chat!", "messages", "staff", "chat_cannot_enable"),

    server_name_blank("messages.hub.server-name-blank", "{prefix}<red>Server name cannot be blank!", List.of(Platform.VELOCITY), "messages", "hub", "server-name-blank"),
    server_doesnt_exist("messages.hub.server-doesnt-exist", "{prefix}<red>Server does not exist in velocity.toml, or is not registered/online.", List.of(Platform.VELOCITY), "messages", "hub", "server-doesnt-exist"),
    server_transfer_success("messages.hub.server-transfer-successful", "{prefix}<green>Successfully forwarded you to {server}.", List.of(Platform.VELOCITY), "messages", "hub", "server-transfer-successful"),
    server_transfer_failed("messages.hub.server-transfer-failed", "{prefix}<red>Failed to forward you to {server}.", List.of(Platform.VELOCITY), "messages", "hub", "server-transfer-failed"),
    server_already_there("messages.hub.server-already-there", "{prefix}<red>You are already on that server.", List.of(Platform.VELOCITY), "messages", "hub", "server-already-there"),

    inventory_not_empty("messages.player.inventory.not.empty", "{prefix}<red>Inventory is not empty, Please clear up some room.", List.of(Platform.MINECRAFT, Platform.HYTALE), "messages", "player", "inventory-not-empty"),

    server_muted_broadcast("messages.mute.chat.server-muted-broadcast", "{prefix}{player} <red>has muted the chat, You cannot speak!", List.of(Platform.MINECRAFT, Platform.HYTALE), "messages", "mute", "chat", "server-muted-broadcast"),
    server_unmuted_broadcast("messages.mute.chat.server-unmuted-broadcast", "{prefix}{player} <red>has unmuted the chat, You can now speak!", List.of(Platform.MINECRAFT, Platform.HYTALE), "messages", "mute", "chat", "server-unmuted-broadcast"),
    server_muted_sender("messages.mute.chat.server-muted-sender", "{prefix}<red>You have muted the server chat!", List.of(Platform.MINECRAFT, Platform.HYTALE), "messages", "mute", "chat", "server-muted-sender"),
    server_unmuted_sender("messages.mute.chat.server-unmuted-sender", "{prefix}<red>You have unmuted the server chat!", List.of(Platform.MINECRAFT, Platform.HYTALE), "messages", "mute", "chat", "server-unmuted-sender"),

    cannot_speak_while_muted("messages.mute.chat.cannot-speak-while-muted", "{prefix}<red>You cannot speak while the server chat is muted!", List.of(Platform.MINECRAFT, Platform.HYTALE), "messages", "mute", "chat", "cannot-speak-while-muted"),

    message_of_the_day("messages.motd", List.of(
            "<gray>------------------------------------",
            "",
            "<green>Welcome to the server <blue>{player}</blue>!",
            "",
            "<green>If you need any help, Please message online staff!",
            "",
            "<green>You can change this message in the messages.yml or the locale folder.",
            "<gray>------------------------------------"
    ), List.of(Platform.MINECRAFT, Platform.HYTALE), "messages", "motd");

    private final ChatterBoxPlugin plugin = (ChatterBoxPlugin) ChatterBoxProvider.getInstance();

    private final ISenderAdapter senderAdapter = this.plugin.getSenderAdapter();

    private final List<Platform> platforms;
    private final String defaultValue;
    private final Object[] path;
    private final FusionKey id;

    Messages(final String id, final String defaultValue, final List<Platform> platforms, final Object... path) {
        this.defaultValue = defaultValue;
        this.id = FusionKey.key(namespace, id);
        this.platforms = platforms;
        this.path = path;
    }

    Messages(final String id, final List<String> defaultValue, final List<Platform> platforms, final Object... path) {
        this(id, StringUtils.toString(defaultValue), platforms, path);
    }

    Messages(final String id, final String defaultValue, final Object... path) {
        this(id, defaultValue, List.of(), path);
    }

    Messages(final String id, final List<String> defaultValue, final Object... path) {
        this(id, StringUtils.toString(defaultValue), path);
    }

    public void addVelocityKey(final MessageRegistry registry, final CommentedConfigurationNode configuration, final FusionKey id) {
        if (!this.platforms.contains(Platform.VELOCITY)) return;

        final YamlMessageAdapter adapter = new YamlMessageAdapter(configuration, this.defaultValue, this.path);

        registry.addKey(
                id,
                this.id,
                adapter
        );
    }

    public void addMinecraftKey(final MessageRegistry registry, final CommentedConfigurationNode configuration, final FusionKey id) {
        if (this.platforms.contains(Platform.VELOCITY)) return;

        final YamlMessageAdapter adapter = new YamlMessageAdapter(configuration, this.defaultValue, this.path);

        registry.addKey(
                id,
                this.id,
                adapter
        );
    }

    public void addKey(final MessageRegistry registry, final CommentedConfigurationNode configuration, final FusionKey id) {
        if (!this.platforms.isEmpty()) return;

        final YamlMessageAdapter adapter = new YamlMessageAdapter(configuration, this.defaultValue, this.path);

        registry.addKey(
                id,
                this.id,
                adapter
        );
    }

    public List<String> toList(final String value) {
        return value.lines().toList();
    }

    public void sendMessage(final Audience audience, final Map<String, String> placeholders) {
        this.senderAdapter.sendMessage(audience, this.id, placeholders);
    }

    public void sendMessage(final Audience audience, final String placeholder, final String value) {
        sendMessage(audience, Map.of(placeholder, value));
    }

    public void sendMessage(final Audience audience) {
        sendMessage(audience, Map.of());
    }

    public String getMessage(final Audience audience, final Map<String, String> placeholders) {
        return this.senderAdapter.getMessage(audience, this.id, placeholders);
    }

    public String getMessage(final Audience audience, final String placeholder, final String value) {
        return getMessage(audience, Map.of(placeholder, value));
    }

    public String getMessage(final Audience audience) {
        return this.senderAdapter.getMessage(audience, this.id);
    }

    public FusionKey getKey() {
        return this.id;
    }
}