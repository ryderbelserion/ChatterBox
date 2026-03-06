package com.ryderbelserion.chatterbox.paper.api;

import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperMessageRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperSenderAdapter;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.commands.PaperCommand;
import org.bukkit.Server;

public abstract class ChatterBoxCommand extends PaperCommand {

    protected final ChatterBox instance = ChatterBox.getInstance();

    protected final ChatterBoxPaper platform = this.instance.getPlatform();

    protected final PaperSenderAdapter adapter = this.platform.getSenderAdapter();

    protected final PaperMessageRegistry messageRegistry = this.platform.getMessageRegistry();

    protected final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

    protected final FusionPaper fusion =  this.instance.getFusion();

    protected final Server server = this.instance.getServer();

}