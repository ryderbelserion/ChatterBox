package com.ryderbelserion.chatterbox.paper.api.registry.adapters;

import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Support;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.fusion.api.interfaces.mods.IMod;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class PaperUserAdapter extends IUser {

    private Player player;

    public PaperUserAdapter(@Nullable final CommandSender sender) {
        if (sender instanceof Player reference) {
            this.player = reference;
        }
    }

    public PaperUserAdapter() {
        this(null);
    }

    @Override
    public @NotNull final UUID getUniqueId() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_UUID : this.player.getUniqueId();
    }

    @Override
    public @NotNull final String getUsername() {
        return this.player == null ? ChatterBoxPlugin.CONSOLE_NAME : this.player.getName();
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