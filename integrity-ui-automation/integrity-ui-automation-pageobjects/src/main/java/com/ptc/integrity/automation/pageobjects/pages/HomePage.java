package com.ptc.integrity.automation.pageobjects.pages;

import org.openqa.selenium.Keys;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

public class HomePage extends PageObjectBase {

    /**
     * Instantiates a new Home Page
     *
     * @param pageObjectFactory the page object factory
     */
    //
    public HomePage(PageObjectFactory pageObjectFactory) {
        super(pageObjectFactory, "xpath:=//*[@id='root_label-3-bounding-box']");
    }

    public HomePage(PageObjectFactory pageObjectFactory, String pageName) {
        super(pageObjectFactory, pageName);
    }

    private String edtBxItemId="xpath:=//*[@id='root_textbox-4-bounding-box']//input";
    private String btnOpen="xpath:=//*[@id='root_navigation-5-bounding-box']//button";
    
    /**
	 * Enter ItemId.
	 * @nx.param "ItemID" 
	 * @return true, if successful
	 */
    public Boolean EnterItemId() {
    	return getCommonFunctions().fCommonSetValueEditBox(edtBxItemId, "Item ID", getDictionary().get("ItemID"), "Y", "Y");
    }
    
    /**
	 * Click Open.
	 * @return true, if successful
	 */
    public Boolean ClickOpen() {
    	return getCommonFunctions().fCommonClick(btnOpen, "Open Button");
    }
    
    public Boolean ClickOpenI() {
    	getCommonFunctions().fCommonJavascriptClick(btnOpen, "");
    	return true;
    }
    
    public Boolean SelectCategory() {
    	
    	return true;
    }
//    String.format(webElmtTab, getDictionary().get("TabName")), "Open Tab"
}
