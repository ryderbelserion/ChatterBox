package com.ryderbelserion.chatterbox.api.users.objects;

import java.util.UUID;

public interface IUser {

    String getUsername();

    String getLocale();

    UUID getUuid();

}