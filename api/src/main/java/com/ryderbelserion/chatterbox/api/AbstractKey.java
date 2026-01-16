package com.ryderbelserion.chatterbox.api;

import org.jetbrains.annotations.NotNull;

public record AbstractKey(String namespace, String value) {

    public AbstractKey(@NotNull final String namespace, @NotNull final String value) {
        this.namespace = namespace;

        this.value = value;
    }

    public static AbstractKey key(@NotNull final String namespace, @NotNull final String value) {
        return new AbstractKey(namespace, value);
    }

    public static AbstractKey key(@NotNull final String value) {
        if (!value.contains(":")) return key(AbstractChatterBox.namespace, value);

        final String[] split = value.split(":");

        return new AbstractKey(split[0], split[1]);
    }

    @Override
    public @NotNull String namespace() {
        return this.namespace;
    }

    @Override
    public @NotNull String value() {
        return this.value;
    }

    public @NotNull String asString() {
        return this.namespace + ":" + this.value;
    }
}