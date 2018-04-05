package com.ptc.integrity.automation.pageobjects.pages;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

public class ItemsView extends PageObjectBase {

    /**
     * Instantiates a new Items View
     *
     * @param pageObjectFactory the page object factory
     */
    //
    public ItemsView(PageObjectFactory pageObjectFactory) {
        super(pageObjectFactory, "xpath:=//*[@id='root_mashupcontainer-10_image-95']");
    }

    public ItemsView(PageObjectFactory pageObjectFactory, String pageName) {
        super(pageObjectFactory, pageName);
    }

    private String drpDwnCategory="xpath:=//*[@id='root_mashupcontainer-13_mashupcontainer-17_dhxlist-33']//div[contains(@class,'current')]";
    private String drpDwnCategoryValue="xpath:=//div[@cellvalue='%s']";
    private String webElmtID="xpath:=//span[contains(text(),'%s')]/../..//span[contains(text(),'%2$s')]/../..//td/span";
    private String btnListView="xpath:=//div[@id='root_mashupcontainer-13_mashupcontainer-17_button-30']//button/span";
    private String webElmtDetailView="xpath:=//*[@id='root_mashupcontainer-13_mashupcontainer-17_details-23-bounding-box']//span[contains(text(),'%s')]";
  
    /**
	 * Select Category.
	 * @nx.param "Category" 
	 * @return true, if successful
	 */
    public Boolean SelectCategory() {
    	if(getCommonFunctions().fCommonClick(drpDwnCategory, "Category  Dropdown")==false)
    		return false;
    	return getCommonFunctions().fCommonClick(String.format(drpDwnCategoryValue,getDictionary().get("Category")), "Category  Dropdown value");
    }
    
    /**
     * Gets the id.
     *
     * @nx.param "Date", "State"
     * @return the id
     */
    public String getID() {
    	if(getCommonFunctions().fCommonCheckObjectExistance(String.format(webElmtID, getDictionary().get("Date"),getDictionary().get("State")))==false)
    		return null;
    	return getCommonFunctions().fCommonGetText(String.format(webElmtID, getDictionary().get("Date"),getDictionary().get("State")), "ID");
    }
    
    /**
     * Select ID.
     *
     *@nx.param "Date", "State"
     * @return true, if successful
     */
    public Boolean SelectID() {
    	getCommonFunctions().fCommonSync(2000);
    	return getCommonFunctions().fCommonClick(String.format(webElmtID, getDictionary().get("Date"),getDictionary().get("State")), "ID to select");
    }
    
    /**
     * Toggle list view.
     *
     * @return the boolean
     */
    public Boolean ToggleListView() {
    	getCommonFunctions().fCommonSync(2000);
    	return getCommonFunctions().fCommonClick(btnListView, "Toggle list view");
    }
    
    /**
     * Check detail view visible or not.
     *
     * @return the boolean
     */
    public Boolean IsDetailViewVisible() {
    	getCommonFunctions().fCommonCheckObjectExistance(String.format(webElmtDetailView, getDictionary().get("ValueToValidate")));
    	return true;
    }


}
