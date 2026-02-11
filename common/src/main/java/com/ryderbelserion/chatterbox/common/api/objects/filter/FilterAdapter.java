package com.ryderbelserion.chatterbox.common.api.objects.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.NotNull;

public abstract class FilterAdapter extends AbstractFilter {

    protected final int minimumLevel;

    protected FilterAdapter(final int minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Message message, final Throwable throwable) {
        Result result = throwable != null ? execute(throwable.getMessage(), level) : Result.NEUTRAL;

        if (message != null) {
            result = execute(message.getFormattedMessage(), level);
        }

        return result;
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Object message, final Throwable throwable) {
        Result result = throwable != null ? execute(throwable.getMessage(), level) : Result.NEUTRAL;

        if (message != null) {
            result = execute(message.toString(), level);
        }

        return result;
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String message, final Object... params) {
        return execute(message, level);
    }

    @Override
    public Result filter(final LogEvent event) {
        return event == null ? Result.NEUTRAL : execute(event.getMessage().getFormattedMessage(), event.getLevel());
    }

    protected abstract Result execute(@NotNull final String message, @NotNull final Level level);

}