package com.ryderbelserion.chatterbox.api;

import com.hypixel.hytale.event.EventRegistry;

public interface EventListener<E> {

    void init(final EventRegistry registry);

    Class<E> getEvent();

}