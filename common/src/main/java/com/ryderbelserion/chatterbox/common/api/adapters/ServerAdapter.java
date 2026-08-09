package com.ryderbelserion.chatterbox.common.api.adapters;

import com.ryderbelserion.chatterbox.api.adapters.IServerAdapter;
import com.ryderbelserion.chatterbox.api.enums.server.ServerState;
import org.jspecify.annotations.NullMarked;
import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class ServerAdapter implements IServerAdapter {

    private final List<ServerState> states = new ArrayList<>();

    @Override
    public boolean hasState(final ServerState state) {
        return this.states.contains(state);
    }

    @Override
    public void removeState(final ServerState state) {
        this.states.remove(state);
    }

    @Override
    public void addState(final ServerState state) {
        this.states.add(state);
    }
}