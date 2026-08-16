package com.ryderbelserion.chatterbox.velocity.api.registry.adapters;

import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Support;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.fusion.api.interfaces.mods.IMod;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class VelocityUserAdapter extends IUser {

    private Player player;

    public VelocityUserAdapter(@Nullable final CommandSource sender) {
        if (sender instanceof Player reference) {
            this.player = reference;
        }
    }

    public VelocityUserAdapter() {
        this(null);
    }

    @Override
    public @NotNull final UUID getUniqueId() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_UUID : this.player.getUniqueId();
    }

    @Override
    public @NotNull final String getUsername() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_NAME : this.player.getUsername();
    }

    @Override
    public @NotNull final FusionKey getLocaleKey() {
        return this.player == null ? ChatterBox.default_locale : this.locale;
    }

    @Override
    public @NotNull final GroupAdapter getGroupAdapter() {
        final IMod mod = this.registry.getMod(Support.luckperms_minecraft);

        if (!mod.isEnabled()) {
            return new GroupAdapter();
        }

        return new GroupAdapter(getUniqueId());
    }
}