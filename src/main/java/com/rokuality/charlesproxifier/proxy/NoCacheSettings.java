package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class NoCacheSettings {

    private JSONObject noCacheSettingsJSON = null;

    public NoCacheSettings() {
        noCacheSettingsJSON = new JSONObject();
    }

    public void enableNoCaching(boolean enableNoCaching) {
        noCacheSettingsJSON.put("enable_no_caching", enableNoCaching);
    }

    public void include(List<Location> locations) {
        JSONArray includeArr = new JSONArray();
        for (Location location : locations) {
            includeArr.add(location.getLocationAsJSON());
        }
        noCacheSettingsJSON.put("include_urls", includeArr);
    }

    public JSONObject getSettingsAsJSON() {
        return noCacheSettingsJSON;
    }

}
