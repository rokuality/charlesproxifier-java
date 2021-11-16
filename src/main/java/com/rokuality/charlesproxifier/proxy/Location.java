package com.rokuality.charlesproxifier.proxy;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Location {

    private JSONObject locationJSON = null;

    public Location() {
        locationJSON = new JSONObject();
    }

    public void setProtocol(String protocol) {
        locationJSON.put("protocol", protocol);
    }

    public String getProtocol() {
        return (String) locationJSON.get("protocol");
    }

    public void setHost(String host) {
        locationJSON.put("host", host);
    }

    public String getHost() {
        return (String) locationJSON.get("host");
    }

    public void setPort(String port) {
        locationJSON.put("port", port);
    }

    public String getPort() {
        return (String) locationJSON.get("port");
    }

    public void setPath(String path) {
        locationJSON.put("path", path);
    }

    public String getPath() {
       return (String) locationJSON.get("path");
    }

    public void setQueryString(String queryString) {
        locationJSON.put("query", queryString);
    }

    public String getQueryString() {
        return (String) locationJSON.get("query");
    }

    public JSONObject getLocationAsJSON() {
       return locationJSON;
    }

}
