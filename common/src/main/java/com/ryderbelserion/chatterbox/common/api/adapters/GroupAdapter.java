package com.ryderbelserion.chatterbox.common.api.adapters;

import com.ryderbelserion.chatterbox.api.adapters.IGroupAdapter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupAdapter implements IGroupAdapter {

    private final LuckPerms luckperms = LuckPermsProvider.get();

    private final UserManager userManager = this.luckperms.getUserManager();

    private String primaryGroup = "";
    private String prefix = "";
    private String suffix = "";

    public GroupAdapter(@NotNull final UUID uuid) {
        final User user = this.userManager.getUser(uuid);

        if (user == null) {
            return;
        }

        this.primaryGroup = user.getPrimaryGroup();

        final CachedMetaData data = user.getCachedData().getMetaData();

        this.prefix = data.getPrefix();
        this.suffix = data.getSuffix();
    }

    public GroupAdapter() {
        this.primaryGroup = "";
        this.prefix = "";
        this.suffix = "";
    }

    @Override
    public @NotNull final Map<String, String> getPlaceholders() {
        final Map<String, String> placeholders = new HashMap<>();

        if (!this.primaryGroup.isBlank()) {
            placeholders.put("{group}", this.primaryGroup);
        }

        if (!this.prefix.isBlank()) {
            placeholders.put("{prefix}", this.prefix);
        }

        if (!this.suffix.isBlank()) {
            placeholders.put("{suffix}", this.suffix);
        }

        return Collections.unmodifiableMap(placeholders);
    }

    @Override
    public @NotNull final String getPrimaryGroup() {
        return this.primaryGroup;
    }

    @Override
    public @NotNull final String getPrefix() {
        return this.prefix;
    }

    @Override
    public @NotNull final String getSuffix() {
        return this.suffix;
    }
}