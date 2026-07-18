package com.ryderbelserion.chatterbox.api.user;

import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.adapters.IGroupAdapter;
import com.ryderbelserion.chatterbox.api.enums.user.UserState;
import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class IUser {

    protected final List<UserState> states = new ArrayList<>();

    protected FusionKey locale = ChatterBox.default_locale;

    public @NotNull abstract UUID getUniqueId();

    public @NotNull abstract String getUsername();

    public @NotNull abstract FusionKey getLocaleKey();

    public abstract IGroupAdapter getGroupAdapter();

    public void addUserState(@NotNull final UserState state) {
        this.states.add(state);
    }

    public void removeUserState(@NotNull final UserState state) {
        this.states.remove(state);
    }

    public boolean hasUserState(@NotNull final UserState state) {
        return this.states.contains(state);
    }

    @ApiStatus.Internal
    public void setLocale(@NotNull final String locale) {
        final String[] splitter = locale.contains("-") ? locale.split("-") : locale.split("_");

        final String language = splitter[0];
        final String country = splitter[1];

        final String value = "%s_%s.yml".formatted(language, country).toLowerCase();

        if (!value.equalsIgnoreCase("en_us.yml")) {
            this.locale = FusionKey.key(ChatterBox.namespace, value);
        }
    }

    public @NotNull String getLocale() {
        return getLocaleKey().asString();
    }
}