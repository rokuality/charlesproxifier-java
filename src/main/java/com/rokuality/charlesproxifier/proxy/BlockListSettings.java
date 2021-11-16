package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import com.rokuality.charlesproxifier.enums.BlockConnectionAction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class BlockListSettings {

    private JSONObject blockListSettingsJSON = null;

    public BlockListSettings() {
        blockListSettingsJSON = new JSONObject();
    }

    public void enableBlockRequests(boolean enableBlockRequests, BlockConnectionAction blockConnectionAction) {
        blockListSettingsJSON.put("enable_block_list", enableBlockRequests);
        blockListSettingsJSON.put("block_connection_action", blockConnectionAction.value());
    }

    public void include(List<Location> locations) {
        JSONArray includeArr = new JSONArray();
        for (Location location : locations) {
            includeArr.add(location.getLocationAsJSON());
        }
        blockListSettingsJSON.put("include_urls", includeArr);
    }

    public JSONObject getSettingsAsJSON() {
        return blockListSettingsJSON;
    }

}
