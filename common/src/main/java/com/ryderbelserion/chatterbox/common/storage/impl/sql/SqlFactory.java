package com.ryderbelserion.chatterbox.common.storage.impl.sql;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.common.storage.impl.file.FlatFactory;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.zaxxer.hikari.HikariDataSource;
import org.jspecify.annotations.NonNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public abstract class SqlFactory extends FlatFactory {

    protected final String create_users_table = "create table if not exists chatterbox_players(" +
            "uuid varchar(36) primary key, " +
            "creation_date bigint not null, " +
            "timezone varchar(36) not null)";

    protected final String create_messages_table = "create table if not exists chatterbox_messages(" +
            "message_id varchar(36) primary key not null, " + // unique identifier if we need to remove a message.
            "uuid varchar(36) not null, " + // the player uuid
            "message_value text not null, " + // the text to send, limit: 1gb unless we use some other value for 4gb... but if you need 4gb, rethink your life.
            "is_enabled integer not null check (is_enabled in (0,1)), " + // 0 is false, 1 is true.
            "foreign key(uuid) references chatterbox_players(uuid) on delete cascade)";

    protected final String index_player_uid = "create unique index if not exists idx_player_uuid on chatterbox_messages(uuid)";

    /*protected final String create_messages_table = "create table if not exists chatterbox_messages(" +
            "uuid varchar(36) primary key not null, " +
            "message_id varchar(16) not null, " + // unique identifier if we need to remove a message.
            "message_value text not null, " + // the text to send, limit: 1gb unless we use some other value for 4gb... but if you need 4gb, rethink your life.
            "is_enabled integer not null check (is_enabled in (0,1)), " + // 0 is false, 1 is true.
            "foreign key(uuid) references chatterbox_players(uuid) on delete cascade)";

    protected final String index_message_id = "create unique index if not exists idx_message_id on chatterbox_messages(message_id)";*/

    protected HikariDataSource source;

    public SqlFactory(final ChatterBoxPlugin plugin, final String impl, final String url) {
        super(plugin, impl, url);
    }

    @Override
    public void init() {
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = getConnection()) {
                try (final Statement statement = connection.createStatement()) {
                    //todo() add missing columns automatically in a way that just isn't ass?

                    statement.addBatch(this.create_users_table);

                    statement.addBatch(this.create_messages_table);
                    statement.addBatch(this.index_player_uid);

                    statement.executeBatch();
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void reload() {
        //todo() add missing columns automatically
    }

    @Override
    public void stop() {
        try {
            getConnection().close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save() {

    }

    @Override
    public @NonNull Connection getConnection() throws SQLException {
        if (this.source.isClosed()) {
            throw new FusionException("Failed to get connection from pool. (Source returned closed)");
        }

        return this.source.getConnection();
    }

    public boolean tableExists(final String table) {
        try (final ResultSet resultSet = getConnection().getMetaData().getTables(
                null,
                null,
                table,
                null
        )) {
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();

            return false;
        }
    }
}