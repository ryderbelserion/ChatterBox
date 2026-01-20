package com.ryderbelserion.chatterbox.commands.subs;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.enums.Support;
import com.ryderbelserion.chatterbox.common.messages.MessageRegistry;
import com.ryderbelserion.chatterbox.users.UserManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class MotdCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final MessageRegistry registry = this.instance.getMessageRegistry();

    private final UserManager userManager = this.instance.getUserManager();

    public MotdCommand() {
        super("motd", "Shows the message of the day!", false);

        requirePermission("chatterbox.command.motd");
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        final CommandSender sender = context.sender();

        final Map<String, String> placeholders = new HashMap<>();

        placeholders.put("{player}", sender.getDisplayName());

        if (sender instanceof PlayerRef player) {
            if (Support.luckperms.isEnabled()) {
                final LuckPerms luckperms = LuckPermsProvider.get();

                final User user = luckperms.getPlayerAdapter(PlayerRef.class).getUser(player);

                final CachedMetaData data = user.getCachedData().getMetaData();

                final String prefix = data.getPrefix();
                final String suffix = data.getSuffix();

                placeholders.put("{prefix}", prefix == null ? "N/A" : prefix);
                placeholders.put("{suffix}", suffix == null ? "N/A" : suffix);
            }

            this.userManager.getUser(player.getUuid()).ifPresentOrElse(user -> user.sendMessage(Messages.message_of_the_day), () -> this.registry.getMessage(Messages.message_of_the_day).send(sender));

            return;
        }

        this.registry.getMessage(Messages.message_of_the_day).send(sender, placeholders);
    }
}