package com.ryderbelserion.chatterbox.velocity.api;

import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityMessageRegistry;
import com.ryderbelserion.chatterbox.velocity.api.registry.VelocityUserRegistry;
import com.ryderbelserion.chatterbox.velocity.api.registry.adapters.VelocitySenderAdapter;
import com.ryderbelserion.fusion.FusionVelocity;
import com.ryderbelserion.fusion.commands.VelocityCommand;
import com.velocitypowered.api.proxy.ProxyServer;

public abstract class ChatterCommand extends VelocityCommand {

    protected final ChatterBox plugin = ChatterBox.getInstance();

    protected final ChatterBoxVelocity platform = this.plugin.getPlatform();

    protected final VelocityUserRegistry userRegistry = this.platform.getUserRegistry();

    protected final VelocityMessageRegistry messageRegistry = this.platform.getMessageRegistry();

    protected final VelocitySenderAdapter adapter = this.platform.getSenderAdapter();

    protected final FusionVelocity fusion = this.plugin.getFusion();

    protected final ProxyServer server = this.plugin.getServer();
}