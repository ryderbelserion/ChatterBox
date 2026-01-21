package com.ryderbelserion.chatterbox.api.registry;

import org.jetbrains.annotations.NotNull;

public interface IAnnotationRegistry<F> {

    void registerFeature(@NotNull final F parser);

}