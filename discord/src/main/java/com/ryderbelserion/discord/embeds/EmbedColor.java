package com.ryderbelserion.discord.embeds;

import com.ryderbelserion.discord.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public enum EmbedColor {

    DEFAULT("#bff7fd"),
    SUCCESS("#0eeb6a"),
    FAIL("#e0240b"),
    WARNING("#eb6123"),
    EDIT("#5e68ff");

    private final Color color;

    EmbedColor(@NotNull final String code) {
        this.color = ColorUtils.toColor(code);
    }

    public @NotNull final Color getColor() {
        return this.color;
    }
}