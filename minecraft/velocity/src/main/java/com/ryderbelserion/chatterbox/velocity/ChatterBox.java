package com.ryderbelserion.chatterbox.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;

@Plugin(id = "chatterbox", name = "ChatterBox", version = "1.0.0",
        url = "https://github.com/ChatterBox", description = "Spice up your chat!", authors = {"ryderbelserion"})
public class ChatterBox {

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}