package com.ryderbelserion.chatterbox.paper.commands;

import com.ryderbelserion.chatterbox.paper.ChatterBox;
import com.ryderbelserion.chatterbox.paper.api.ChatterBoxPlatform;
import com.ryderbelserion.chatterbox.api.registry.IAnnotationRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperMessageRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.PaperUserRegistry;
import com.ryderbelserion.chatterbox.paper.api.registry.adapters.PaperSenderAdapter;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.incendo.cloud.annotations.AnnotationParser;

public abstract class AnnotationFeature implements IAnnotationRegistry<AnnotationParser<CommandSourceStack>> {

    protected final ChatterBox instance = ChatterBox.getInstance();

    protected final ChatterBoxPlatform platform = this.instance.getPlatform();

    protected final PaperSenderAdapter adapter = this.platform.getSenderAdapter();

    protected final PaperMessageRegistry messageRegistry = this.platform.getMessageRegistry();

    protected final PaperUserRegistry userRegistry = this.platform.getUserRegistry();

}