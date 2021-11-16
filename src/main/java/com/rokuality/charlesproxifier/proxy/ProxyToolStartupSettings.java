package com.rokuality.charlesproxifier.proxy;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ProxyToolStartupSettings {

	private JSONObject proxyToolStartupJSON = null;

	public ProxyToolStartupSettings() {
		proxyToolStartupJSON = new JSONObject();
	}

	public ProxyToolStartupSettings addNoCacheSettings(NoCacheSettings noCacheSettings) {
		proxyToolStartupJSON.put("proxy_no_cache_settings", noCacheSettings.getSettingsAsJSON());
		return this;
	}

	public ProxyToolStartupSettings addBlockCookiesSettings(BlockCookiesSettings blockCookiesSettings) {
		proxyToolStartupJSON.put("proxy_block_cookies_settings", blockCookiesSettings.getSettingsAsJSON());
		return this;
	}

	public ProxyToolStartupSettings addBlockListSettings(BlockListSettings blockListSettings) {
		proxyToolStartupJSON.put("proxy_block_list_settings", blockListSettings.getSettingsAsJSON());
		return this;
	}

	public ProxyToolStartupSettings addAllowListSettings(AllowListSettings allowListSettings) {
		proxyToolStartupJSON.put("proxy_allow_list_settings", allowListSettings.getSettingsAsJSON());
		return this;
	}

	public ProxyToolStartupSettings addDNSSpoofingSettings(DNSSpoofingSettings dnsSpoofingSettings) {
		proxyToolStartupJSON.put("proxy_dns_spoofing_settings", dnsSpoofingSettings.getSettingsAsJSON());
		return this;
	}

	public ProxyToolStartupSettings addAutoSaveSettings(AutoSaveSettings autoSaveSettings) {
		proxyToolStartupJSON.put("proxy_auto_save_settings", autoSaveSettings.getSettingsAsJSON());
		return this;
	}

	public ProxyToolStartupSettings addRewriteSettings(List<RewriteRule> rewriteRules) {
		if (rewriteRules != null && !rewriteRules.isEmpty()) {
			JSONArray rewriteArr = new JSONArray();
			for (RewriteRule rule : rewriteRules) {
				rewriteArr.add(rule.getRewriteRuleAsJSON());
			}
			proxyToolStartupJSON.put("proxy_rewrite_settings", rewriteArr);
		}
		return this;
	}

	public JSONObject getSettingsAsJSON() {
		return proxyToolStartupJSON;
	}

}
