package com.ryderbelserion.chatterbox.paper.listeners.chat.renderers;

import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
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
        final String value = fusion.papi(player, format.replace("{message}", "<message>")); // parse papi separately

        final Map<String, String> map = new HashMap<>(placeholders);

        map.put("{player}", player.getName());

        final List<TagResolver> resolvers = new ArrayList<>();

        resolvers.add(message(player, message.message()));

        this.renderedMessage = fusion.asComponent(player, value, map, resolvers);
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

    private @NotNull TagResolver message(@NotNull final Player player, @NotNull final String message) {
        final List<TagResolver> resolvers = new ArrayList<>();

        if (Permissions.standard_colors.hasPermission(player)) {
            resolvers.add(StandardTags.color());
        }

        if (Permissions.gradient_colors.hasPermission(player)) {
            resolvers.add(StandardTags.gradient());
        }

        if (Permissions.rainbow_colors.hasPermission(player)) {
            resolvers.add(StandardTags.rainbow());
        }

        if (Permissions.decoration_font.hasPermission(player)) {
            resolvers.add(StandardTags.font());
        }

        if (Permissions.decoration_wildcard.hasPermission(player)) {
            resolvers.add(StandardTags.decorations());
        } else {
            if (Permissions.decoration_bold.hasPermission(player)) {
                resolvers.add(StandardTags.decorations(TextDecoration.BOLD));
            }

            if (Permissions.decoration_italic.hasPermission(player)) {
                resolvers.add(StandardTags.decorations(TextDecoration.ITALIC));
            }

            if (Permissions.decoration_underlined.hasPermission(player)) {
                resolvers.add(StandardTags.decorations(TextDecoration.UNDERLINED));
            }

            if (Permissions.decoration_strikethrough.hasPermission(player)) {
                resolvers.add(StandardTags.decorations(TextDecoration.STRIKETHROUGH));
            }

            if (Permissions.decoration_obfuscated.hasPermission(player)) {
                resolvers.add(StandardTags.decorations(TextDecoration.OBFUSCATED));
            }
        }

        final MiniMessage parser = MiniMessage.builder()
                .tags(TagResolver
                        .builder()
                        .resolvers(resolvers)
                        .build()
                )
                .build();

        final Component parsedMessage = parser.deserialize(message);

        return TagResolver.resolver(
                "message",
                (_, _) -> Tag.selfClosingInserting(parsedMessage)
        );
    }
}