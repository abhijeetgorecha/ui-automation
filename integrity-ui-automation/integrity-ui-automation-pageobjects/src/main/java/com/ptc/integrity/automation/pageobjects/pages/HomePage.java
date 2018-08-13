package com.ptc.integrity.automation.pageobjects.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

public class HomePage extends PageObjectBase {

	/**
	 * Instantiates a new Home Page
	 *
	 * @param pageObjectFactory the page object factory
	 */
	//
	public HomePage(PageObjectFactory pageObjectFactory) {
		super(pageObjectFactory, "xpath:=//div[text()='Connected Requirements']");
	}

	public HomePage(PageObjectFactory pageObjectFactory, String pageName) {
		super(pageObjectFactory, pageName);
	}

	private String edtBxItemId="xpath:=//*[@id='root_textbox-4-bounding-box']//input";
	private String btnOpen="xpath:=//*[@id='root_navigation-5-bounding-box']//button";
	private String webElmtRoot="xpath:=//*[@id='c1']";
	private String webElmtAllProjects = "xpath:=//*[@role='tablist']//*[contains(text(),'All Projects')]";
	private String webElmtProjectName = "xpath:=//*[@id='datatable']//div[contains(text(),'%s')]";
	private String webElmtProjectNameToSelect = "xpath:=//*[contains(text(),'%s')]/../div";
	private String webElmtProjectDetails ="xpath:=//*[@class='fa-icon-header']";
	private String edtBxSearch ="cssSelector:=.search";

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


	/**
	 * Check scroller functionality.
	 *
	 * @return the boolean
	 */
	public Boolean CheckScrollerFunctionality() {
		WebElement Scroll=getCommonFunctions().expandRootElement(webElmtRoot);
		WebElement bkwdScroll=Scroll.findElement(By.xpath("\\iron-icon[@part=back-button]"));
		WebElement fwdScroll=Scroll.findElement(By.xpath("\\iron-icon[@part=forward-button]"));
		fwdScroll.click();
		return true;
	}
	
	/**
	 * Click all projects.
	 *
	 * @return the boolean
	 */
	public Boolean ClickAllProjects() {
		getCommonFunctions().fCommonClick(webElmtAllProjects, "All Projects");
		return true;
	}
	
	/**
	 * Click on project.
	 *
	 *@nx.param "ProjectName" 
	 * @return the boolean
	 */
	public Boolean ClickOnProject() {
		return getCommonFunctions().fCommonClick(String.format(webElmtProjectName, getDictionary().get("ProjectName")), "Open Tab");
	}
	
	/**
	 * Select project.
	 * @nx.param "ProjectName" 
	 * @return the boolean
	 */
	public Boolean SelectProject() {
		return getCommonFunctions().fCommonClick(String.format(webElmtProjectNameToSelect, getDictionary().get("ProjectName")), "Open Tab");
	}
	
	/**
	 * Click project details.
	 *
	 * @return the boolean
	 */
	public Boolean ClickProjectDetails() {
		getCommonFunctions().fCommonClick(webElmtProjectDetails, "All Projects");
		return true;
	}
	
	/**
	 * Search project.
	 *
	 * @return the boolean
	 */
	public Boolean SearchProject() {
		getCommonFunctions().AccessShadowRootElement();
		return true;
	}
}
