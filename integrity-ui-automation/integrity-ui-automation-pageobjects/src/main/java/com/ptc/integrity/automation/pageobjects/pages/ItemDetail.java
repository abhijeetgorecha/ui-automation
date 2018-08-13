package com.ptc.integrity.automation.pageobjects.pages;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

public class ItemDetail extends PageObjectBase {

	/**
	 * Instantiates a new Home Page
	 *
	 * @param pageObjectFactory the page object factory
	 */
	//
	public ItemDetail(PageObjectFactory pageObjectFactory) {
		super(pageObjectFactory, "xpath:=//div[contains(@class,'hbar project-details')]");
	}

	public ItemDetail(PageObjectFactory pageObjectFactory, String pageName) {
		super(pageObjectFactory, pageName);
	}

	private String edtBxDefect="xpath:=//*[@id='root_textbox-7-bounding-box']//input";
	private String edtBxCreatedBy="xpath:=//*[@id='root_textbox-12-bounding-box']//input";
	private String edtModifiedBy="xpath:=//*[@id='root_textbox-13-bounding-box']//input";
	private String edtBxSummary="xpath:=//*[@id='root_textbox-6-bounding-box']//input";
	private String edtBxNotes="xpath:=//*[@id='root_textarea-41-bounding-box']//textarea";
	private String webElmtTab="xpath:=//div[contains(text(),'%s')]/../..";
	private String webElmtState="xpath:=//div[@cellvalue='%s']/../../td[2]"; 
	private String webElmtProjectName="xpath:=//div[contains(@class,'project-details')]//span[contains(text(),'%s')]";
	private String webElmtStatus="xpath:=//label[contains(text(),'STATUS')]/..//label[contains(text(),'%s')]";

	/**
	 * Enter Defect.
	 * @nx.param "DefectText" 
	 * @return true, if successful
	 */
	public Boolean EnterDefect() {
		return getCommonFunctions().fCommonSetValueEditBox(edtBxDefect, "Defect Text", getDictionary().get("DefectText"), "Y", "Y");
	}

	/**
	 * Enter Created By.
	 * @nx.param "CreatedBy" 
	 * @return true, if successful
	 */
	public Boolean EnterCreatedBy() {
		return getCommonFunctions().fCommonSetValueEditBox(edtBxCreatedBy, "Created By", getDictionary().get("CreatedBy"), "Y", "Y");
	}

	/**
	 * Enter Modified By.
	 * @nx.param "ModifiedBy" 
	 * @return true, if successful
	 */
	public Boolean EnterModifiedBy() {
		return getCommonFunctions().fCommonSetValueEditBox(edtModifiedBy, "Modified By", getDictionary().get("ModifiedBy"), "Y", "Y");
	}

	/**
	 * Enter Summary.
	 * @nx.param "Summary" 
	 * @return true, if successful
	 */
	public Boolean EnterSummary() {
		return getCommonFunctions().fCommonSetValueEditBox(edtBxSummary, "Summary", getDictionary().get("Summary"), "Y", "Y");
	}

	/**
	 * Enter Notes.
	 * @nx.param "Notes" 
	 * @return true, if successful
	 */
	public Boolean EnterNotes() {
		return getCommonFunctions().fCommonSetValueEditBox(edtBxNotes, "Notes", getDictionary().get("Notes"), "Y", "Y");
	}

	/**
	 * Click on Tab.
	 * @nx.param "TabName" 
	 * @return true, if successful
	 */
	public Boolean ClickOnTab() {
		getCommonFunctions().fCommonSync(2000);
		return getCommonFunctions().fCommonClick(String.format(webElmtTab, getDictionary().get("TabName")), "Open Tab");
	}

	/**
	 * Check if Tab is selected.
	 * @nx.param "TabName" 
	 * @return true, if successful
	 */
	public Boolean CheckTabIsSelected() {
		if(getCommonFunctions().fCommonGetWebElement(String.format(webElmtTab, getDictionary().get("TabName")), "Tab "+getDictionary().get("TabName")).getAttribute("class").contains("selected")==false)
		{
			getReporter().fnWriteToHtmlOutput( "CheckTabIsSelected", getDictionary().get("TabName")+" should be selected","Not Highlighted", "Fail");
			return false;
		}
		getReporter().fnWriteToHtmlOutput( "CheckTabIsSelected", getDictionary().get("TabName")+" should be selected","Highlighted", "Pass");
		return true;
	}

	/**
	 * Check State.
	 * @nx.param "ID" 
	 * @return true, if successful
	 */
	public String CheckState() {
		return getCommonFunctions().fCommonGetWebElement(String.format(webElmtState, getDictionary().get("ID")), "State of ID").getText();
	}

	/**
	 * Compare Text.
	 * @nx.param "Actual" 
	 * @nx.param "Expected" 
	 * @return true, if successful
	 */
	public Boolean CompareText() {
		if(getDictionary().get("Actual").equalsIgnoreCase(getDictionary().get("Expected"))) {
			getReporter().fnWriteToHtmlOutput( "CompareText", "State should be "+getDictionary().get("Expected"),"State is "+getDictionary().get("Actual"), "Pass");
			return true;
		}
		getReporter().fnWriteToHtmlOutput( "CompareText", "State should be "+getDictionary().get("Expected"),"State is "+getDictionary().get("Actual"), "Fail");
		return false;
	}
	
	/**
	 * Verify project deatils.
	 *
	 * @nx.param "ProjectName" 
	 * @nx.param "Status" 
	 * @return the boolean
	 */
	public Boolean VerifyProjectDeatils() {
		if(getCommonFunctions().fCommonCheckObjectExistance(String.format(webElmtProjectName, getDictionary().get("ProjectName"))) == false)
			return false;
		if(getCommonFunctions().fCommonCheckObjectExistance(String.format(webElmtStatus, getDictionary().get("Status"))) == false)
			return false;
		return true;
	}
}
