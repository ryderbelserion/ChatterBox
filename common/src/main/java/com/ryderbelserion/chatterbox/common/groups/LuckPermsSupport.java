package com.ryderbelserion.chatterbox.common.groups;

import com.ryderbelserion.chatterbox.api.constants.Support;
import com.ryderbelserion.chatterbox.api.enums.Platform;
import com.ryderbelserion.fusion.core.api.registry.mods.objects.Mod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class LuckPermsSupport extends Mod {

    public LuckPermsSupport(final Platform platform) {
        super(platform == Platform.HYTALE ? Support.luckperms_hytale : Support.luckperms_minecraft);
    }
}