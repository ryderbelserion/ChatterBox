package com.ryderbelserion.chatterbox.paper.listeners.chat.renderers;

import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRender implements ChatRenderer {

    private final Component renderedMessage;

    public ChatRender(@NotNull final FusionPaper fusion, @NotNull final Player player, @NotNull final String format, @NotNull final SignedMessage message, @NotNull final Map<String, String> placeholders) {
        final String value = fusion.papi(player, format); // parse papi separately

        final Map<String, String> map = new HashMap<>(placeholders);

        map.put("{player}", player.getName());
        map.put("{message}", message.message());

        final List<TagResolver> resolvers = new ArrayList<>();

        if (player.hasPermission("chatterbox.color")) {
            resolvers.add(StandardTags.color());
        }

        if (player.hasPermission("chatterbox.gradient")) {
            resolvers.add(StandardTags.gradient());
        }

        if (player.hasPermission("chatterbox.rainbow")) {
            resolvers.add(StandardTags.rainbow());
        }

        if (player.hasPermission("chatterbox.font")) {
            resolvers.add(StandardTags.font());
        }

        if (player.hasPermission("chatterbox.decoration")) {
            resolvers.add(StandardTags.decorations());
        } else {
            if (player.hasPermission("chatterbox.decoration.bold")) {
                resolvers.add(StandardTags.decorations(TextDecoration.BOLD));
            }
            
            if (player.hasPermission("chatterbox.decoration.italic")) {
                resolvers.add(StandardTags.decorations(TextDecoration.ITALIC));
            }
            
            if (player.hasPermission("chatterbox.decoration.underlined")) {
                resolvers.add(StandardTags.decorations(TextDecoration.UNDERLINED));
            }
            
            if (player.hasPermission("chatterbox.decoration.strikethrough")) {
                resolvers.add(StandardTags.decorations(TextDecoration.STRIKETHROUGH));
            }
            
            if (player.hasPermission("chatterbox.decoration.obfuscated")) {
                resolvers.add(StandardTags.decorations(TextDecoration.OBFUSCATED));
            }
        }

        this.renderedMessage = fusion.asComponent(value, map, resolvers, false);
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