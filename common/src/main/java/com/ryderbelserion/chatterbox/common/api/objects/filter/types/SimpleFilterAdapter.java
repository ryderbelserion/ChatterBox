package com.ryderbelserion.chatterbox.common.api.objects.filter.types;

import com.ryderbelserion.chatterbox.common.api.objects.filter.FilterAdapter;
import com.ryderbelserion.chatterbox.common.configs.FilterConfig;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

public class SimpleFilterAdapter extends FilterAdapter {

    private final String[] values;

    public SimpleFilterAdapter(@NotNull final FilterConfig config) {
        super(config.getLevel().intLevel());

        this.values = config.getMessages().toArray(new String[0]);
    }

    @Override
    protected Result execute(@NotNull final String message, @NotNull final Level level) {
        if (level.intLevel() > this.minimumLevel) {
            return Result.DENY;
        }

        for (String value : this.values) {
            if (message.contains(value)) {
                return Result.DENY;
            }
        }

        return Result.NEUTRAL;
    }
}