package com.ptc.integrity.automation.pageobjects.pages;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Menu.
 */
public class Menu extends PageObjectBase {

    /**
     * Instantiates a new Menu.
     *
     * @param pageObjectFactory the page object factory
     */
    //
    public Menu(PageObjectFactory pageObjectFactory) {
        super(pageObjectFactory, "");
    }

    /**
     * Instantiates a new menu.
     *
     * @param pageObjectFactory the page object factory
     * @param pageName the page name
     */
    public Menu(PageObjectFactory pageObjectFactory, String pageName) {
        super(pageObjectFactory, pageName);
    }

    /** The web elmt navigate to. */
    private String webElmtNavigateTo="xpath:=//span[contains(text(),'%s')]/..";
    
    /**
     * Navigate to.
     *
     * @nx.param "MenuOption"
     * @return the boolean
     */
    public Boolean NavigateTo() {
    	return getCommonFunctions().fCommonClick(String.format(webElmtNavigateTo, getDictionary().get("MenuOption")), "Navigate to "+getDictionary().get("MenuOption"));
    }
}
