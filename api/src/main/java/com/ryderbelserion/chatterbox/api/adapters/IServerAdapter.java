package com.ryderbelserion.chatterbox.api.adapters;

import com.ryderbelserion.chatterbox.api.enums.server.ServerState;
import org.jspecify.annotations.NonNull;

public interface IServerAdapter {

    boolean hasState(@NonNull final ServerState state);

    void removeState(@NonNull final ServerState state);

    void addState(@NonNull final ServerState state);

}