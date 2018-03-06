package com.ptc.integrity.automation.core.keydriven;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.ptc.integrity.automation.core.framework.PageDictionary;
import com.ptc.integrity.automation.core.framework.PageObjectFactory;
import com.ptc.integrity.automation.core.framework.Reporting;
/**
 * The Class MultiBrowserProvider.
 */
public class MultiBrowserProvider {

	/** The browser provider. */
	private Map<String, PageObjectFactory> browserProvider = new HashMap<String, PageObjectFactory>();

	/** The default browser. */
	private String defaultBrowser;

	/**
	 * Open new browser and return page object factory .
	 * 
	 * @param browserId
	 *            the browser id
	 * @param objectFactory
	 *            the object factory
	 * @return the next gen page object factory
	 */
	public PageObjectFactory newBrowser(String browserId,
			PageObjectFactory objectFactory) {
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		boolean inCognitoMode = this.isInCognitoMode(browserId);
		if (inCognitoMode) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("-incognito");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		}

		WebDriver newDriver = new ChromeDriver(capabilities);
		PageObjectFactory pageObjectFactory = getIntegrityPageObjectFactory(
				newDriver, objectFactory.getDriverType(),
				objectFactory.getDictionary(), objectFactory.getEnvironment(),
				objectFactory.getReporter());
		if (inCognitoMode) {
			String id = browserId.split(":")[1];
			browserProvider.put(id, pageObjectFactory);
		} else {
			browserProvider.put(browserId, pageObjectFactory);

		}
		return pageObjectFactory;
	}

	/**
	 * Checks if browser open.
	 * 
	 * @param browserId
	 *            the browser id
	 * @return true, if browser open
	 */
	public boolean isBrowserOpen(String browserId) {
		boolean isOpen = false;
		if (this.isInCognitoMode(browserId)) {
			String id = browserId.split(":")[1];
			if (browserProvider.containsKey(id)) {
				isOpen = true;
			}
		} else {
			if (browserProvider.containsKey(browserId)) {
				isOpen = true;
			}
		}
		return isOpen;
	}

	/**
	 * Checks if is in cognito mode.
	 * 
	 * @param browserId
	 *            the browser id
	 * @return true, if is in cognito mode
	 */
	public boolean isInCognitoMode(String browserId) {
		boolean inCognito = false;
		if (null != browserId) {
			String[] split = browserId.split(":");
			if (split.length > 1 && split[0].equalsIgnoreCase("INC")) {
				inCognito = true;
			}
		}
		return inCognito;
	}

	/**
	 * Gets the browser page object factory.
	 * 
	 * @param browserId
	 *            the browser id
	 * @return the browser page object factory
	 */
	public PageObjectFactory getBrowserPageObjectFactory(String browserId) {
		if (this.isInCognitoMode(browserId)) {
			String id = browserId.split(":")[1];
			return browserProvider.get(id);
		} else {
			return browserProvider.get(browserId);
		}
	}

	/**
	 * Gets the next gen page object factory.
	 * 
	 * @param webDriver
	 *            the web driver
	 * @param driverType
	 *            the driver type
	 * @param Dictionary
	 *            the dictionary
	 * @param Env
	 *            the env
	 * @param Report
	 *            the report
	 * @return the next gen page object factory
	 */
	public PageObjectFactory getIntegrityPageObjectFactory(
			final WebDriver webDriver, final String driverType,
			final PageDictionary Dictionary, final Map<String, String> Env,
			final Reporting Report) {
		PageObjectFactory pageObjectFactory = PageObjectFactory
				.createInstance(webDriver, driverType, Dictionary, Env, Report);
		return pageObjectFactory;
	}

	/**
	 * Close the driver .
	 */
	public void close() {
		Set<Entry<String, PageObjectFactory>> pageObjectSet = browserProvider
				.entrySet();
		Iterator<Entry<String, PageObjectFactory>> iterator = pageObjectSet
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, PageObjectFactory> entry = iterator.next();
			if (!entry.getKey().equalsIgnoreCase(getDefaultBrowser()))
				entry.getValue().getDriver().quit();
		}
	}

	/**
	 * Register as default browser.
	 * 
	 * @param browserId
	 *            the browser id
	 * @param objectFactory
	 *            the object factory
	 */
	public void registerAsDefaultBrowser(String browserId,
			PageObjectFactory objectFactory) {
		browserProvider.put(browserId, objectFactory);
		defaultBrowser = browserId;
	}

	/**
	 * Checks if default browser registered.
	 * 
	 * @return true, if default browser registered
	 */
	public boolean isDefaultBrowserRegistered() {
		return defaultBrowser != null;
	}

	/**
	 * Gets the default browser object factory.
	 * 
	 * @return the default browser object factory
	 */
	public PageObjectFactory getDefaultBrowserObjectFactory() {
		return browserProvider.get(defaultBrowser);
	}

	/**
	 * Gets the default browser.
	 * 
	 * @return the default browser
	 */
	public String getDefaultBrowser() {
		return defaultBrowser;
	}

	/**
	 * Sets the default browser.
	 * 
	 * @param defaultBrowser
	 *            the new default browser
	 */
	public void setDefaultBrowser(String defaultBrowser) {
		this.defaultBrowser = defaultBrowser;
	}

}
