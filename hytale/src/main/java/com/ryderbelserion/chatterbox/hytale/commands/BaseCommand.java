package com.ryderbelserion.chatterbox.hytale.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.ryderbelserion.chatterbox.hytale.commands.subs.MotdCommand;
import com.ryderbelserion.chatterbox.hytale.commands.subs.admin.ReloadCommand;

public class BaseCommand extends AbstractCommandCollection {

    public BaseCommand() {
        super("cb", "The base command for ChatterBox");

        addAliases("chatterbox");

        addSubCommand(new ReloadCommand());

        addSubCommand(new MotdCommand());

        requirePermission("chatterbox.command.use");
    }
}