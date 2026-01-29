package com.ryderbelserion.chatterbox.velocity.api;

import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.velocity.discord.DiscordManager;
import com.ryderbelserion.fusion.FusionVelocity;
import com.ryderbelserion.fusion.files.enums.FileType;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxPlatform extends ChatterBoxPlugin<Audience, Object> {

    public ChatterBoxPlatform(@NotNull final FusionVelocity fusion) {
        super(fusion);
    }

    private DiscordManager discordManager;

    @Override
    public void init() {
        super.init();

        this.fileManager.addFile(this.dataPath.resolve("discord.yml"), FileType.YAML);

        post();
    }

    @Override
    public void post() {
        super.post();

        final FusionVelocity fusion = (FusionVelocity) this.fusion;

        this.discordManager = new DiscordManager(fusion);
        this.discordManager.init();
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
    public ISenderAdapter getSenderAdapter() {
        return null;
    }

    @Override
    public IUserRegistry getUserRegistry() {
        return null;
    }

    @Override
    public @NotNull final Platform getPlatform() {
        return Platform.VELOCITY;
    }
}