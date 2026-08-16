package com.ryderbelserion.chatterbox.api.configs;

import com.ryderbelserion.chatterbox.api.configs.types.IServerConfig;
import com.ryderbelserion.chatterbox.api.configs.types.discord.IDiscordConfig;
import com.ryderbelserion.chatterbox.api.configs.types.discord.features.IPerServerConfig;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IConfigManager<SC extends IPerServerConfig> {

    IDiscordConfig<SC> getDiscord();

    IServerConfig getServer();

    String getServerName();

    String getTimezone();

}