package com.ryderbelserion.chatterbox.api.registry.adapters;

import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.constants.Support;
import com.ryderbelserion.chatterbox.api.user.IUser;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.GroupAdapter;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.objects.Mod;
import com.ryderbelserion.fusion.core.api.registry.ModRegistry;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class PaperUserAdapter implements IUser {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    private final ModRegistry registry = this.fusion.getModRegistry();

    protected Player player;

    protected Key locale;

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
    public @NotNull final Key getLocaleKey() {
        return this.player == null ? Messages.default_locale : this.locale;
    }

    @Override
    public @NotNull final GroupAdapter getGroupAdapter() {
        final Mod mod = (Mod) this.registry.getMod(Support.luckperms_minecraft);

        if (!mod.isEnabled()) {
            return new GroupAdapter();
        }

        return new GroupAdapter(getUniqueId());
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