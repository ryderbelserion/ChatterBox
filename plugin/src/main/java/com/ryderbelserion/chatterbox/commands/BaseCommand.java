package com.ryderbelserion.chatterbox.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.ryderbelserion.chatterbox.commands.subs.ReloadCommand;

public class BaseCommand extends AbstractCommandCollection {

    public BaseCommand() {
        super("chatterbox", "The base command for ChatterBox");

        addSubCommand(new ReloadCommand());
    }
}