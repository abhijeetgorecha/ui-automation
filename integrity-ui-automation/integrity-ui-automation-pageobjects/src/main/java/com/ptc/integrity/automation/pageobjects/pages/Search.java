package com.ptc.integrity.automation.pageobjects.pages;

import org.openqa.selenium.Keys;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Search.
 */
public class Search extends PageObjectBase {

	/**
	 * Instantiates a new Search.
	 *
	 * @param pageObjectFactory the page object factory
	 */
	//
	public Search(PageObjectFactory pageObjectFactory) {
		super(pageObjectFactory, "");
	}

	/**
	 * Instantiates a new search.
	 *
	 * @param pageObjectFactory the page object factory
	 * @param pageName the page name
	 */
	public Search(PageObjectFactory pageObjectFactory, String pageName) {
		super(pageObjectFactory, pageName);
	}

	/** The drp dwn category. */
	private String drpDwnCategory="xpath:=//*[@id='root_mashupcontainer-13_mashupcontainer-17_dhxlist-9']//div[contains(@class,'current')]";
	
	/** The drp dwn category value. */
	private String drpDwnCategoryValue="xpath:=//div[@cellvalue='%s']";
	
	/** The edt bx ID. */
	private String edtBxID="xpath:=//div[@id='root_mashupcontainer-13_mashupcontainer-17_textbox-8']//input";
	
	/** The web elmt validate property. */
	private String webElmtValidateProperty="xpath:=//b[contains(text(),'%s')]/..";

	/**
	 * Select category.
	 * @nx.param "Category"
	 * @return the boolean
	 */
	public Boolean SelectCategory() {
		if(getCommonFunctions().fCommonClick(drpDwnCategory, "Category  Dropdown")==false)
			return false;
		return getCommonFunctions().fCommonClick(String.format(drpDwnCategoryValue,getDictionary().get("Category")), "Category  Dropdown value");
	}

	/**
	 * Enter item id.
	 * 
	 *@nx.param "ItemID"
	 * @return the boolean
	 */
	public Boolean EnterItemId() {
		if(getCommonFunctions().fCommonSetValueEditBox(edtBxID, "Item ID", getDictionary().get("ItemID"), "Y", "Y")==false)
			return false;
		return getCommonFunctions().fCommonSendKeys(edtBxID, "Item ID", Keys.ENTER);
	}

	/**
	 * Validate property.
	 *
	 *@nx.param "PropertyName",  "PropertyValue"
	 * @return the boolean
	 */
	public Boolean ValidateProperty() {
		String propertyValue =getCommonFunctions().fCommonGetText(String.format(webElmtValidateProperty, getDictionary().get("PropertyName")), "Property To validate");
		if(propertyValue.contains(getDictionary().get("PropertyValue")))
		return true;
		else return false;
	}
}
