package com.ryderbelserion.chatterbox.common.storage.impl.sql.types;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.storage.impl.StorageCredentials;
import com.ryderbelserion.chatterbox.common.storage.impl.sql.SqlFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PostgresFactory extends SqlFactory {

    private final StorageCredentials credentials;
    private final int defaultPort;

    public PostgresFactory(final ChatterBoxPlugin plugin, final StorageCredentials credentials) {
        super(plugin, "PostgresSQL", "org.postgresql.Driver");

        this.credentials = credentials;
        this.defaultPort = 5432;
    }

    @Override
    public void init() {
        final HikariConfig config = new HikariConfig();

        config.setDriverClassName(this.url);

        final int port = this.credentials.port() == -1 ? this.defaultPort : this.credentials.port();

        config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s", this.url, this.credentials.address(), port, this.credentials.database()));

        config.setUsername(this.credentials.username());
        config.setPassword(this.credentials.password());

        config.setMaximumPoolSize(this.credentials.maxPoolSize());
        config.setMinimumIdle(this.credentials.minIdle());
        config.setMaxLifetime(this.credentials.maxLifetime());
        config.setKeepaliveTime(this.credentials.heartbeat());
        config.setConnectionTimeout(this.credentials.connectionTimeout());

        this.source = new HikariDataSource(config);

        super.init();
    }
}