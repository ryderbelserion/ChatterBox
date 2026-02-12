package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.FusionVelocity;
import com.ryderbelserion.fusion.commands.context.VelocityCommandContext;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.mojang.AbstractCommand;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;

public abstract class VelocityCommand extends AbstractCommand<VelocityCommand, CommandSource, VelocityCommandContext> {

    private final FusionVelocity fusion = (FusionVelocity) FusionProvider.getInstance();

    public @NotNull final BrigadierCommand getBrigadierCommand() {
        return new BrigadierCommand(literal());
    }
}