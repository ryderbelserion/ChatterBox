package com.ryderbelserion.chatterbox.api;

import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.SenderAdapter;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxPlatform extends ChatterBoxPlugin<CommandSender, Component> {

    //private HytaleMessageRegistry messageRegistry;
    //private UserManager userManager;

    public ChatterBoxPlatform(@NotNull final FusionPaper fusion) {
        super(fusion);
    }

    @Override
    public IMessageRegistry getMessageRegistry() {
        return null;
    }

    @Override
    public IContextRegistry getContextRegistry() {
        return null;
    }

    @Override
    public IUserRegistry getUserRegistry() {
        return null;
    }

    @Override
    public SenderAdapter getSenderAdapter() {
        return null;
    }

    @Override
    public void init() {
        super.init();

        //this.messageRegistry = new HytaleMessageRegistry();
        //this.messageRegistry.init();

        //this.userManager = new UserManager();
        //this.userManager.init();
    }

    /*@Override
    public @NotNull final HytaleMessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }

    @Override
    public @NotNull final UserManager getUserManager() {
        return this.userManager;
    }*/

    /*
    @Override
    public void sendMessage(@NonNull final CommandSender sender, @NotNull final String component, @NotNull final Map<String, String> placeholders) {
        sender.sendMessage(getComponent(sender, component, placeholders));
    }


    @Override
    public Component getComponent(@NonNull final CommandSender sender, @NotNull final String component, @NotNull final Map<String, String> placeholders) {
        return this.fusion.asComponent(sender, component, placeholders);
    }*/
}