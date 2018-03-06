package com.ptc.integrity.automation.core.keydriven; 

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.reflect.ClassPath;


// TODO: Auto-generated Javadoc
/**
 * Repository Class which hold mapping of page object keyword .
 * 
 */
public class ObjectRepository {

	/** The _instance. */
	private static volatile ObjectRepository _instance = null;

	/** The config. */
	private static PropertiesConfiguration config = null;

	/**
	 * method to generate object.properties file .
	 * 
	 * @param loader
	 *            the loader
	 * @param packageName
	 *            the package name
	 */
	public static void createObjectRepositoryPropertiesFile(
			final ClassLoader loader, final String packageName) {
		BufferedWriter writer = null;
		try {
			final File fout = new File(
					"object.properties");
			final FileOutputStream fos = new FileOutputStream(fout);
			writer = new BufferedWriter(new OutputStreamWriter(fos));

			for (final ClassPath.ClassInfo info : ClassPath.from(loader)
					.getTopLevelClasses()) {
				if (info.getName().startsWith(packageName)) {
					final Class<?> clazz = info.load();
					System.out.println(clazz.getCanonicalName());
					System.out.println(clazz.getSimpleName());
					writer.write(StringUtils.lowerCase(clazz.getSimpleName())
							+ "=" + clazz.getCanonicalName());
					writer.newLine();
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * Factory Method to return instance of ObjectRepository.
	 * 
	 * @return ObjectRepository instance
	 */
	public static ObjectRepository getRepository() {
		if (_instance == null) {
			synchronized (ObjectRepository.class) {
				if (_instance == null) {
					_instance = new ObjectRepository();
					if (config == null) {
						try {
							config = new PropertiesConfiguration(
									"object.properties");
						} catch (final ConfigurationException e) {
							System.err
									.println("Configuration Exception -  Not able to locate object.properties");
							e.printStackTrace();
				}

			}
				}
			}

		}
		return _instance;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
//		createObjectRepositoryPropertiesFile(
//				TestPageObject.class.getClassLoader(),
//				"com.ptc.integrity.automation.pageobjects");
	}

	/**
	 * Instantiates a new object repository.
	 */
	private ObjectRepository() {
	}

	/**
	 * Method to return value of page object key .
	 * 
	 * @param key
	 *            Page object key
	 * @return Value mapped to a key
	 */
	public String getValue(final String key) {

		return config.getString(StringUtils.lowerCase(key));
			}

	/**
	 * Contains key.
	 * 
	 * @param key
	 *            the key
	 * @return true, if successful
	 */
	public boolean containsKey(String key) {
		return config.containsKey(StringUtils.lowerCase(key));
		}
	} 
