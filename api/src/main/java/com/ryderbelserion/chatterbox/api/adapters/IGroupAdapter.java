package com.ryderbelserion.chatterbox.api.adapters;

import java.util.Map;

public interface IGroupAdapter {

    Map<String, String> getPlaceholders();

    String getPrimaryGroup();

    String getPrefix();

    String getSuffix();


}