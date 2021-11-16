package com.rokuality.charlesproxifier.proxy;

import com.rokuality.charlesproxifier.utils.JsonUtils;

import org.json.simple.JSONObject;

public class BaseProxy {

	private JSONObject proxyJSON = null;
	private String serverURL = null;

	public BaseProxy() {

	}

	protected void setProxy(JSONObject proxy) {
		this.proxyJSON = proxy;
	}

	public JSONObject getProxy() {
		return JsonUtils.deepCopy(this.proxyJSON);
	}

	protected void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	protected String getServerURL() {
		return serverURL;
	}

}
