package com.ryderbelserion.chatterbox.api.constants;

import com.ryderbelserion.fusion.core.api.FusionKey;
import static com.ryderbelserion.chatterbox.api.ChatterBox.namespace;

public class Messages {

    public static final FusionKey default_locale = FusionKey.key(namespace, "default");

    public static final FusionKey reload_plugin = FusionKey.key(namespace, "reload_plugin");
    public static final FusionKey no_permission = FusionKey.key(namespace, "no_permission");

    public static final FusionKey must_be_player = FusionKey.key(namespace, "must_be_player");

    public static final FusionKey must_be_console_sender =  FusionKey.key(namespace, "must_be_console_sender");

    public static final FusionKey target_not_online = FusionKey.key(namespace, "target_not_online");

    public static final FusionKey target_same_player = FusionKey.key(namespace, "target_same_player");

    public static final FusionKey inventory_not_empty = FusionKey.key(namespace, "inventory_not_empty");

    public static final FusionKey message_of_the_day = FusionKey.key(namespace, "message_of_the_day");

    // /cb mutechat
    public static final FusionKey server_muted_broadcast = FusionKey.key(namespace, "server_muted_broadcast");

    public static final FusionKey server_unmuted_broadcast = FusionKey.key(namespace, "server_unmuted_broadcast");

    public static final FusionKey server_muted_sender = FusionKey.key(namespace, "server_muted_sender");

    public static final FusionKey server_unmuted_sender = FusionKey.key(namespace, "server_unmuted_sender");

    public static final FusionKey cannot_speak_while_muted = FusionKey.key(namespace, "cannot_speak_while_muted");

    // /cb staffchat
    public static final FusionKey staff_chat_enabled = FusionKey.key(namespace, "staff_chat_enabled");

    public static final FusionKey staff_chat_disabled = FusionKey.key(namespace, "staff_chat_disabled");

    public static final FusionKey staff_chat_format = FusionKey.key(namespace, "staff_chat_format");

    public static final FusionKey staff_chat_cannot_enable = FusionKey.key(namespace, "staff_chat_cannot_enable");

    // /cb msg
    public static final FusionKey receiver_format = FusionKey.key(namespace, "receiver_format");
    public static final FusionKey sender_format = FusionKey.key(namespace, "sender_format");

    public static final FusionKey cannot_msg_yourself = FusionKey.key(namespace, "cannot_msg_yourself");

    public static final FusionKey msg_cannot_be_blank = FusionKey.key(namespace, "msg_cannot_be_blank");

    // velocity only messages
    public static final FusionKey server_name_blank = FusionKey.key(namespace, "server_name_blank");
    public static final FusionKey server_doesnt_exist = FusionKey.key(namespace, "server_doesnt_exist");
    public static final FusionKey server_transfer_success = FusionKey.key(namespace, "server_transfer_success");
    public static final FusionKey server_transfer_failed = FusionKey.key(namespace, "server_transfer_failed");
    public static final FusionKey server_already_there = FusionKey.key(namespace, "server_already_there");
}