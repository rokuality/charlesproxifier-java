package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class SSLProxySettings {

    private JSONObject sslProxySettingsJSON = null;

    public SSLProxySettings() {
        sslProxySettingsJSON = new JSONObject();
    }

    public void enableSSLProxying(boolean enableSSLproxying) {
        sslProxySettingsJSON.put("enable_ssl_proxying", enableSSLproxying);
    }

    public void include(List<Location> locations) {
        JSONArray sslIncludeArr = new JSONArray();
        for (Location location : locations) {
            sslIncludeArr.add(location.getLocationAsJSON());
        }
        sslProxySettingsJSON.put("ssl_include_urls", sslIncludeArr);
    }

    public void exclude(List<Location> locations) {
        JSONArray sslExcludeArr = new JSONArray();
        for (Location location : locations) {
            sslExcludeArr.add(location.getLocationAsJSON());
        }
        sslProxySettingsJSON.put("ssl_exclude_urls", sslExcludeArr);
    }

    public JSONObject getSettingsAsJSON() {
        return sslProxySettingsJSON;
    }

}
