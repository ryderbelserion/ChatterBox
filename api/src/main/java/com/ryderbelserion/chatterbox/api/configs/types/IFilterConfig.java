package com.ryderbelserion.chatterbox.api.configs.types;

import org.jspecify.annotations.NullMarked;
import java.util.List;

@NullMarked
public interface IFilterConfig<L> {

    List<String> getMessages();

    L getLevel();

    boolean isUseRegex();

    boolean isEnabled();

}