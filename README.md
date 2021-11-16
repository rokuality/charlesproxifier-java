# Charles Proxifier - Java API

An automated library for running the popular [Charles Proxy](https://www.charlesproxy.com/) App programatically in an automated fashion. Use [Charles Proxifier](https://www.rokuality.com/charles-proxifier) to start/stop Charles Proxy, apply rewrite rules, query your application traffic, and more!

Charles Proxy is an amazing application that is incredibly popular with developers and testers for debugging and validating their application back end traffic. And now, using Charles Proxifier, you can control the proxy from inside your Java applications and tests, and use it to drive your automated tests! 

Have automated tests using Appium or Selenium and you want to validate/verify your application back end traffic? No problem! Want to run Charles Proxy headlessly from within your CICD pipelines? Charles Proxifier is your solution!

**IMPORTANT** Charles Proxifier is powered by Rokuality and is completely independent from Charles Proxy. Charles Proxifier does not hack Charles Proxy code in any way, nor does it allow you to work around the Charles Proxy [license requirements](https://www.charlesproxy.com/buy/). Charles Proxifier is an independent add on application that provides a powerful API for starting/configuring Charles Proxy at launch - empowering it to be used from within your automated testing applications. It is your responsiblity to ensure you have a valid Charles Proxy license, and that you are operating Charles within the bounds of the Charles Proxy [EULA](https://www.charlesproxy.com/buy/eula/)!

### Getting started
Before using Charles Proxifier, you must ensure you have an activated version of [Charles Proxy](https://www.charlesproxy.com/download/) installed and available on your system. Charles Proxifier uses your default activation license for Charles Proxy. Once you have an activated the Charles Proxy application, you are ready to use Charles Proxifier!

#### Download and Install Charles Proxifier
Charles Proxifier is a client webserver application for Windows and Mac that allows you to communicate and control Charles Proxy from within your network. First you must [download Charles Proxifier](https://www.rokuality.com/download-charles-proxifier) and install the webserver client in order to send api commands to the application.

#### Start the Charles Proxifier Web Server

1. Using the GUI

Once you've installed and opened Charles Proxifer, simply choose your desired port for the Charles Proxifier webserver to run on and startup the server. Once the Charles Proxifier server is listening, you can connect to it via the Charles Proxifier API from within your automated test code!

<img src="https://rokuality-public.s3.amazonaws.com/StartCharlesProxifier.png" width="348" height="248">                <img src="https://rokuality-public.s3.amazonaws.com/CharlesProxifierListening.png" width="348" height="248">

2. Using the Command Line

Optionally, you can [download](https://www.rokuality.com/download-charles-proxifier) the Charles Proxifier java client and start via the terminal (headlessly) with the `headless=true` and `port={PORT}` properties as follows:

```xml
    java -Dheadless=true -Dport=8777 -jar /path/to/CharlesProxifier.jar
```

With your server operational and listening for commands, you are now ready to use the Charles Proxifier API to send automated commands to your proxy!

### Getting the API Jar

1. Download the latest [Charles Proxifier API Jar](https://github.com/rokuality/charlesproxifier-java/releases) from github.
2. Add the jar library to your Java project. There are many ways to do this but an easy way is to use `maven` and import the jar file using the [maven-install-plugin](https://maven.apache.org/plugins/maven-install-plugin/) as follows:


```xml
    mvn install:install-file -Dfile=/path/to/where/you/downloaded/charlesproxifier.jar -DgroupId=com.rokuality -DartifactId=charles-proxifier-java -Dversion=1.0.0 -Dpackaging=jar
```

3. Finally, add the dependency to your project. Again, multiple ways of accomplishing this but if you're using maven, the dependency can be added to your .pom as follows:

```xml
    <dependency>
        <groupId>com.rokuality</groupId>
        <artifactId>charles-proxifier-java</artifactId>
        <version>1.0.0</version>
    </dependency>
```

Once the java api is installed in your java project, you're ready to start leveraging it to control Charles Proxy instances! Keep reading for explicit examples and instructions. But also please checkout the [sample tests](https://github.com/rokuality/charlesproxifier-java/blob/main/src/test/java/com/rokuality/test/tests/CharlesProxifierTests.java) for additional examples.

### Starting a Proxy

Charles Proxifier communicates and controls Charles Proxy via a lightweight webserver listening for traffic on the machine that has Charles installed. As such, you can initiate and control Charles from any machine on your network.

To start a proxy, you must first create a `ProxySettings` object and pass that to your `Proxy` object as follows:

```Java
    // initiate a proxy settings object
    ProxySettings proxySettings = new ProxySettings();

    // set the unique port you want your proxy to listen on
    proxySettings.setPort(8891);
	
    // set if you want to run the proxy headlessly or not
    proxySettings.setHeadless(false);

    // create a proxy startup settings object and assign your proxy settings object
    ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
    proxyStartupSettings.addProxySettings(proxySettings);

    // create a proxy object pointing to your running Charles Proxifier url and start your proxy
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings);

```

When the above code executes, a new Charles Proxy instance will be started and listening for traffic on port 8891. You can have multiple concurrent proxies running on the same machine, provided you give each proxy instance a unique port. Note that if running on Windows, please see the [concurrent instance](https://www.charlesproxy.com/documentation/faqs/running-multiple-instances-of-charles/) instructions for setting up Charles Proxy to run multiple instances safely.

### Finding Charles Proxy
Charles Proxifier will attempt to find your installed Charles Proxy binary in the default installation locations on your Mac or Windows machine. But if for whatever reason, it can't be found - or if you've installed Charles Proxy in a custom location, you can specify the path to your Charles Proxy binary locations in your proxy settings object as follows:

```java
    // mac os example
    proxySettings.setCharlesBinaryLocation("/path/to/where/you/installed/charles/Charles.app/Contents/MacOS/Charles");
```

### Stopping a Proxy
When you're finished with your proxy, you can teardown the running Charles Proxy instance by calling `stop` as follows:

```java
    // stop your proxy
    proxy.stop();
```

### Recording Traffic Settings
By default, when you start a proxy instance via Charles Proxifier, all traffic will be recorded in the session. But if you wish to limit the recorded traffic, you can provide a `RecordingSettings` objects with relevant locations to include/exclude as well as additional [Charles Proxy recording settings](https://www.charlesproxy.com/documentation/using-charles/recording/) which can then be passed to your `ProxyStartupSettings` object on proxy start.

```java
    // initiate your recording settings object
    RecordingSettings recordingSettings = new RecordingSettings();

    // set a location object for a url you wish to include. Wild cards supports in the same fashion as charles proxy
    Location recordingLocation = new Location();
    recordingLocation.setProtocol("http");
    recordingLocation.setHost("ahosttorecord");
    recordingLocation.setPort("*");
    recordingLocation.setPath("*");
    recordingLocation.setQueryString("*");

    // include the list of all of your location objects
    recordingSettings.include(Arrays.asList(recordingLocation));

    // set the recording limit size
    recordingSettings.setRecordingLimit(50);

    // set the recording http and websocket request limits
    recordingSettings.limitRecordingHistory(100);
    recordingSettings.limitWebsocketHistory(100);

    // add the recording settings to your proxy startup settings object
    ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
    proxyStartupSettings.addProxySettings(your_proxy_settings_object)
        .addRecordingSettings(recordingSettings)

    // create a proxy object pointing to your running Charles Proxifier url and start your proxy
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings);
```

### Querying Your Proxy Traffic During Test
Charles Proxifier allows you to programaticaly query your device traffic during test by returning your currently recorded traffic in a har object which allows you to verify every facet of your application's back end traffic. We also include the very helpful and powerful [har-reader](https://github.com/sdstoehr/har-reader) library as a dependency to verify your application traffic as a returned object as follows:

```java
    // get your session entries as a string in har format from a running proxy
    String sessionContent = proxy.getSession();

    // construct a har reader object and ready in the har content from charles proxifier
    HarReader harReader = new HarReader();
    Har har = harReader.readFromString(sessionContent);

    // convert to a list of all har entries and query your back end traffic or assert/fail as desired
    List<HarEntry> harEntries = har.getLog().getEntries();
    for (HarEntry harEntry : harEntries) {
        System.out.println("Request url: " + harEntry.getRequest().getUrl());
        System.out.println("Response code: " + harEntry.getResponse().getStatus());
    }
```

Note that by default, all session entries will be returned by your `getSession` call from the time the proxy started until call. If you wish at any point to clear your session log you can do so by calling `clearSession` as follows:

```java
    // clears any recorded session entries
    proxy.clearSession();
```

### SSL Traffic
By default, Charles Proxifier will use your Charles Proxy default ssl setup and root ssl certificate. You should follow the initial [SSL Proxying](https://www.charlesproxy.com/documentation/proxying/ssl-proxying/) instructions from Charles Proxy in order to establish a succesful man in the middle https proxy to decrypt your https traffic during test.

Once you've setup your browsers/devices under test to use the [Charles Proxy Root Certificate](https://www.charlesproxy.com/documentation/using-charles/ssl-certificates/), you can enable and configure ssl recording in Charles Proxifier by creating a `SSLProxySettings` object including your urls to include/exclude for ssl handling during test. Note that by defaul, if you don't pass a list of urls to include, then all url locations will be decrypted for ssl handling. Also note that SSL proxy handling is disabled by default on proxy start and must be enabled as follows:

```java
    // initiate the ssl proxy settings object
    SSLProxySettings sslProxySettings = new SSLProxySettings();

    // enable ssl proxying
    sslProxySettings.enableSSLProxying(true);

    // create location objects for any/all locations you wish to descrypt ssl
    // note by default all locations are included if no locations are passed
    Location sslIncludeLocation = new Location();
    sslIncludeLocation.setHost("*");
    sslIncludeLocation.setPort("*");
    sslProxySettings.include(Arrays.asList(sslIncludeLocation));

    // create a proxy startup object and pass your proxy settings object and your ssl proxy setting object
    ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
    proxyStartupSettings.addProxySettings(proxySettings)
        .addSSLProxySettings(sslProxySettings);

    // start your proxy
    proxy.start(proxyStartupSettings);
```

### Throttling Traffic
Proxy traffic throttling can be setup on proxy start by applying a `ThrottleSettings` object on proxy startup.

```java
    // initiate your throttle settings object and enable throttling
    ThrottleSettings throttleSettings = new ThrottleSettings();
    throttleSettings.enableThrottling(true);

    // create a list of location objects identifying the locations to throttle traffic
    Location throttleLocation = new Location();
    throttleLocation.setProtocol("*");
    throttleLocation.setHost("www.someaddresstothrottle.com");
    throttleLocation.setPort("*");
    throttleSettings.include(Arrays.asList(throttleLocation));

    // set your desired throttle traffic settings
    throttleSettings.setBandwidthDownload(7425);
    throttleSettings.setBandwidthUpload(2024);
    throttleSettings.setUtilizationDownloadPercentage(100);
    throttleSettings.setUtilizationUploadPercentage(100);
    throttleSettings.setRoundTripLatency(100);
    throttleSettings.setMTU(1800);
    throttleSettings.setReliabilityPercentage(100);
    throttleSettings.setStabilityPercentage(100);
    throttleSettings.setUnstableQualityRange(80, 90);
		
    // create a proxy startup settings object and pass the throttle settings object
    ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
    proxyStartupSettings.addProxySettings(proxySettings)
        .addThrottleSettings(throttleSettings);

    // start your proxy
    proxy.start(proxyStartupSettings);
```

### Proxy Rewrite Rules
Charles Proxifier supports all of the [Charles Proxy Rewrite Rules](https://www.charlesproxy.com/documentation/tools/rewrite/) which can be applied to your proxy at start by constructing `RewriteRule` objects which consist of different `Match` and `Replace` objects identifying the behavior of your rewrites on startup. The rewrite rules can then be applied to a `ProxyToolStartupSettings` object which is passed to the proxy object on startup. Below is an example of adding a header using a rewrite tool, but please see the [example tests](https://github.com/rokuality/charlesproxifier-java/blob/main/src/test/java/com/rokuality/test/tests/CharlesProxifierTests.java) for a complete list of all rewrite examples

```java
    // initiate your rewrite rule object
    RewriteRule addHeaderRule = new RewriteRule();

    // set the rule type
    addHeaderRule.setRuleType(RewriteRuleType.ADD_HEADER);

    // set a unique name for your rule
    addHeaderRule.setName("Add a Header Rule");

    // set a list of locations of where the rewrite rule will be applied
    Location addHeaderLocation = new Location();
    addHeaderLocation.setProtocol("*");
    addHeaderLocation.setHost("*");
    addHeaderLocation.setPort("*");
    addHeaderLocation.setPath("*");
    addHeaderLocation.setQueryString("*");
    addHeaderRule.setLocation(addHeaderLocation);

    // create a match object
    Match headerMatch = new Match();
    headerMatch.matchRequest(true);
    addHeaderRule.setMatch(headerMatch);

    // create a replace object with the header name/value to add during test
    Replace headerReplace = new Replace();
    headerReplace.setName("CustomHeader");
    headerReplace.setValue("CustomHeaderValue");
    addHeaderRule.setReplace(headerReplace);

    // create a proxy tool startup settings object and add the add header rule to it
    ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
    proxyToolStartupSettings.addRewriteSettings(Arrays.asList(addHeaderRule))

    // pass the proxy tool startup setting object to your proxy and start
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings, proxyToolStartupSettings);
```

### Setting the Allow List
Setting a list of allowed urls is supported by applying a `AllowListSettings` objects on proxy startup as follows:

```java
    // initiate the allow list and set the desired drop action
    AllowListSettings allowListSettings = new AllowListSettings();
    allowListSettings.enableAllowRequests(true, BlockConnectionAction.DROP_CONNECTION);

    // construct your allow list location(s) object of urls to allow
    Location allowListLocation = new Location();
    allowListLocation.setProtocol("http");
    allowListLocation.setHost("someurlforallow.com");
    allowListLocation.setPort("*");
    allowListLocation.setPath("*");
    allowListLocation.setQueryString("*");
    allowListSettings.include(Arrays.asList(allowListLocation));

    // create a proxy tool startup settings object and add the allow list settings to it
    ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
    proxyToolStartupSettings.addAllowListSettings(allowListSettings);

    // pass the proxy tool startup setting object to your proxy and start
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings, proxyToolStartupSettings);
```

### Setting the Block List
Setting a list of [blocked/blacklists urls](https://www.charlesproxy.com/documentation/tools/block-list/) is supported by applying a `BlockListSettings` objects on proxy startup as follows:

```Java
    // initiate the block list and set the desired drop action
    BlockListSettings blockListSettings = new BlockListSettings();
    blockListSettings.enableBlockRequests(true, BlockConnectionAction.DROP_CONNECTION);

    // construct your block list location(s) object of urls to block
    Location blockListLocation = new Location();
    blockListLocation.setProtocol("http");
    blockListLocation.setHost("aurltoblock.com");
    blockListLocation.setPort("*");
    blockListLocation.setPath("*");
    blockListLocation.setQueryString("*");
    blockListSettings.include(Arrays.asList(blockListLocation));

    // create a proxy tool startup settings object and add the block list settings to it
    ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
    proxyToolStartupSettings.addBlockListSettings(blockListSettings);

    // pass the proxy tool startup setting object to your proxy and start
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings, proxyToolStartupSettings);
```

### Blocking Cookies
[Blocking cookies](https://www.charlesproxy.com/documentation/tools/block-cookies/) is supported by applying a `BlockCookiesSettings` objects on proxy startup as follows:

```java
    // initiate the block cookies settings object
    BlockCookiesSettings blockCookiesSettings = new BlockCookiesSettings();
    blockCookiesSettings.enableBlockCookies(true);

    // construct your block cookies location(s) object of urls to block
    Location blockCookiesLocation = new Location();
    blockCookiesLocation.setProtocol("http");
    blockCookiesLocation.setHost("aurltoblockcookies.com");
    blockCookiesLocation.setPort("*");
    blockCookiesLocation.setPath("*");
    blockCookiesLocation.setQueryString("*");
    blockCookiesSettings.include(Arrays.asList(blockCookiesLocation));

    // create a proxy tool startup settings object and add the block cookies settings to it
    ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
    proxyToolStartupSettings.addBlockCookiesSettings(blockCookiesSettings);

    // pass the proxy tool startup setting object to your proxy and start
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings, proxyToolStartupSettings);
```

### Enabling Auto Save
Setting up [auto save](https://www.charlesproxy.com/documentation/tools/auto-save/) is supported by applying a `AutoSaveSettings` objects on proxy startup as follows:

```java
    // initiate the auto save object, set the interval in minutes, set the directory to save to
    // and set the desired format to save in
    AutoSaveSettings autoSaveSettings = new AutoSaveSettings();
    autoSaveSettings.enableAutoSave(true, 1, new File("/directory/to/save/to"), AutoSaveType.HTTP_ARCHIVE);

    // create a proxy tool startup settings object and add the auto save settings to it
    ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
    proxyToolStartupSettings.addAutoSaveSettings(autoSaveSettings);

    // pass the proxy tool startup setting object to your proxy and start
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings, proxyToolStartupSettings);
```

### Enabling DNS Spoofing
Setting up [DNS Spoofing](https://www.charlesproxy.com/documentation/tools/dns-spoofing/) is supported by applying a `DNSSpoofingSettings` objects on proxy startup as follows:

```java
    // initiate the dns spoofing object and enable
    DNSSpoofingSettings dnsSpoofingSettings = new DNSSpoofingSettings();
    dnsSpoofingSettings.enableDNSSpoofing(true);

    // create a list of dns spoof objects including the host name and the address to spoof to
    DNSSpoof dnsSpoof = new DNSSpoof();
    dnsSpoof.setHostName("somehosttospoof1.com");
    dnsSpoof.setAddress("newspoofedaddress1.com");
    dnsSpoofingSettings.include(Arrays.asList(dnsSpoof));

    // create a proxy tool startup settings object and add the dns spoofing settings to it
    ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
    proxyToolStartupSettings.addDNSSpoofingSettings(dnsSpoofSettings);

    // pass the proxy tool startup setting object to your proxy and start
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings, proxyToolStartupSettings);
```

### Enabling No Caching
[Cache blocking](https://www.charlesproxy.com/documentation/tools/no-caching/) is supported by applying a `NoCacheSettings` objects on proxy startup as follows:

```java
    // initiate the no cache object and enable
    NoCacheSettings noCacheSettings = new NoCacheSettings();
    noCacheSettings.enableNoCaching(true);

    // set a list of no cache locations where caching will not be allowed
    Location noCacheLocation = new Location();
    noCacheLocation.setProtocol("http");
    noCacheLocation.setHost("aurlnottocache.com");
    noCacheLocation.setPort("*");
    noCacheLocation.setPath("*");
    noCacheLocation.setQueryString("*");
    noCacheSettings.include(Arrays.asList(noCacheLocation));

    // create a proxy tool startup settings object and add the no cache settings to it
    ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
    proxyToolStartupSettings.addDNSSpoofingSettings(noCacheSettings);

    // pass the proxy tool startup setting object to your proxy and start
    Proxy proxy = new Proxy("http://localhost:{your-running-charles-proxifier-port");
    proxy.start(proxyStartupSettings, proxyToolStartupSettings);
```

