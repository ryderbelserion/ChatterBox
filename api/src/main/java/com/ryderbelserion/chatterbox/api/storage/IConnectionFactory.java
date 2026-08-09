package com.ryderbelserion.chatterbox.api.storage;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.sql.Connection;
import java.sql.SQLException;

@NullMarked
public abstract class IConnectionFactory {

    @ApiStatus.Internal
    public abstract Connection getConnection() throws SQLException;

    public abstract String getImpl();

    public abstract String getUrl();

    public abstract void init();

    public abstract void stop();

    public abstract void save();

}