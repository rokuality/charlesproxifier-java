package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import com.rokuality.charlesproxifier.enums.BlockConnectionAction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class AllowListSettings {

    private JSONObject allowListSettingsJSON = null;

    public AllowListSettings() {
        allowListSettingsJSON = new JSONObject();
    }

    public void enableAllowRequests(boolean enableAllowRequests, BlockConnectionAction blockConnectionAction) {
        allowListSettingsJSON.put("enable_allow_list", enableAllowRequests);
        allowListSettingsJSON.put("block_connection_action", blockConnectionAction.value());
    }

    public void include(List<Location> locations) {
        JSONArray includeArr = new JSONArray();
        for (Location location : locations) {
            includeArr.add(location.getLocationAsJSON());
        }
        allowListSettingsJSON.put("include_urls", includeArr);
    }

    public JSONObject getSettingsAsJSON() {
        return allowListSettingsJSON;
    }

}
