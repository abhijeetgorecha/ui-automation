package com.ptc.integrity.automation.core.framework;

import java.util.Iterator;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class ConfigurationManager {

	private static ConfigurationManager instance = null;
	private static CompositeConfiguration config = null;
	private String configPath = "integrity-ui-automation.properties";

	private ConfigurationManager() {

	}

	/*
	 * method to return instance of ConfigurationManager
	 */
	public static ConfigurationManager getInstance() {
		if (instance == null) {
			instance = new ConfigurationManager();
		}
		return instance;
	}

	/*
	 * return config
	 */
	public Configuration getConfig() {
		if (config == null) {
			try {
				// composite config for custom and system properties .
				config = new CompositeConfiguration();
				config.addConfiguration(new SystemConfiguration());
				config.addConfiguration(new PropertiesConfiguration(configPath));
				this.separatorsToSystem(config);

			} catch (ConfigurationException e) {
				System.err
						.println("Configuration Exception -  Not able to locate integrity-ui-automation.properties");
				e.printStackTrace();
			}

		}
		return config;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	private void separatorsToSystem(Configuration config) {

		Iterator<String> it = (Iterator<String>) config.getKeys();
		while (it.hasNext()) {
			String key = it.next();
			if (StringUtils.startsWith(key, "ui.automation")) {
				config.setProperty(key,
						FilenameUtils.separatorsToSystem(config.getString(key)));
			}

		}

	}
}
