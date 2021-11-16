package com.rokuality.charlesproxifier.proxy;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ProxyStartupSettings {

	private JSONObject proxyStartupJSON = null;

	public ProxyStartupSettings() {
		proxyStartupJSON = new JSONObject();
	}

	public ProxyStartupSettings addProxySettings(ProxySettings proxySettings) {
		proxyStartupJSON.put("proxy_settings", proxySettings.getSettingsAsJSON());
		return this;
	}

	public ProxyStartupSettings addSSLProxySettings(SSLProxySettings sslProxySettings) {
		proxyStartupJSON.put("ssl_proxy_settings", sslProxySettings.getSettingsAsJSON());
		return this;
	}

	public ProxyStartupSettings addRecordingSettings(RecordingSettings recordingSettings) {
		proxyStartupJSON.put("recording_settings", recordingSettings.getSettingsAsJSON());
		return this;
	}

	public ProxyStartupSettings addThrottleSettings(ThrottleSettings throttleSettings) {
		proxyStartupJSON.put("throttle_settings", throttleSettings.getSettingsAsJSON());
		return this;
	}

	public JSONObject getSettingsAsJSON() {
		return proxyStartupJSON;
	}

}
