package com.ryderbelserion.chatterbox.paper.listeners.chat.renderers;

import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class ChatRender implements ChatRenderer {

    private final Component renderedMessage;

    public ChatRender(@NotNull final FusionPaper fusion, @NotNull final Player player, @NotNull final String format, @NotNull final SignedMessage message, @NotNull final Map<String, String> placeholders) {
        final String value = fusion.papi(player, format); // parse papi separately

        final Map<String, String> map = new HashMap<>(placeholders);

        map.put("{player}", player.getName());
        map.put("{message}", message.message());

        this.renderedMessage = fusion.asComponent(value, map);
    }

    @Override
    public @NotNull Component render(
            @NotNull final Player player,
            @NotNull final Component displayName,
            @NotNull final Component message,
            @NotNull final Audience viewer
    ) {
        return this.renderedMessage;
    }
}