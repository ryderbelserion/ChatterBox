package com.ryderbelserion.chatterbox.api.user;

import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.chatterbox.api.adapters.IGroupAdapter;
import com.ryderbelserion.chatterbox.api.enums.user.UserState;
import com.ryderbelserion.chatterbox.api.storage.IStorageHolder;
import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NullMarked
public abstract class IUser {

    protected final ChatterBox provider = ChatterBoxProvider.getInstance();

    protected final IStorageHolder storage = this.provider.getStorageHolder();

    protected final List<UserState> states = new ArrayList<>();

    protected FusionKey locale = ChatterBox.default_locale;
    protected String creationDate = "";
    protected String timezone = "";

    public abstract IGroupAdapter getGroupAdapter();

    public abstract FusionKey getLocaleKey();

    public abstract String getUsername();

    public abstract UUID getUniqueId();

    public void addUserState(final UserState state) {
        this.states.add(state);
    }

    public void removeUserState(final UserState state) {
        this.states.remove(state);
    }

    public boolean hasUserState(final UserState state) {
        return this.states.contains(state);
    }

    public void setCreationDate(final long date) {
        this.creationDate = Instant.ofEpochSecond(date).atZone(getTimezone()).toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public final ZoneId getTimezone() {
        return this.timezone.isBlank() || this.timezone.equalsIgnoreCase("N/A") ? ZoneId.systemDefault() : ZoneId.of(this.timezone);
    }

    public void init() {
        if (this.storage.hasUser(this.getUniqueId())) {
            return;
        }

        this.storage.insertUser(this);
    }

    @ApiStatus.Internal
    public IUser setLocale(final String locale) {
        final String[] splitter = locale.contains("-") ? locale.split("-") : locale.split("_");

        final String language = splitter[0];
        final String country = splitter[1];

        final String value = "%s_%s.yml".formatted(language, country).toLowerCase();

        if (!value.equalsIgnoreCase("en_us.yml")) {
            this.locale = FusionKey.key(ChatterBox.namespace, value);
        }

        return this;
    }

    public String getLocale() {
        return getLocaleKey().asString();
    }
}