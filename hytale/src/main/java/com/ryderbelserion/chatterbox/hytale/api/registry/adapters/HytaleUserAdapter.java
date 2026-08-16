package com.ryderbelserion.chatterbox.hytale.api.registry.adapters;

import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Support;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.UUID;

public final class HytaleUserAdapter extends IUser {

    private PlayerRef player;

    public HytaleUserAdapter(@Nullable final IMessageReceiver sender) {
        if (sender instanceof PlayerRef reference) {
            this.player = reference;
        }
    }

    public HytaleUserAdapter() {
        this(null);
    }

    @Override
    public @NonNull UUID getUniqueId() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_UUID : this.player.getUuid();
    }

    @Override
    public @NonNull String getUsername() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_NAME : this.player.getUsername();
    }

    @Override
    public @NonNull FusionKey getLocaleKey() {
        return this.player == null ? ChatterBox.default_locale : this.locale;
    }

    @Override
    public @NonNull GroupAdapter getGroupAdapter() {
        return this.registry.getMod(Support.luckperms_hytale).isEnabled() ? new GroupAdapter(getUniqueId()) : new GroupAdapter();
    }
}