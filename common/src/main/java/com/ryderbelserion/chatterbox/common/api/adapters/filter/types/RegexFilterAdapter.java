package com.ryderbelserion.chatterbox.common.api.adapters.filter.types;

import com.ryderbelserion.chatterbox.common.api.adapters.filter.FilterAdapter;
import com.ryderbelserion.chatterbox.common.configs.FilterConfig;
import org.apache.logging.log4j.Level;
import org.jspecify.annotations.NullMarked;
import java.util.regex.Pattern;

@NullMarked
public final class RegexFilterAdapter extends FilterAdapter {

    private final Pattern[] values;

    public RegexFilterAdapter(final FilterConfig config) {
        super(config.getLevel().intLevel());

        this.values = config.getMessages().stream().map(Pattern::compile).distinct().toArray(Pattern[]::new);
    }

    @Override
    protected Result execute(final String message, final Level level) {
        if (level.intLevel() > this.minimumLevel) {
            return Result.DENY;
        }

        for (Pattern value : this.values) {
            if (value.matcher(message).find()) {
                return Result.DENY;
            }
        }

        return Result.NEUTRAL;
    }
}