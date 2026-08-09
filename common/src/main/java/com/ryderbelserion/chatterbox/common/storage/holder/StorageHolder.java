package com.ryderbelserion.chatterbox.common.storage.holder;

import com.ryderbelserion.chatterbox.api.storage.IConnectionFactory;
import com.ryderbelserion.chatterbox.api.storage.IStorageHolder;
import com.ryderbelserion.chatterbox.api.user.IUser;
import org.jspecify.annotations.NullMarked;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class StorageHolder implements IStorageHolder {

    private final IConnectionFactory factory;

    public StorageHolder(final IConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void insertUser(final IUser user) {
        final UUID uuid = user.getUniqueId();

        CompletableFuture.runAsync(() -> {
            try (final Connection connection = this.factory.getConnection()) {
                try (final PreparedStatement statement =
                             connection.prepareStatement("insert into chatterbox_players (uuid, creation_date, timezone) values(?, ?, ?)")) {
                    statement.setString(1, uuid.toString());
                    statement.setLong(2, Instant.now().getEpochSecond());
                    statement.setString(3, user.getTimezone().getId());

                    statement.executeUpdate();
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void removeUser(final UUID uuid) {
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = this.factory.getConnection(); final PreparedStatement statement = connection.prepareStatement("delete from chatterbox_players where uuid=?")) {
                statement.setString(1, uuid.toString());

                statement.executeUpdate();
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void insertMessage(final IUser user, final String id, final String message) {
        if (hasMessage(id)) {
            // update message
            CompletableFuture.runAsync(() -> {
                try (final Connection connection = this.factory.getConnection(); final PreparedStatement statement = connection.prepareStatement("update chatterbox_messages set message_value=? where message_id=?")) {
                    statement.setString(1, message);
                    statement.setString(2, id);
                } catch (final SQLException exception) {
                    exception.printStackTrace();
                }
            });

            return;
        }

        // fresh insert
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = this.factory.getConnection(); final PreparedStatement statement = connection.prepareStatement("insert into chatterbox_messages (uuid, message_id, message_value, is_enabled) values (?, ?, ?, ?)")) {
                statement.setString(1, user.getUniqueId().toString());
                statement.setString(2, id);
                statement.setString(3, message);
                statement.setInt(4, 0);
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void removeMessage(final String id) {
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = this.factory.getConnection(); final PreparedStatement statement = connection.prepareStatement("delete from chatterbox_messages where message_id=?")) {
                statement.setString(1, id);

                statement.executeUpdate();
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void setCreationDate(final IUser user) {
        final UUID uuid = user.getUniqueId();

        CompletableFuture.runAsync(() -> {
            try (final Connection connection = this.factory.getConnection(); final PreparedStatement statement =
                    connection.prepareStatement("select (creation_date) from chatterbox_players where uuid=?")) {
                statement.setString(1, uuid.toString());

                final ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    user.setCreationDate(rs.getLong("creation_date"));
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public boolean hasMessage(final String id) {
        return CompletableFuture.supplyAsync(() -> {
            boolean hasMessage = false;

            try (final Connection connection = this.factory.getConnection(); final PreparedStatement statement = connection.prepareStatement("select 1 from chatterbox_messages where message_id=?")) {
                statement.setString(1, id);

                final ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    hasMessage = true;

                    return hasMessage;
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }

            return hasMessage;
        }).join();
    }

    @Override
    public boolean hasUser(final UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            boolean hasUser = false;

            try (final Connection connection = this.factory.getConnection(); final PreparedStatement statement =
                    connection.prepareStatement("select 1 from chatterbox_players where uuid=?")) {
                statement.setString(1, uuid.toString());

                final ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    hasUser = true;

                    return hasUser;
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }

            return hasUser;
        }).join();
    }

    @Override
    public StorageHolder init() {
        this.factory.init();

        return this;
    }

    @Override
    public StorageHolder reload() {
        this.factory.reload();

        return this;
    }

    @Override
    public StorageHolder stop() {
        this.factory.stop();

        return this;
    }

    @Override
    public StorageHolder save() {
        this.factory.save();

        return this;
    }
}