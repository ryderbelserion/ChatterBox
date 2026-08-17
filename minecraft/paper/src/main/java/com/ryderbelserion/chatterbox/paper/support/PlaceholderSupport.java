package com.ryderbelserion.chatterbox.paper.support;

import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperUserAdapter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public class PlaceholderSupport extends PlaceholderExpansion {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

    @Override
    public @NonNull final String onPlaceholderRequest(final Player player, @NonNull final String placeholder) {
        if (player == null || placeholder.isEmpty()) return "N/A";

        final Optional<PaperUserAdapter> adapter = this.userRegistry.getUser(player.getUniqueId());

        if (adapter.isEmpty()) return "";

        final PaperUserAdapter user = adapter.get();

        return switch (placeholder) {
            case "creation_date" -> user.getCreationDate();
            default -> "N/A";
        };
    }

    @Override
    public final boolean persist() {
        return true;
    }

    @Override
    public final boolean canRegister() {
        return true;
    }

    @Override
    public @NonNull final String getIdentifier() {
        return this.plugin.getName().toLowerCase();
    }

    @Override
    public @NonNull final String getVersion() {
        return this.plugin.getPluginMeta().getVersion();
    }

    @Override
    public @NonNull final String getAuthor() {
        return "ryderbelserion";
    }
}