package com.ryderbelserion.chatterbox.common.storage;

import com.ryderbelserion.chatterbox.common.ChatterBoxPlugin;
import com.ryderbelserion.chatterbox.api.enums.FileKeys;
import com.ryderbelserion.chatterbox.common.storage.holder.StorageHolder;
import com.ryderbelserion.chatterbox.common.storage.impl.StorageCredentials;
import com.ryderbelserion.chatterbox.common.storage.impl.sql.types.PostgresFactory;
import com.ryderbelserion.chatterbox.common.storage.impl.sql.types.SqliteFactory;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.CommentedConfigurationNode;

@NullMarked
public class StorageManager {

    private final ChatterBoxPlugin plugin;

    public StorageManager(final ChatterBoxPlugin plugin) {
        this.plugin = plugin;
    }

    public StorageHolder init() {
        final CommentedConfigurationNode configuration = FileKeys.config.getYamlConfig();

        final CommentedConfigurationNode database = configuration.node("root", "database");

        final CommentedConfigurationNode connection = database.node("connection");

        final CommentedConfigurationNode settings = database.node("pool-settings");

        final String type = database.node("type").getString("SQLITE").toLowerCase();

        final StorageCredentials credentials = new StorageCredentials(
                connection.node("database").getString(""),
                connection.node("username").getString(""),
                connection.node("password").getString(""),
                connection.node("address").getString(""),
                settings.node("connection-timeout").getLong(5000),
                settings.node("maximum-lifetime").getLong(900000),
                settings.node("maximum-pool-size").getInt(10),
                settings.node("heartbeat").getLong(0),
                settings.node("minimum-idle").getInt(10),
                connection.node("port").getInt(-1)
        );

        return switch (type) {
            case "postgres" -> new StorageHolder(new PostgresFactory(this.plugin, credentials)).init();

            case "sqlite" -> new StorageHolder(new SqliteFactory(this.plugin)).init();

            default -> throw new FusionException("Unknown Database Type: %s".formatted(type));
        };
    }
}