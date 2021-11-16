package com.rokuality.charlesproxifier.proxy;

import com.rokuality.charlesproxifier.enums.RewriteRuleType;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RewriteRule {

    private JSONObject rewriteRuleJSON = null;

    public RewriteRule() {
        rewriteRuleJSON = new JSONObject();
    }

    public void setName(String rewriteName) {
        rewriteRuleJSON.put("name", rewriteName);
    }

    public void setRuleType(RewriteRuleType ruleType) {
        rewriteRuleJSON.put("rewrite_type", ruleType.value());
    }

    public void setLocation(Location location) {
        rewriteRuleJSON.put("location", location.getLocationAsJSON());
    }

    public void setMatch(Match match) {
        rewriteRuleJSON.put("match", match.getMatchAsJSON());
    }

    public void setReplace(Replace replace) {
        rewriteRuleJSON.put("replace", replace.getReplaceAsJSON());
    }

    public JSONObject getRewriteRuleAsJSON() {
        return this.rewriteRuleJSON;
    }

}
