package com.ryderbelserion.chatterbox.api.adapters;

import com.ryderbelserion.chatterbox.api.enums.server.ServerState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IServerAdapter {

    boolean hasState(final ServerState state);

    void removeState(final ServerState state);

    void addState(final ServerState state);

}