package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ThrottleSettings {

    private JSONObject throttleSettingsJSON = null;

    public ThrottleSettings() {
        throttleSettingsJSON = new JSONObject();
    }

    public void enableThrottling(boolean enableThrottling) {
        throttleSettingsJSON.put("enable_throttling", enableThrottling);
    }

    public void include(List<Location> locations) {
        JSONArray throttleIncludeArr = new JSONArray();
        for (Location location : locations) {
            throttleIncludeArr.add(location.getLocationAsJSON());
        }
        throttleSettingsJSON.put("include_urls", throttleIncludeArr);
    }

    public void setBandwidthDownload(int downloadKBPS) {
        throttleSettingsJSON.put("bandwidth_download_kbps", downloadKBPS);
    }

    public void setBandwidthUpload(int uploadKBPS) {
        throttleSettingsJSON.put("bandwidth_upload_kbps", uploadKBPS);
    }

    public void setUtilizationDownloadPercentage(int percentage) {
        throttleSettingsJSON.put("utilization_download_percentage", percentage);
    }

    public void setUtilizationUploadPercentage(int percentage) {
        throttleSettingsJSON.put("utilization_upload_percentage", percentage);
    }

    public void setRoundTripLatency(int latencyInMS) {
        throttleSettingsJSON.put("round_trip_latency_ms", latencyInMS);
    }

    public void setMTU(int mtuBytes) {
        throttleSettingsJSON.put("mtu_bytes", mtuBytes);
    }

    public void setReliabilityPercentage(int percentage) {
        throttleSettingsJSON.put("reliability_percentage", percentage);
    }

    public void setStabilityPercentage(int percentage) {
        throttleSettingsJSON.put("stability_percentage", percentage);
    }

    public void setUnstableQualityRange(int startingPercentage, int endingPercentage) {
        throttleSettingsJSON.put("unstable_quality_start", startingPercentage);
        throttleSettingsJSON.put("unstable_quality_end", endingPercentage);
    }

    public JSONObject getSettingsAsJSON() {
        return throttleSettingsJSON;
    }

}
