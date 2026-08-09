package com.ryderbelserion.chatterbox.common.api.adapters.sender;

import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jspecify.annotations.NullMarked;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public abstract class ISenderAdapter<C, S> {

    public abstract UUID getUniqueId(final S sender);

    public abstract String getName(final S sender);

    public abstract void sendMessage(final S sender, final FusionKey id, final Map<String, String> placeholders);

    public abstract void broadcast(final S sender, final FusionKey id, final Map<String, String> placeholders);

    public abstract C getComponent(final S sender, final FusionKey id, final Map<String, String> placeholders);

    public abstract String getMessage(final S sender, final FusionKey id, final Map<String, String> placeholders);

    public String getMessage(final S sender, final FusionKey id) {
        return getMessage(sender, id, Map.of());
    }

    public C getComponent(final S sender, final FusionKey id) {
        return getComponent(sender, id, new HashMap<>());
    }

    public void sendMessage(final S sender, final FusionKey id) {
        sendMessage(sender, id, new HashMap<>());
    }

    public void broadcast(final S sender, final FusionKey id) {
        broadcast(sender, id, new HashMap<>());
    }

    public abstract boolean isConsole(S sender);

}