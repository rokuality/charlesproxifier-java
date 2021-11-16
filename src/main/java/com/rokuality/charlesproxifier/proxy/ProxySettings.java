package com.rokuality.charlesproxifier.proxy;

import java.util.HashSet;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ProxySettings {

    private JSONObject proxySettingsJSON = null;

    public ProxySettings() {
        proxySettingsJSON = new JSONObject();
    }

    public void setCharlesBinaryLocation(String pathToCharles) {
        proxySettingsJSON.put("charles_path", pathToCharles);
    }

    public void setHeadless(boolean headlessProxy) {
        proxySettingsJSON.put("headless", headlessProxy);
    }

    public void setPort(int port) {
        proxySettingsJSON.put("port", port);
    }

    public void enableTransparentHttpProxying(boolean enableTransparentProxy) {
        proxySettingsJSON.put("enable_transparent_proxy", enableTransparentProxy);
    }

    public void enableSOCKSProxy(boolean enableSOCKSProxy) {
        proxySettingsJSON.put("enable_socks_proxy", enableSOCKSProxy);
    }

    public void setSOCKSProxyPort(int port) {
        proxySettingsJSON.put("socks_proxy_port", port);
    }

    public void bypassProxy(HashSet<String> urlsToBypass) {
        String bypassUrls = String.join(",", urlsToBypass);
        proxySettingsJSON.put("urls_to_bypass", bypassUrls);
    }

    public JSONObject getSettingsAsJSON() {
        return proxySettingsJSON;
    }

}
