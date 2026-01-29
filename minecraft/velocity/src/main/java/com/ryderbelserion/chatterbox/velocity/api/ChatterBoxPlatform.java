package com.ryderbelserion.chatterbox.velocity.api;

import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.chatterbox.api.registry.IContextRegistry;
import com.ryderbelserion.chatterbox.api.registry.IMessageRegistry;
import com.ryderbelserion.chatterbox.api.registry.IUserRegistry;
import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.api.adapters.sender.ISenderAdapter;
import com.ryderbelserion.chatterbox.velocity.ChatterBox;
import com.ryderbelserion.chatterbox.velocity.api.discord.DiscordManager;
import com.ryderbelserion.fusion.FusionVelocity;
import com.ryderbelserion.fusion.files.enums.FileType;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxPlatform extends ChatterBoxPlugin<Audience, Object> {

    private final ChatterBox instance;

    public ChatterBoxPlatform(@NotNull final FusionVelocity fusion, @NotNull final ChatterBox instance) {
        super(fusion);

        this.instance = instance;
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

        this.discordManager = new DiscordManager(fusion, this.instance);
        this.discordManager.init();
    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public @NotNull final IMessageRegistry getMessageRegistry() {
        return null;
    }

    @Override
    public @NotNull final IContextRegistry getContextRegistry() {
        return null;
    }

    @Override
    public @NotNull final ISenderAdapter getSenderAdapter() {
        return null;
    }

    @Override
    public @NotNull final IUserRegistry getUserRegistry() {
        return null;
    }

    @Override
    public @NotNull final Platform getPlatform() {
        return Platform.VELOCITY;
    }
}