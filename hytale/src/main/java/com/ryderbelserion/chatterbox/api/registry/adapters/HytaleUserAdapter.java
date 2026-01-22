package com.ryderbelserion.chatterbox.api.registry.adapters;

import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.user.IUser;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class HytaleUserAdapter implements IUser {

    protected PlayerRef player;

    protected Key locale = Messages.default_locale;

    public HytaleUserAdapter(@Nullable final IMessageReceiver sender) {
        if (sender instanceof PlayerRef reference) {
            this.player = reference;
        }
    }

    public HytaleUserAdapter() {
        this(null);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_UUID : this.player.getUuid();
    }

    @Override
    public @NotNull String getUsername() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_NAME : this.player.getUsername();
    }

    @Override
    public @NotNull Key getLocaleKey() {
        return this.player == null ? Messages.default_locale : this.locale;
    }

    @Override
    public void setLocale(@NotNull final String locale) {
        final String[] splitter = locale.split("-");

        final String language = splitter[0];
        final String country = splitter[1];

        final String value = "%s_%s.yml".formatted(language, country).toLowerCase();

        if (!value.equalsIgnoreCase("en_us.yml")) {
            this.locale = Key.key(ChatterBox.namespace, value);
        }
    }
}