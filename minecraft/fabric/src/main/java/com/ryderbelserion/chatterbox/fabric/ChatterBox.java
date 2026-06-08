package com.ryderbelserion.chatterbox.fabric;

import com.ryderbelserion.chatterbox.fabric.events.PlayerChatEvent;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ChatterBox implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        System.out.println("ChatterBox is now ready!");

        new PlayerChatEvent().init();
    }
}