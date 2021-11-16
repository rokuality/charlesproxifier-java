package com.rokuality.charlesproxifier.proxy;

import java.io.File;

import com.rokuality.charlesproxifier.enums.AutoSaveType;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class AutoSaveSettings {

    private JSONObject autoSaveSettingsJSON = null;

    public AutoSaveSettings() {
        autoSaveSettingsJSON = new JSONObject();
    }

    public void enableAutoSave(boolean enableAutoSave, int intervalInMinutes, File saveToDirectory,
            AutoSaveType autoSaveType) {
        autoSaveSettingsJSON.put("enable_auto_save", enableAutoSave);
        autoSaveSettingsJSON.put("interval_in_minutes", intervalInMinutes);
        autoSaveSettingsJSON.put("directory_to_save_to", saveToDirectory.getAbsolutePath());
        autoSaveSettingsJSON.put("auto_save_type", autoSaveType.value());
    }

    public JSONObject getSettingsAsJSON() {
        return autoSaveSettingsJSON;
    }

}
