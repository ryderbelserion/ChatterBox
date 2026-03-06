package com.ryderbelserion.chatterbox.api.constants;

import net.kyori.adventure.key.Key;
import static com.ryderbelserion.chatterbox.api.ChatterBox.namespace;

public class Messages {

    public static final Key default_locale = Key.key(namespace, "default");

    public static final Key reload_plugin = Key.key(namespace, "reload_plugin");
    public static final Key no_permission = Key.key(namespace, "no_permission");

    public static final Key must_be_player = Key.key(namespace, "must_be_player");

    public static final Key must_be_console_sender =  Key.key(namespace, "must_be_console_sender");

    public static final Key target_not_online = Key.key(namespace, "target_not_online");

    public static final Key target_same_player = Key.key(namespace, "target_same_player");

    public static final Key inventory_not_empty = Key.key(namespace, "inventory_not_empty");

    public static final Key message_of_the_day = Key.key(namespace, "message_of_the_day");

    // social messages
    public static final Key receiver_format = Key.key(namespace, "receiver_format");
    public static final Key sender_format = Key.key(namespace, "sender_format");

    public static final Key cannot_msg_yourself = Key.key(namespace, "cannot_msg_yourself");

    public static final Key msg_cannot_be_blank = Key.key(namespace, "msg_cannot_be_blank");

    // velocity only messages
    public static final Key server_name_blank = Key.key(namespace, "server_name_blank");
    public static final Key server_doesnt_exist = Key.key(namespace, "server_doesnt_exist");
    public static final Key server_transfer_success = Key.key(namespace, "server_transfer_success");
    public static final Key server_transfer_failed = Key.key(namespace, "server_transfer_failed");
    public static final Key server_already_there = Key.key(namespace, "server_already_there");
}