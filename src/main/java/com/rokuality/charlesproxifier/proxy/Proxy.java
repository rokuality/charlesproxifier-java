package com.rokuality.charlesproxifier.proxy;

import com.rokuality.charlesproxifier.exceptions.ServerFailureException;
import com.rokuality.charlesproxifier.exceptions.ProxyHarException;
import com.rokuality.charlesproxifier.exceptions.ProxyNotStartedException;
import com.rokuality.charlesproxifier.httpexecutor.HttpClient;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Proxy extends BaseProxy {

	private ServerPostHandler serverPostHandler = null;
	private HttpClient httpClient = null;

	public Proxy(String serverURL) {
		httpClient = new HttpClient(serverURL);
		serverPostHandler = new ServerPostHandler(httpClient);
		super.setServerURL(serverURL);
	}

	public void start(ProxyStartupSettings proxyStartupSettings) {
		startProxy(proxyStartupSettings, null);
	}

	public void start(ProxyStartupSettings proxyStartupSettings, ProxyToolStartupSettings proxyToolStartupSettings) {
		startProxy(proxyStartupSettings, proxyToolStartupSettings);
	}

	public void stop() {
		JSONObject proxyObj = super.getProxy();
		if (proxyObj != null) {
			proxyObj.put("action", "stop");
			serverPostHandler.postToServerWithHandling("proxy", proxyObj, ServerFailureException.class);
		} else {
			throw new ProxyNotStartedException("No proxy data found! Was the proxy started?");
		}
	}

	private void startProxy(ProxyStartupSettings proxyStartupSettings, ProxyToolStartupSettings proxyToolStartupSettings) {
		JSONObject proxyJSON = new JSONObject();
		proxyJSON.put("action", "start");
		proxyJSON.put("proxy_startup_settings", proxyStartupSettings.getSettingsAsJSON());

		if (proxyToolStartupSettings != null) {
			proxyJSON.put("proxy_tool_startup_settings", proxyToolStartupSettings.getSettingsAsJSON());
		}
		
		super.setProxy(serverPostHandler.postToServerWithHandling("proxy", proxyJSON, ProxyNotStartedException.class));
	}

	public String getSession() {
		JSONObject proxyObj = super.getProxy();
		proxyObj.put("action", "get_session_content");
		JSONObject resultObj = serverPostHandler.postToServerWithHandling("proxy", proxyObj, ProxyHarException.class);
		return (String) resultObj.get("session_content");
	}

	public void clearSession() {
		JSONObject proxyObj = super.getProxy();
		proxyObj.put("action", "clear_session");
		serverPostHandler.postToServerWithHandling("proxy", proxyObj, ProxyHarException.class);
	}

}
