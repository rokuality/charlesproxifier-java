package com.rokuality.test.tests;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.rokuality.charlesproxifier.enums.AutoSaveType;
import com.rokuality.charlesproxifier.enums.BlockConnectionAction;
import com.rokuality.charlesproxifier.enums.RewriteRuleType;
import com.rokuality.charlesproxifier.proxy.AllowListSettings;
import com.rokuality.charlesproxifier.proxy.AutoSaveSettings;
import com.rokuality.charlesproxifier.proxy.BlockCookiesSettings;
import com.rokuality.charlesproxifier.proxy.BlockListSettings;
import com.rokuality.charlesproxifier.proxy.DNSSpoof;
import com.rokuality.charlesproxifier.proxy.DNSSpoofingSettings;
import com.rokuality.charlesproxifier.proxy.Location;
import com.rokuality.charlesproxifier.proxy.Match;
import com.rokuality.charlesproxifier.proxy.NoCacheSettings;
import com.rokuality.charlesproxifier.proxy.Proxy;
import com.rokuality.charlesproxifier.proxy.ProxySettings;
import com.rokuality.charlesproxifier.proxy.ProxyStartupSettings;
import com.rokuality.charlesproxifier.proxy.ProxyToolStartupSettings;
import com.rokuality.charlesproxifier.proxy.RecordingSettings;
import com.rokuality.charlesproxifier.proxy.Replace;
import com.rokuality.charlesproxifier.proxy.RewriteRule;
import com.rokuality.charlesproxifier.proxy.SSLProxySettings;
import com.rokuality.charlesproxifier.proxy.ThrottleSettings;

import org.testng.annotations.*;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;

public class CharlesProxifierTests {

	private static final String SERVER_URL = "http://10.0.0.180:7777";

	Proxy proxy;

	@BeforeMethod(alwaysRun = true)
	public synchronized void beforeTest() {
		proxy = new Proxy(SERVER_URL);
	}

	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		proxy.stop();
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void basicHttpProxyTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.enableTransparentHttpProxying(true);
		proxySettings.bypassProxy(new HashSet<>(Arrays.asList("ahosttobypass.com", "anotherhosttobypass.com", "andanotherhosttobypass.com")));
		proxySettings.setHeadless(false);
		proxySettings.setCharlesBinaryLocation("/Applications/Charles.app/Contents/MacOS/Charles");

		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		proxy.start(proxyStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithRecordingSettingsTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RecordingSettings recordingSettings = new RecordingSettings();
		Location recordingLocation = new Location();
		recordingLocation.setProtocol("*");
		recordingLocation.setHost("*");
		recordingLocation.setPort("*");
		recordingLocation.setPath("*");
		recordingLocation.setQueryString("*");
		recordingSettings.include(Arrays.asList(recordingLocation));

		Location recordingExcludeLocation = new Location();
		recordingExcludeLocation.setProtocol("http");
		recordingExcludeLocation.setHost("ahostnotrecord.com");
		recordingExcludeLocation.setPort("*");
		recordingExcludeLocation.setPath("*");
		recordingExcludeLocation.setQueryString("*");
		recordingSettings.exclude(Arrays.asList(recordingExcludeLocation));

		recordingSettings.setRecordingLimit(50);
		recordingSettings.limitRecordingHistory(100);
		recordingSettings.limitWebsocketHistory(100);

		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings)
			.addRecordingSettings(recordingSettings);

		proxy.start(proxyStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyGetSessionTrafficTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		proxy.start(proxyStartupSettings);

		String sessionContent = proxy.getSession();
		HarReader harReader = new HarReader();
		Har har = null;
		try {
			har = harReader.readFromString(sessionContent);
		} catch (HarReaderException e) {
			e.printStackTrace();
		}

		List<HarEntry> harEntries = har.getLog().getEntries();
		for (HarEntry harEntry : harEntries) {
			System.out.println("Request url: " + harEntry.getRequest().getUrl());
			System.out.println("Response code: " + harEntry.getResponse().getStatus());
		}

		proxy.clearSession();
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithThrottlingSettingsTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);

		ThrottleSettings throttleSettings = new ThrottleSettings();
		throttleSettings.enableThrottling(true);
		Location throttleLocation = new Location();
		throttleLocation.setProtocol("*");
		throttleLocation.setHost("www.someaddresstothrottle.com");
		throttleLocation.setPort("*");

		throttleSettings.include(Arrays.asList(throttleLocation));
		throttleSettings.setBandwidthDownload(7425);
		throttleSettings.setBandwidthUpload(2024);
		throttleSettings.setUtilizationDownloadPercentage(100);
		throttleSettings.setUtilizationUploadPercentage(100);
		throttleSettings.setRoundTripLatency(100);
		throttleSettings.setMTU(1800);
		throttleSettings.setReliabilityPercentage(100);
		throttleSettings.setStabilityPercentage(100);
		throttleSettings.setUnstableQualityRange(80, 90);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings)
			.addThrottleSettings(throttleSettings);

		proxy.start(proxyStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithSSLTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		SSLProxySettings sslProxySettings = new SSLProxySettings();
		sslProxySettings.enableSSLProxying(true);
		Location sslIncludeLocation = new Location();
		sslIncludeLocation.setHost("hosttoinclude");
		sslIncludeLocation.setPort("*");
		sslProxySettings.include(Arrays.asList(sslIncludeLocation));

		Location sslExcludeLocation = new Location();
		sslExcludeLocation.setHost("ahosttoexcludefromssl");
		sslExcludeLocation.setPort("*");
		sslProxySettings.exclude(Arrays.asList(sslExcludeLocation));

		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings)
			.addSSLProxySettings(sslProxySettings);

		proxy.start(proxyStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithAllowListToolTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);

		AllowListSettings allowListSettings = new AllowListSettings();
		allowListSettings.enableAllowRequests(true, BlockConnectionAction.DROP_CONNECTION);
		Location allowListLocation = new Location();
		allowListLocation.setProtocol("http");
		allowListLocation.setHost("ahosttoallow.com");
		allowListLocation.setPort("*");
		allowListLocation.setPath("*");
		allowListLocation.setQueryString("*");
		allowListSettings.include(Arrays.asList(allowListLocation));
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addAllowListSettings(allowListSettings);

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithBlockListToolTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);

		BlockListSettings blockListSettings = new BlockListSettings();
		blockListSettings.enableBlockRequests(true, BlockConnectionAction.DROP_CONNECTION);
		Location blockListLocation = new Location();
		blockListLocation.setProtocol("http");
		blockListLocation.setHost("aurltoblock.com");
		blockListLocation.setPort("*");
		blockListLocation.setPath("*");
		blockListLocation.setQueryString("*");
		blockListSettings.include(Arrays.asList(blockListLocation));
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addBlockListSettings(blockListSettings);

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithBlockCookiesToolTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);

		BlockCookiesSettings blockCookiesSettings = new BlockCookiesSettings();
		blockCookiesSettings.enableBlockCookies(true);
		Location blockCookiesLocation = new Location();
		blockCookiesLocation.setProtocol("http");
		blockCookiesLocation.setHost("ahosttoblockcookies.com");
		blockCookiesLocation.setPort("*");
		blockCookiesLocation.setPath("*");
		blockCookiesLocation.setQueryString("*");
		blockCookiesSettings.include(Arrays.asList(blockCookiesLocation));
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addBlockCookiesSettings(blockCookiesSettings);

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithAutoSaveToolTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);

		AutoSaveSettings autoSaveSettings = new AutoSaveSettings();
		autoSaveSettings.enableAutoSave(true, 1, new File("~/Desktop"), AutoSaveType.HTTP_ARCHIVE);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addAutoSaveSettings(autoSaveSettings);

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithDNSSpoofToolTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);

		DNSSpoofingSettings dnsSpoofingSettings = new DNSSpoofingSettings();
		dnsSpoofingSettings.enableDNSSpoofing(true);
		DNSSpoof dnsSpoof = new DNSSpoof();
		dnsSpoof.setHostName("somehosttospoof1.com");
		dnsSpoof.setAddress("newspoofedaddress1.com");
		dnsSpoofingSettings.include(Arrays.asList(dnsSpoof));
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addDNSSpoofingSettings(dnsSpoofingSettings);

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyWithNoCacheToolTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);

		NoCacheSettings noCacheSettings = new NoCacheSettings();
		noCacheSettings.enableNoCaching(true);
		Location noCacheLocation = new Location();
		noCacheLocation.setProtocol("http");
		noCacheLocation.setHost("ahostnottocache.com");
		noCacheLocation.setPort("*");
		noCacheLocation.setPath("*");
		noCacheLocation.setQueryString("*");
		noCacheSettings.include(Arrays.asList(noCacheLocation));
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addNoCacheSettings(noCacheSettings);

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteAddHeaderTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule addHeaderRule = new RewriteRule();
		addHeaderRule.setRuleType(RewriteRuleType.ADD_HEADER);
		addHeaderRule.setName("Add a Header Rule");
		Location addHeaderLocation = new Location();
		addHeaderLocation.setProtocol("*");
		addHeaderLocation.setHost("*");
		addHeaderLocation.setPort("*");
		addHeaderLocation.setPath("*");
		addHeaderLocation.setQueryString("*");
		addHeaderRule.setLocation(addHeaderLocation);
		Match headerMatch = new Match();
		headerMatch.matchRequest(true);
		addHeaderRule.setMatch(headerMatch);
		Replace headerReplace = new Replace();
		headerReplace.setName("NewHeaderName");
		headerReplace.setValue("NewHeaderValue");
		addHeaderRule.setReplace(headerReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(addHeaderRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteModifyHeaderTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule modifyHeaderRule = new RewriteRule();
		modifyHeaderRule.setRuleType(RewriteRuleType.MODIFY_HEADER);
		modifyHeaderRule.setName("Modify a Header Rule");
		Location modifyHeaderLocation = new Location();
		modifyHeaderLocation.setProtocol("http");
		modifyHeaderLocation.setHost("*");
		modifyHeaderLocation.setPort("*");
		modifyHeaderLocation.setPath("*");
		modifyHeaderLocation.setQueryString("*");
		modifyHeaderRule.setLocation(modifyHeaderLocation);
		Match modifyHeaderMatch = new Match();
		modifyHeaderMatch.setName("HeaderToMatchName");
		modifyHeaderMatch.setValue("HeaderToMatchValue");
		modifyHeaderMatch.matchResponse(false);
		modifyHeaderMatch.matchRequest(true);
		modifyHeaderRule.setMatch(modifyHeaderMatch);
		Replace modifyHeaderReplace = new Replace();
		modifyHeaderReplace.setName("ReplacedHeaderName");
		modifyHeaderReplace.setValue("ReplacedHeaderValue");
		modifyHeaderReplace.replaceAll(true);
		modifyHeaderRule.setReplace(modifyHeaderReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(modifyHeaderRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteRemoveHeaderTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule removeHeaderRule = new RewriteRule();
		removeHeaderRule.setRuleType(RewriteRuleType.REMOVE_HEADER);
		removeHeaderRule.setName("Remove a Header Rule");
		Location removeHeaderLocation = new Location();
		removeHeaderLocation.setProtocol("http");
		removeHeaderLocation.setHost("hostwithheadertoremove");
		removeHeaderLocation.setPort("*");
		removeHeaderLocation.setPath("*");
		removeHeaderLocation.setQueryString("*");
		removeHeaderRule.setLocation(removeHeaderLocation);
		
		Match removeHeaderMatch = new Match();
		removeHeaderMatch.setName("HeaderToMatchName");
		removeHeaderMatch.setValue("HeaderToMatchValue");
		removeHeaderMatch.matchRequest(false);
		removeHeaderMatch.matchRequest(true);
		removeHeaderRule.setMatch(removeHeaderMatch);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(removeHeaderRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteModifyUrlTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule modifyURLRule = new RewriteRule();
		modifyURLRule.setRuleType(RewriteRuleType.URL);
		modifyURLRule.setName("Modify a URL Rule");
		Location modifyURLLocation = new Location();
		modifyURLLocation.setProtocol("http");
		modifyURLLocation.setHost("*");
		modifyURLLocation.setPort("*");
		modifyURLLocation.setPath("*");
		modifyURLLocation.setQueryString("*");
		modifyURLRule.setLocation(modifyURLLocation);

		Match modifyURLMatch = new Match();
		modifyURLMatch.setValue("aurltomodify");
		modifyURLRule.setMatch(modifyURLMatch);

		Replace modifyURLReplace = new Replace();
		modifyURLReplace.setValue("aurltomodifyto");
		modifyURLRule.setReplace(modifyURLReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(modifyURLRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteAddQueryParamTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule addQueryParamRule = new RewriteRule();
		addQueryParamRule.setRuleType(RewriteRuleType.ADD_QUERY_PARAM);
		addQueryParamRule.setName("Add a Query Param Rule");
		Location addQueryParamLocation = new Location();
		addQueryParamLocation.setProtocol("http");
		addQueryParamLocation.setHost("ahosttoaddqueryparam");
		addQueryParamLocation.setPort("*");
		addQueryParamLocation.setPath("*");
		addQueryParamLocation.setQueryString("*");
		addQueryParamRule.setLocation(addQueryParamLocation);

		Replace addQueryParamReplace = new Replace();
		addQueryParamReplace.setName("newqueryparamname");
		addQueryParamReplace.setValue("newqueryparamvalue");
		addQueryParamRule.setReplace(addQueryParamReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(addQueryParamRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteModifyQueryParamTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule modifyQueryParamRule = new RewriteRule();
		modifyQueryParamRule.setRuleType(RewriteRuleType.MODIFY_QUERY_PARAM);
		modifyQueryParamRule.setName("Modify a Query Param Rule");
		Location modifyQueryParamLocation = new Location();
		modifyQueryParamLocation.setProtocol("http");
		modifyQueryParamLocation.setHost("ahosttomodifyqueryparam");
		modifyQueryParamLocation.setPort("*");
		modifyQueryParamLocation.setPath("*");
		modifyQueryParamLocation.setQueryString("*");
		modifyQueryParamRule.setLocation(modifyQueryParamLocation);

		Match modifyQueryParamMatch = new Match();
		modifyQueryParamMatch.setName("nameOfParamToModify");
		modifyQueryParamMatch.setValue("valueOfParamToModify");
		modifyQueryParamRule.setMatch(modifyQueryParamMatch);
		
		Replace modifyQueryParamReplace = new Replace();
		modifyQueryParamReplace.setName("newQueryParamName");
		modifyQueryParamReplace.setValue("newQueryParamValue");
	
		modifyQueryParamRule.setReplace(modifyQueryParamReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(modifyQueryParamRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteModifyResponseStatusTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule modifyStatusRule = new RewriteRule();
		modifyStatusRule.setRuleType(RewriteRuleType.RESPONSE_STATUS);
		modifyStatusRule.setName("Modify a Response Status Rule");
		
		Location modifyStatusLocation = new Location();
		modifyStatusLocation.setProtocol("http");
		modifyStatusLocation.setHost("ahostforstatusmodification");
		modifyStatusLocation.setPort("*");
		modifyStatusLocation.setPath("*");
		modifyStatusLocation.setQueryString("*");
		modifyStatusRule.setLocation(modifyStatusLocation);

		Match modifyStatusMatch = new Match();
		modifyStatusMatch.setValue("200"); // original status code
		modifyStatusRule.setMatch(modifyStatusMatch);
		
		Replace modifyStatusReplace = new Replace();
		modifyStatusReplace.setValue("404"); // status code to replace with
		modifyStatusRule.setReplace(modifyStatusReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(modifyStatusRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	@Test(groups = { "CharlesProxyAPITests" })
	public void proxyRewriteModifyBodyTest() {

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8891);
		proxySettings.setHeadless(false);

		RewriteRule modifyBodyRule = new RewriteRule();
		modifyBodyRule.setRuleType(RewriteRuleType.BODY);
		modifyBodyRule.setName("Modify Body Rule");
		
		Location modifyBodyLocation = new Location();
		modifyBodyLocation.setProtocol("http");
		modifyBodyLocation.setHost("ahostwithbodytomodify");
		modifyBodyLocation.setPort("*");
		modifyBodyLocation.setPath("*");
		modifyBodyLocation.setQueryString("*");
		modifyBodyRule.setLocation(modifyBodyLocation);
		
		Match modifyBodyMatch = new Match();
		modifyBodyMatch.setValue("bodysnippettoreplace");
		modifyBodyMatch.matchRequest(false);
		modifyBodyMatch.matchResponse(true);
		modifyBodyRule.setMatch(modifyBodyMatch);

		Replace modifyBodyReplace = new Replace();
		modifyBodyReplace.setValue("bodysnippettoreplacewith");
		modifyBodyReplace.replaceAll(true);
		modifyBodyRule.setReplace(modifyBodyReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(modifyBodyRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

}
