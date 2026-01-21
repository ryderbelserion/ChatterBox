package com.ryderbelserion.chatterbox.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxProvider {

    private static ChatterBox instance;

    @ApiStatus.Internal
    private ChatterBoxProvider() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ChatterBox getInstance() {
        return instance;
    }

    @ApiStatus.Internal
    public static void register(@NotNull final ChatterBox instance) {
        ChatterBoxProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        ChatterBoxProvider.instance = null;
    }
}