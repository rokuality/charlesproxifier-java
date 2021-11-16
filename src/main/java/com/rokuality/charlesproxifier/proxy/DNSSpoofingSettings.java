package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class DNSSpoofingSettings {

    private JSONObject dnsSpoofingSettingsJSON = null;

    public DNSSpoofingSettings() {
        dnsSpoofingSettingsJSON = new JSONObject();
    }

    public void enableDNSSpoofing(boolean enableDNSSpoofing) {
        dnsSpoofingSettingsJSON.put("enable_dns_spoofing", enableDNSSpoofing);
    }

    public void include(List<DNSSpoof> dnsSpoofEntries) {
        JSONArray includeArr = new JSONArray();
        for (DNSSpoof spoof : dnsSpoofEntries) {
            includeArr.add(spoof.getSpoofAsJSON());
        }
        dnsSpoofingSettingsJSON.put("dns_spoof_entries", includeArr);
    }

    public JSONObject getSettingsAsJSON() {
        return dnsSpoofingSettingsJSON;
    }

}
