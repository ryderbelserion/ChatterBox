package com.ryderbelserion.chatterbox.common.api.objects.filter.types;

import com.ryderbelserion.chatterbox.common.api.objects.filter.FilterAdapter;
import com.ryderbelserion.chatterbox.common.configs.FilterConfig;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Pattern;

public class RegexFilterAdapter extends FilterAdapter {

    private final Pattern[] values;

    public RegexFilterAdapter(@NotNull final FilterConfig config) {
        super(config.getLevel().intLevel());

        this.values = config.getMessages().stream().map(Pattern::compile).distinct().toArray(Pattern[]::new);
    }

    @Override
    protected Result execute(@NotNull final String message, @NotNull final Level level) {
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