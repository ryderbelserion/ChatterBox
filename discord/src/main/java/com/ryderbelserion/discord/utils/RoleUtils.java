package com.ryderbelserion.discord.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public class RoleUtils {

    public static @NotNull Optional<Role> getHighestRoleByColor(@NotNull final Member member) {
        Role foundRole = null;

        for (final Role role : member.getRoles()) {
            if (role.getColor() == null) {
                continue;
            }

            foundRole = role;

            break;
        }

        return Optional.ofNullable(foundRole);
    }

    public static @NotNull Optional<Role> getHighestRole(@NotNull final Member member) {
        final List<Role> roles = member.getRoles();

        return roles.isEmpty() ? Optional.empty() : Optional.of(roles.getFirst());
    }

    public static @NotNull String getRoleName(@NotNull final Role role) {
        return role.getName();
    }
}