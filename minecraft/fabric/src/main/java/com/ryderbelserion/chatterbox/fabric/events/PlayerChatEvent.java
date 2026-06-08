package com.ryderbelserion.chatterbox.fabric.events;

import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.chat.Component;

public class PlayerChatEvent {

    public void init() {
        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE, (sender, message) -> {
            System.out.println("Message: %s".formatted(message.getString()));

            return Component.nullToEmpty(JSONComponentSerializer.json().serialize(MiniMessage.miniMessage().deserialize(message.getString())));
        });
    }
}