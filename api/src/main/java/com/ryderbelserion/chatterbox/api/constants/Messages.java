package com.ryderbelserion.chatterbox.api.constants;

import com.ryderbelserion.chatterbox.api.AbstractKey;
import static com.ryderbelserion.chatterbox.api.AbstractChatterBox.namespace;

public class Messages {

    public static final AbstractKey default_locale = AbstractKey.key(namespace, "default");

    public static final AbstractKey reload_plugin = AbstractKey.key(namespace, "reload_plugin");
    public static final AbstractKey no_permission = AbstractKey.key(namespace, "no_permission");

    public static final AbstractKey message_of_the_day = AbstractKey.key(namespace, "message_of_the_day");

}