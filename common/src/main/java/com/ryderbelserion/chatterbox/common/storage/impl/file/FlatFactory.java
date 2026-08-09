package com.ryderbelserion.chatterbox.common.storage.impl.file;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.storage.IConnectionFactory;
import com.ryderbelserion.fusion.core.FusionCore;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class FlatFactory extends IConnectionFactory {

    protected final ChatterBoxPlugin plugin;
    protected final FusionCore fusion;

    protected final String impl;
    protected final String url;

    public FlatFactory(final ChatterBoxPlugin plugin, final String impl, final String url) {
        this.fusion = plugin.getFusion();
        this.plugin = plugin;
        this.impl = impl;
        this.url = url;
    }

    @Override
    public String getImpl() {
        return this.impl;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}