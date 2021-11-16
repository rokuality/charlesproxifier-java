package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RecordingSettings {

    private JSONObject recordingSettingsJSON = null;

    public RecordingSettings() {
        recordingSettingsJSON = new JSONObject();
    }

    public void setRecordingLimit(int mbMaxSize) {
        recordingSettingsJSON.put("max_mb_recording", mbMaxSize);
    }

    public void limitRecordingHistory(int maxRequests) {
        recordingSettingsJSON.put("max_requests", maxRequests);
    }

    public void limitWebsocketHistory(int maxWebSocketMessages) {
        recordingSettingsJSON.put("max_websocket_messages", maxWebSocketMessages);
    }

    public void include(List<Location> locations) {
        JSONArray includeArr = new JSONArray();
        for (Location location : locations) {
            includeArr.add(location.getLocationAsJSON());
        }
        recordingSettingsJSON.put("include_urls", includeArr);
    }

    public void exclude(List<Location> locations) {
        JSONArray sslExcludeArr = new JSONArray();
        for (Location location : locations) {
            sslExcludeArr.add(location.getLocationAsJSON());
        }
        recordingSettingsJSON.put("exclude_urls", sslExcludeArr);
    }

    public JSONObject getSettingsAsJSON() {
        return recordingSettingsJSON;
    }

}
