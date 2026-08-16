package com.ryderbelserion.chatterbox.api.configs.types;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IServerConfig {

    IFilterConfig getFilterConfig();

    IServerConfig init();

}