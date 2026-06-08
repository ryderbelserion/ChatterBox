package com.ryderbelserion.chatterbox.api.enums;

import com.ryderbelserion.chatterbox.api.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxProvider;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import org.jspecify.annotations.NullMarked;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public enum Permissions {

    staff_chat(PermissionType.OP, "command.staffchat", "Allows you to talk to other staff privately!"),
    mute_chat(PermissionType.OP, "command.mutechat", "Mutes the server chat!"),

    broadcast(PermissionType.OP, "command.broadcast", "Broadcasts a message to the server!"),

    reload(PermissionType.OP, "command.reload", "Reloads the plugin!"),

    motd(PermissionType.OP, "command.motd", "Shows the message of the day!"),

    hub(PermissionType.OP, "command.hub", "Sends you to the hub server!"),

    use(PermissionType.OP, "command.use", "Allows use of /cb|/cbv"),

    // features
    mute_chat_bypass(PermissionType.OP, "mutechat.bypass", "Allows you to bypass muted server chats!", true),

    // color resolvers
    standard_colors(PermissionType.TRUE, "color", "Allows you to use standard colors like <red>", true),

    gradient_colors(PermissionType.TRUE, "gradient", "Allows you to use gradient colors using <gradient>", true),

    rainbow_colors(PermissionType.OP, "rainbow", "Allows you to use rainbow colors by doing <rainbow>", true),

    decoration_font(PermissionType.OP, "font", "Allows you to use <font> in chat", true),

    decoration_wildcard(PermissionType.TRUE, "decoration", "Gives access to all decoration tags!", true),

    decoration_strikethrough(PermissionType.FALSE, "decoration.strikethrough", "Gives access to <strikethrough>", true),
    decoration_obfuscated(PermissionType.FALSE, "decoration.obfuscated", "Gives access to <obfuscated>", true),
    decoration_underlined(PermissionType.FALSE, "decoration.underlined", "Gives access to <underlined>", true),
    decoration_italic(PermissionType.FALSE, "decoration.italic", "Gives access to <italic>", true),
    decoration_bold(PermissionType.FALSE, "decoration.bold", "Gives access to <bold>", true);

    private final PermissionType permissionType;
    private final Map<String, Boolean> children;
    private final String permissionNode;
    private final String permissionDesc;
    private final boolean isRegister;

    Permissions(
            final PermissionType permissionType,
            final String permissionNode,
            final String permissionDesc,
            final Map<String, Boolean> children,

            final boolean isRegister
    ) {
        this.permissionType = permissionType;
        this.permissionNode = permissionNode;
        this.permissionDesc = permissionDesc;
        this.children = children;

        this.isRegister = isRegister;
    }

    Permissions(
            final PermissionType permissionType,
            final String permissionNode,
            final String permissionDesc,

            final boolean isRegister
    ) {
        this(permissionType, permissionNode, permissionDesc, new HashMap<>(), isRegister);
    }

    Permissions(
            final PermissionType permissionType,
            final String permissionNode,
            final String permissionDesc
    ) {
        this(permissionType, permissionNode, permissionDesc, new HashMap<>(), false);
    }

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final ChatterBox plugin = ChatterBoxProvider.getInstance();

    public final boolean hasPermission(final Object object) {
        return this.plugin.hasPermission(getPermissionNode(), object);
    }

    public final PermissionType getPermissionType() {
        return this.permissionType;
    }

    public final Map<String, Boolean> getChildren() {
        return this.children;
    }

    public final String getPermissionDesc() {
        return this.permissionDesc;
    }

    public final String getPermissionNode() {
        return "chatterbox.%s".formatted(this.permissionNode);
    }

    public final PermissionContext getContext() {
        final PermissionContext context = new PermissionContext(
                getPermissionNode(),
                getPermissionDesc(),
                switch (getPermissionType()) {
                    case TRUE -> PermissionType.TRUE;
                    case FALSE -> PermissionType.FALSE;
                    case OP -> PermissionType.OP;
                    case NOT_OP -> PermissionType.NOT_OP;
                }
        );

        getChildren().forEach(context::addPermission);

        return context;
    }

    public void registerPermission() {
        if (!this.isRegister) return;

        this.fusion.registerPermission(getContext());
    }
}