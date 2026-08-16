package com.ryderbelserion.chatterbox.api.configs.types.discord.features;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IPresenceConfig {

    String getStatus();

    boolean isEnabled();

}