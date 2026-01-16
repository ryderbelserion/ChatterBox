package com.ryderbelserion.chatterbox.api.listeners;

import com.hypixel.hytale.event.EventRegistry;

public interface EventListener<E> {

    void init(final EventRegistry registry);

    Class<E> getEvent();

}