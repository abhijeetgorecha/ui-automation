package com.ptc.integrity.automation.pageobjects.objectfactory;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.ptc.integrity.automation.core.framework.PageDictionary;
import com.ptc.integrity.automation.core.framework.PageObjectFactory;
import com.ptc.integrity.automation.core.framework.Reporting;

public class IntegrityPageObjectFactory extends PageObjectFactory {

	private static IntegrityPageObjectFactory instance;

	public static IntegrityPageObjectFactory createInstance(WebDriver webDriver,
			String DT, PageDictionary GDictionary, Map<String, String> Env,
			Reporting Report) {
		instance = new IntegrityPageObjectFactory(webDriver, DT, GDictionary,
				Env, Report);
		return instance;
	}

	public static IntegrityPageObjectFactory getInstance() {
		return instance;
	}

	public IntegrityPageObjectFactory(WebDriver webDriver, String DT,
			PageDictionary GDictionary, Map<String, String> Env,
			Reporting Report) {
		super(webDriver, DT, GDictionary, Env, Report);
	}

}
