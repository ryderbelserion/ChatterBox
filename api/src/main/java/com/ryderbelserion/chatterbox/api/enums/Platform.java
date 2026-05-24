package com.ryderbelserion.chatterbox.api.enums;

import org.jspecify.annotations.NonNull;

public enum Platform {

    MINECRAFT("Minecraft", ""),
    VELOCITY("Velocity", "velocity"),
    HYTALE("Hytale", "");

    private final String jarFolder;
    private final String name;

    Platform(@NonNull final String name, @NonNull final String jarFolder) {
        this.jarFolder = jarFolder;
        this.name = name;
    }

    public @NonNull final String getJarFolder() {
        return this.jarFolder;
    }

    public @NonNull final String getName() {
        return this.name;
    }
}