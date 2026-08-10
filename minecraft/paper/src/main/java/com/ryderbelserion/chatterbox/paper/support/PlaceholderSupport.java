package com.ryderbelserion.chatterbox.paper.support;

import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPaper;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperUserAdapter;
import com.ryderbelserion.fusion.paper.FusionPaper;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;

@NullMarked
public final class PlaceholderSupport extends PlaceholderExpansion {

    private final ChatterBox plugin = ChatterBox.getInstance();

    private final FusionPaper fusion = this.plugin.getFusion();

    private final ChatterBoxPaper platform = this.plugin.getPlatform();

    private final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

    @Override
    public String onPlaceholderRequest(final Player player, final String placeholder) {
        if (placeholder.isEmpty()) return "N/A";

        final Optional<PaperUserAdapter> adapter = this.userRegistry.getUser(player.getUniqueId());

        if (adapter.isEmpty()) return "";

        final PaperUserAdapter user = adapter.get();

        return switch (placeholder) {
            case "creation_date" -> user.getCreationDate();
            default -> "N/A";
        };
    }

    @Override
    public String getIdentifier() {
        return this.plugin.getName().toLowerCase();
    }

    @Override
    public String getVersion() {
        return this.plugin.getPluginMeta().getVersion();
    }

    @Override
    public String getAuthor() {
        return "ryderbelserion";
    }
}