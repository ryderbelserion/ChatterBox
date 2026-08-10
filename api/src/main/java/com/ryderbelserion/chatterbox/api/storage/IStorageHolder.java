package com.ryderbelserion.chatterbox.api.storage;

import com.ryderbelserion.chatterbox.api.user.IUser;
import org.jspecify.annotations.NullMarked;
import java.util.UUID;

@NullMarked
public interface IStorageHolder {

    void populateCache(final IUser user);

    void insertUser(final IUser user);

    void removeUser(final UUID uuid);

    void insertMessage(final IUser user, final String id, final String message);

    void removeMessage(final String id);

    String getMessage(final IUser user);

    void setCreationDate(final IUser user);

    void setTimezone(final IUser user);

    boolean hasMessage(final String id);

    boolean hasUser(final UUID uuid);

    IStorageHolder reload();

    IStorageHolder init();

    IStorageHolder stop();

    IStorageHolder save();

}