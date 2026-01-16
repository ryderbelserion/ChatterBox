package com.ryderbelserion.chatterbox;

import com.ryderbelserion.chatterbox.api.AbstractChatterBox;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class ChatterBoxProvider {

    private static AbstractChatterBox instance;

    @ApiStatus.Internal
    private ChatterBoxProvider() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static AbstractChatterBox getInstance() {
        return instance;
    }

    @ApiStatus.Internal
    public static void register(@NotNull final AbstractChatterBox instance) {
        ChatterBoxProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        ChatterBoxProvider.instance = null;
    }
}