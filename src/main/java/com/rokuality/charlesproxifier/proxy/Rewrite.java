package com.rokuality.charlesproxifier.proxy;

import com.rokuality.charlesproxifier.enums.RewriteRuleType;
import com.rokuality.charlesproxifier.exceptions.ProxyRewriteException;
import com.rokuality.charlesproxifier.httpexecutor.HttpClient;
import com.rokuality.charlesproxifier.utils.JsonUtils;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Rewrite {

    private ServerPostHandler serverPostHandler = null;
    private JSONObject sessionJSON = null;

    public Rewrite(HttpClient httpClient, JSONObject sessionJSON) {
        serverPostHandler = new ServerPostHandler(httpClient);
        this.sessionJSON = JsonUtils.deepCopy(sessionJSON);
    }

    public void applyRule(RewriteRule rewriteRule) {
        if (rewriteRule == null) {
            throw new ProxyRewriteException("You must provide a rewrite rule object!");
        }

        JSONObject rewriteJSON = rewriteRule.getRewriteRuleAsJSON();
        if (!rewriteJSON.containsKey("name")) {
            throw new ProxyRewriteException("Rewrite rule must contain a name!");
        }

        if (!rewriteJSON.containsKey("rewrite_type")) {
            throw new ProxyRewriteException("Rewrite rule must contain a valid rule type!");
        }

        if (!rewriteJSON.containsKey("location")) {
            throw new ProxyRewriteException("Rewrite rule must contain a location object!");
        }

        if (!rewriteJSON.containsKey("match")) {
            throw new ProxyRewriteException("Rewrite rule must contain a match object!");
        }

        RewriteRuleType ruleType = RewriteRuleType.getEnumByString((String) rewriteJSON.get("rewrite_type"));
        if (!rewriteJSON.containsKey("replace") && !(RewriteRuleType.REMOVE_HEADER.equals(ruleType) || RewriteRuleType.REMOVE_QUERY_PARAM.equals(ruleType))) {
            throw new ProxyRewriteException("Rewrite rule must contain a replace object!");
        }

        sessionJSON.put("action", "apply_rewrite");
        sessionJSON.put("rewrite", rewriteJSON);
        serverPostHandler.postToServerWithHandling("proxy", sessionJSON, ProxyRewriteException.class);
    }

}
