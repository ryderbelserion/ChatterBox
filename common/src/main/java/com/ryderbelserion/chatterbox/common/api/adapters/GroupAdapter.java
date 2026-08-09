package com.ryderbelserion.chatterbox.common.api.adapters;

import com.ryderbelserion.chatterbox.api.adapters.IGroupAdapter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public final class GroupAdapter implements IGroupAdapter {

    private String primaryGroup = "";
    private String prefix = "";
    private String suffix = "";

    public GroupAdapter(final UUID uuid) {
        final LuckPerms luckperms = LuckPermsProvider.get();

        final UserManager userManager = luckperms.getUserManager();

        final User user = userManager.getUser(uuid);

        if (user == null) {
            return;
        }

        this.primaryGroup = user.getPrimaryGroup();

        final CachedMetaData data = user.getCachedData().getMetaData();

        final String prefix = data.getPrefix();

        if (prefix != null) {
            this.prefix = data.getPrefix();
        }

        final String suffix = data.getSuffix();

        if (suffix != null) {
            this.suffix = data.getSuffix();
        }
    }

    public GroupAdapter() {
        this.primaryGroup = "";
        this.prefix = "";
        this.suffix = "";
    }

    @Override
    public Map<String, String> getPlaceholders() {
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
    public String getPrimaryGroup() {
        return this.primaryGroup;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }
}