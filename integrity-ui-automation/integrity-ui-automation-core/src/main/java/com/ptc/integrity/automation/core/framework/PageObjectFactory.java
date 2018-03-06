package com.ptc.integrity.automation.core.framework;

import java.util.Map;

import org.openqa.selenium.WebDriver;

public class PageObjectFactory {
	private WebDriver driver;
	private String driverType;
	private Reporting Reporter;
	private PageDictionary Dictionary;
	private Map<String, String> Environment;

	private static PageObjectFactory instance;

	public static PageObjectFactory createInstance(WebDriver webDriver,
			String DT, PageDictionary GDictionary,
			Map<String, String> Env, Reporting Report) {
		instance = new PageObjectFactory(webDriver, DT, GDictionary, Env,
				Report);
		return instance;
	}

	public static PageObjectFactory getInstance() {
		return instance;
	}

	public PageObjectFactory(WebDriver webDriver, String DT,
			PageDictionary GDictionary, Map<String, String> Env,
			Reporting Report) {
		driver = webDriver;
		driverType = DT;
		Dictionary = GDictionary;
		Environment = Env;
		Reporter = Report;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public String getDriverType() {
		return driverType;
	}

	public Reporting getReporter() {
		return Reporter;
	}

	public PageDictionary getDictionary() {
		return Dictionary;
	}

	public Map<String, String> getEnvironment() {
		return Environment;
	}

	

}
