package com.ryderbelserion.chatterbox.api.constants;

import net.kyori.adventure.key.Key;
import static com.ryderbelserion.chatterbox.api.AbstractChatterBox.namespace;

public class Messages {

    public static final Key default_locale = Key.key(namespace, "default");

    public static final Key reload_plugin = Key.key(namespace, "reload_plugin");
    public static final Key no_permission = Key.key(namespace, "no_permission");

    public static final Key message_of_the_day = Key.key(namespace, "message_of_the_day");

}