package com.ryderbelserion.chatterbox.hytale.api.registry.adapters;

import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.constants.Support;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.registry.mods.ModRegistry;
import com.ryderbelserion.fusion.hytale.FusionHytale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class HytaleUserAdapter extends IUser {

    private final FusionHytale fusion = (FusionHytale) FusionProvider.getInstance();

    private final ModRegistry registry = this.fusion.getModRegistry();

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
    public @NotNull final UUID getUniqueId() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_UUID : this.player.getUuid();
    }

    @Override
    public @NotNull final String getUsername() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_NAME : this.player.getUsername();
    }

    @Override
    public @NotNull final FusionKey getLocaleKey() {
        return this.player == null ? Messages.default_locale : this.locale;
    }

    @Override
    public @NotNull final GroupAdapter getGroupAdapter() {
        return this.registry.getMod(Support.luckperms_hytale).isEnabled() ? new GroupAdapter(getUniqueId()) : new GroupAdapter();
    }
}