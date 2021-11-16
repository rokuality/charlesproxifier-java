package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class BlockCookiesSettings {

    private JSONObject blockCookiesSettingsJSON = null;

    public BlockCookiesSettings() {
        blockCookiesSettingsJSON = new JSONObject();
    }

    public void enableBlockCookies(boolean enableBlockCookies) {
        blockCookiesSettingsJSON.put("enable_block_cookies", enableBlockCookies);
    }

    public void include(List<Location> locations) {
        JSONArray includeArr = new JSONArray();
        for (Location location : locations) {
            includeArr.add(location.getLocationAsJSON());
        }
        blockCookiesSettingsJSON.put("include_urls", includeArr);
    }

    public JSONObject getSettingsAsJSON() {
        return blockCookiesSettingsJSON;
    }

}
