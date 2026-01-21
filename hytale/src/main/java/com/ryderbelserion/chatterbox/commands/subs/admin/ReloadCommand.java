package com.ryderbelserion.chatterbox.commands.subs.admin;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.ryderbelserion.chatterbox.ChatterBox;
import com.ryderbelserion.chatterbox.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.constants.Messages;
import com.ryderbelserion.chatterbox.api.registry.adapters.HytaleSenderAdapter;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends CommandBase {

    private final ChatterBox instance = ChatterBox.getInstance();

    private final ChatterBoxPlatform platform = this.instance.getPlugin();

    private final HytaleSenderAdapter adapter = this.platform.getSenderAdapter();

    public ReloadCommand() {
        super("reload", "Reloads the plugin", false);

        requirePermission("chatterbox.command.reload");
    }

    @Override
    protected void executeSync(@NotNull final CommandContext context) {
        this.adapter.sendMessage(context.sender(), Messages.reload_plugin);

        this.platform.reload();
    }
}