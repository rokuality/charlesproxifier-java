package com.rokuality.charlesproxifier.proxy;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Replace {

    private JSONObject replaceJSON = null;

    public Replace() {
        replaceJSON = new JSONObject();
    }

    public void setName(String name) {
        replaceJSON.put("name", name);
    }

    public void setValue(String value) {
        replaceJSON.put("value", value);
    }

    public void replaceFirst(boolean replaceFirst) {
        replaceJSON.put("replace_first", replaceFirst);
    }

    public void replaceAll(boolean replaceAll) {
        replaceJSON.put("replace_all", replaceAll);
    }

    public JSONObject getReplaceAsJSON() {
        return replaceJSON;
    }

}
