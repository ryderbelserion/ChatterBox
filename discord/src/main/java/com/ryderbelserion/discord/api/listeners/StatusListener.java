package com.ryderbelserion.discord.api.listeners;

import com.ryderbelserion.discord.DiscordPlugin;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class StatusListener extends ListenerAdapter {

    private final DiscordPlugin plugin;

    public StatusListener(@NotNull final DiscordPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        this.plugin.onGuildReady(event.getGuild());
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        this.plugin.onStop(event.getJDA());
    }

    @Override
    public void onReady(ReadyEvent event) {
        this.plugin.onReady(event.getJDA());
    }
}