package cz.prague.vida.vocab;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * The Class VocabConfig.
 */
public class VocabConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int matchedWordCount = 3;

	private int colorMode = 1;

	private String pronanciationUrl;
	
	private String  pronanciationUrlCzech;

	private String proxy;

	private int proxyPort = 0;

	/**
	 * Instantiates a new vocab config.
	 *
	 * @throws IOException             Signals that an I/O exception has occurred.
	 */
	public VocabConfig() throws IOException {
		super();
		init();
	}

	/**
	 * Inits the.
	 *
	 * @throws IOException             Signals that an I/O exception has occurred.
	 */
	public void init() throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("config", "vocab-tester.properties")));
		setMatchedWordCount(properties.get("matchedWordCount"));
		setColorMode(properties.get("colorMode"));
		setPronanciationUrl(properties.getProperty("pronanciationUrl"));
		setPronanciationUrlCzech(properties.getProperty("pronanciationUrlCzech"));
		setProxy(properties.getProperty("proxy"));
		setProxyPort(properties.getProperty("proxyPort"));
	}

	/**
	 * Gets the proxy.
	 *
	 * @return the proxy
	 */
	public String getProxy() {
		return proxy;
	}

	/**
	 * Sets the proxy.
	 *
	 * @param proxy            the proxy to set
	 */
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	
	

	/**
	 * Gets the pronanciation url czech.
	 *
	 * @return the pronanciationUrlCzech
	 */
	public String getPronanciationUrlCzech() {
		return pronanciationUrlCzech;
	}

	/**
	 * Sets the pronanciation url czech.
	 *
	 * @param pronanciationUrlCzech the pronanciationUrlCzech to set
	 */
	public void setPronanciationUrlCzech(String pronanciationUrlCzech) {
		this.pronanciationUrlCzech = pronanciationUrlCzech;
	}

	/**
	 * Gets the proxy port.
	 *
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort
	 *            the proxyPort to set
	 */
	private void setProxyPort(String proxyPort) {
		if (proxyPort != null) {
			this.proxyPort = Integer.parseInt(proxyPort);	
		}
	}

	/**
	 * Sets the pronanciation url.
	 *
	 * @param pronanciationUrl            the pronanciationUrl to set
	 */
	public void setPronanciationUrl(String pronanciationUrl) {
		this.pronanciationUrl = pronanciationUrl;
	}

	/**
	 * Gets the pronanciation url.
	 *
	 * @return the pronanciationUrl
	 */
	public String getPronanciationUrl() {
		return pronanciationUrl;
	}

	private void setMatchedWordCount(Object object) {
		this.matchedWordCount = Integer.valueOf((String) object);

	}

	private void setColorMode(Object object) {
		this.colorMode = Integer.valueOf((String) object);
	}

	/**
	 * Gets the matched word count.
	 *
	 * @return the matched word count
	 */
	public int getMatchedWordCount() {
		return matchedWordCount;
	}

	/**
	 * Gets the color mode.
	 *
	 * @return the colorMode
	 */
	public int getColorMode() {
		return colorMode;
	}

	/**
	 * Checks if is standard mode.
	 *
	 * @return true, if is standard mode
	 */
	public boolean isStandardMode() {
		return colorMode == 1;
	}

	/**
	 * Checks if is simple mode.
	 *
	 * @return true, if is simple mode
	 */
	public boolean isSimpleMode() {
		return colorMode == 2;
	}

	/**
	 * Checks if is black and white mode.
	 *
	 * @return true, if is black and white mode
	 */
	public boolean isBlackAndWhiteMode() {
		return colorMode == 3;
	}
	
	/**
	 * Checks if is pronanciation ready.
	 *
	 * @return true, if is pronanciation ready
	 */
	public boolean isPronanciationReady(){
		return pronanciationUrl != null && pronanciationUrl.length() > 0;
	}

}
