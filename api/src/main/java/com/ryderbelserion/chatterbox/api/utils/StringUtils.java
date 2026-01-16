package com.ryderbelserion.chatterbox.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;

public class StringUtils {

    private static final char LF = '\n';
    private static final char CR = '\r';

    public static String replacePlaceholders(@NotNull final String message, @NotNull final Map<String, String> placeholders) {
        String safeMessage = message;

        for (final Map.Entry<String, String> entry : placeholders.entrySet()) {
            final String placeholder = entry.getKey();
            final String value = entry.getValue();

            safeMessage = safeMessage.replace(placeholder, value);
        }

        return safeMessage;
    }

    public static @NotNull String toString(@NotNull final List<String> list) {
        if (list.isEmpty()) return "";

        final StringBuilder message = new StringBuilder(list.size());

        for (final String line : list) {
            message.append(line).append("\n");
        }

        return chomp(message.toString());
    }

    public static String chomp(@Nullable final String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        if (value.length() == 1) {
            final char character = value.charAt(0);

            return character == CR || character == LF ? "" : value;
        }

        int lastIdx = value.length() - 1;

        final char last = value.charAt(lastIdx);

        if (last == LF) {
            if (value.charAt(lastIdx - 1) == CR) {
                lastIdx--;
            }
        } else if (last != CR) {
            lastIdx++;
        }

        return value.substring(0, lastIdx);
    }
}