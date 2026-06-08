package com.ryderbelserion.chatterbox.hytale.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.ryderbelserion.chatterbox.api.enums.Permissions;
import com.ryderbelserion.chatterbox.hytale.commands.subs.MotdCommand;
import com.ryderbelserion.chatterbox.hytale.commands.subs.admin.BroadcastCommand;
import com.ryderbelserion.chatterbox.hytale.commands.subs.admin.ReloadCommand;
import com.ryderbelserion.chatterbox.hytale.commands.subs.admin.chat.MuteChatCommand;

public class BaseCommand extends AbstractCommandCollection {

    public BaseCommand() {
        super("cb", "The base command for ChatterBox!");

        addAliases("chatterbox");

        addSubCommand(new BroadcastCommand());
        addSubCommand(new ReloadCommand());

        addSubCommand(new MuteChatCommand());

        addSubCommand(new MotdCommand());

        requirePermission(Permissions.use.getPermissionNode());
    }
}