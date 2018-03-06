package com.ptc.integrity.automation.core.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The Class PageDictionary.
 */
public class PageDictionary {

	/**
	 * Instantiates a new page dictionary.
	 */
	public PageDictionary() {

	}

	/**
	 * Instantiates a new page dictionary.
	 * 
	 * @param map
	 *            the map
	 */
	public PageDictionary(Map<String, Object> map) {
		pageDictionary = map;
	}

	/** The page dictionary. */
	private Map<String, Object> pageDictionary = new HashMap<String, Object>();

	/**
	 * Gets the page dictionary.
	 * 
	 * @return the page dictionary
	 */
	public Map<String, Object> getPageDictionary() {
		return pageDictionary;
	}

	/**
	 * Sets the page dictionary.
	 * 
	 * @param pageDictionary
	 *            the page dictionary
	 */
	public void setPageDictionary(Map<String, Object> pageDictionary) {
		this.pageDictionary = pageDictionary;
	}

	/**
	 * Gets the.
	 * 
	 * @param key
	 *            the key
	 * @return the string
	 */
	public String get(final String key) {
		Object value = pageDictionary.get(key);
		if (null != value && value instanceof String) {
			return (String) value;
		}
		return null;
	}

	/**
	 * Gets the.
	 * 
	 * @param key
	 *            the key
	 * @return the string
	 */
	public String get(final int key) {
		Object value = pageDictionary.get(key);
		if (null != value && value instanceof String) {
			return (String) value;
		}
		return null;
	}

	
	/**
	 * Gets the object.
	 * 
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Object getObject(final String key) {
		Object value = pageDictionary.get(key);
		return value;
	}

	/**
	 * Gets the int.
	 * 
	 * @param key
	 *            the key
	 * @return the int
	 */
	public Integer getInt(final String key) {
		Object value = pageDictionary.get(key);
		if (null != value && value instanceof Integer) {
			return (Integer) value;
		}
		if (null != value && value instanceof String) {
			String num = (String) value;
			if (this.isInteger(num))
				return Integer.parseInt(num);
			else if (this.isDouble(num)) {
				int intValue = (int) Double.parseDouble(num);
				return intValue;
			}

		}
		return null;
	}

	/**
	 * Gets the long.
	 * 
	 * @param key
	 *            the key
	 * @return the long
	 */
	public Long getLong(final String key) {
		Object value = pageDictionary.get(key);
		if (null != value && value instanceof Long) {
			return (Long) value;
		}
		return null;
	}

	/**
	 * Gets the float.
	 *
	 * @param key the key
	 * @return the float
	 */
	public Float getFloat(final String key) {
		Object value = pageDictionary.get(key);
		if (null != value && value instanceof Float) {
			return (Float) value;
		}
		return null;
	}

	/**
	 * Put.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void put(final String key, final String value) {
		pageDictionary.put(key, value);
	}

	/**
	 * Put all.
	 * 
	 * @param dictionary
	 *            the dictionary
	 */
	public void putAll(final PageDictionary dictionary) {
		Set<String> keySet = dictionary.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			pageDictionary.put(key, dictionary.get(key));

		}
	}

	/**
	 * Put object.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void putObject(final String key, final Object value) {
		pageDictionary.put(key, value);
	}

	/**
	 * Key set.
	 * 
	 * @return the sets the
	 */
	public Set<String> keySet() {
		return pageDictionary.keySet();
	}

	/**
	 * Contains key.
	 * 
	 * @param key
	 *            the key
	 * @return true, if successful
	 */
	public boolean containsKey(Object key) {
		return pageDictionary.containsKey(key);
	}

	/**
	 * Removes the.
	 * 
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Object remove(Object key) {
		return pageDictionary.remove(key);
	}

	/**
	 * Clear.
	 */
	public void clear() {
		pageDictionary.clear();
	}

	@Override
	public String toString() {
		return "PageDictionary [pageDictionary=" + pageDictionary + "]";
	}

	private boolean isInteger(String value) {
		boolean isInteger = true;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			isInteger = false;
		}
		return isInteger;
	}

	private boolean isDouble(String value) {
		boolean isDouble = true;
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			isDouble = false;
		}
		return isDouble;
	}



	/**
	 * Gets the.
	 * 
	 * @param key
	 *            the key
	 * @return the string
	 */
	public int getSize() {
		int size = pageDictionary.size();
		
		return size;

	}
}
