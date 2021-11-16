package com.rokuality.charlesproxifier.proxy;

import java.util.regex.Pattern;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Match {

    private JSONObject matchJSON = null;

    public Match() {
        matchJSON = new JSONObject();
    }

    public void matchRequest(boolean matchRequest) {
        matchJSON.put("match_request", matchRequest);
    }

    public void matchResponse(boolean matchResponse) {
        matchJSON.put("match_response", matchResponse);
    }

    public void setName(String name) {
        matchJSON.put("name", name);
        matchJSON.put("name_is_regex", false);
    }

    public void setName(Pattern name) {
        matchJSON.put("name", name.toString());
        matchJSON.put("name_is_regex", true);
    }

    public void setValue(String value) {
        matchJSON.put("value", value);
        matchJSON.put("value_is_regex", false);
    }

    public void setValue(Pattern value) {
        matchJSON.put("value", value.toString());
        matchJSON.put("value_is_regex", true);
    }

    public void matchWholeValue(boolean matchWholeValue) {
        matchJSON.put("match_whole_value", matchWholeValue);
    }

    public void caseSensitive(boolean isCaseSensitive) {
        matchJSON.put("case_sensitive", isCaseSensitive);
    }

    public JSONObject getMatchAsJSON() {
        return matchJSON;
    }

}
