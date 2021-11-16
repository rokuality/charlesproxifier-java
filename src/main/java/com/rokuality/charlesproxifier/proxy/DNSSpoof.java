package com.rokuality.charlesproxifier.proxy;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class DNSSpoof {

    private JSONObject dnsSpoofJSON = null;

    public DNSSpoof() {
        dnsSpoofJSON = new JSONObject();
    }

    public void setHostName(String hostName) {
        dnsSpoofJSON.put("host", hostName);
    }

    public String getHostName() {
        return (String) dnsSpoofJSON.get("host");
    }

    public void setAddress(String address) {
        dnsSpoofJSON.put("address", address);
    }

    public String getAddress() {
        return (String) dnsSpoofJSON.get("address");
    }

    public JSONObject getSpoofAsJSON() {
        return dnsSpoofJSON;
    }

}
