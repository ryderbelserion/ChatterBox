package com.ryderbelserion.chatterbox.users.objects;

import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.AbstractChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.users.objects.IUser;
import com.ryderbelserion.chatterbox.common.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.common.messages.objects.Message;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class User implements IUser<Message, IMessageReceiver> {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final MessageRegistry registry = this.instance.getMessageRegistry();

    private final IMessageReceiver receiver;
    private final String username;
    private final UUID uuid;

    private Key locale = Messages.default_locale;

    public User(@NotNull final IMessageReceiver receiver, @NotNull final UUID uuid, @NotNull final String username) {
        this.receiver = receiver;
        this.username = username;
        this.uuid = uuid;
    }

    @ApiStatus.Internal
    public void setLocale(@NotNull final String locale) {
        final String[] splitter = locale.split("-");

        final String language = splitter[0];
        final String country = splitter[1];

        final String value = "%s_%s.yml".formatted(language, country).toLowerCase();

        if (!value.equalsIgnoreCase("en_us.yml")) {
            this.locale = Key.key(AbstractChatterBox.namespace, value);
        }
    }

    @Override
    public void sendMessage(@NotNull final Key key, @NotNull final Map<String, String> placeholders) {
        getComponent(key).send(this.receiver, placeholders);
    }

    @Override
    public Message getComponent(@NotNull final Key key) {
        return this.registry.getMessageByLocale(this.locale, key);
    }

    @Override
    public @NotNull final String getUsername() {
        return this.username;
    }

    @Override
    public @NotNull final String getLocale() {
        return this.locale.asString();
    }

    @Override
    public @NotNull final UUID getUuid() {
        return this.uuid;
    }

    @Override
    public @NotNull final IMessageReceiver getUser() {
        return this.receiver;
    }
}