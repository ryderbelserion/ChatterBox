package com.ryderbelserion.chatterbox.common.storage.impl.sql.types;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.storage.impl.sql.SqlFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jspecify.annotations.NullMarked;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@NullMarked
public final class SqliteFactory extends SqlFactory {

    private final Path path;

    public SqliteFactory(final ChatterBoxPlugin plugin) {
        super(plugin, "SQLite", "");

        this.path = plugin.getDataPath().resolve("chatterbox.db");
    }

    @Override
    public void init() {
        if (!Files.exists(this.path)) {
            try {
                final Path parent = this.path.getParent();

                if (!Files.exists(parent)) {
                    Files.createDirectory(parent);
                }

                Files.createFile(this.path);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl(getUrl());
        config.setMaximumPoolSize(5); // 5 is enough for flat file.
        config.setConnectionInitSql("PRAGMA foreign_keys = ON;");

        this.source = new HikariDataSource(config);

        super.init();
    }

    @Override
    public String getUrl() {
        return "jdbc:sqlite:" + this.path.toFile().getAbsolutePath();
    }
}