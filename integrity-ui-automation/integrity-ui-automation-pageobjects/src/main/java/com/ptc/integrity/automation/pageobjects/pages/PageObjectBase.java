package com.ptc.integrity.automation.pageobjects.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import com.ptc.integrity.automation.core.framework.CommonFunctions;
import com.ptc.integrity.automation.core.framework.PageDictionary;
import com.ptc.integrity.automation.core.framework.PageObjectFactory;
import com.ptc.integrity.automation.core.framework.Reporting;

public class PageObjectBase {
	// Instance Variables
	private Reporting Reporter;
	private WebDriver driver;
	private String driverType;
	protected PageDictionary Dictionary;
	private HashMap<String, String> Environment;
	private CommonFunctions CommonFunctions;

	private PageObjectFactory pageObjectFactory;

	protected String webElmtPageTitle = "";
	private String webElmtAjaxLoaderIcon = "classname:=nx-ajaxLoader";

	//
	
	public PageObjectBase(PageObjectFactory pageObjectFactory) {
		Reporter = pageObjectFactory.getReporter();
		driver = pageObjectFactory.getDriver();
		driverType = pageObjectFactory.getDriverType();
		Dictionary = (PageDictionary) pageObjectFactory.getDictionary();
		Environment = (HashMap<String, String>) pageObjectFactory
				.getEnvironment();
		CommonFunctions = new CommonFunctions(driver, driverType, Environment,
				Reporter);
		this.pageObjectFactory = pageObjectFactory;
	}

	public PageObjectBase(PageObjectFactory pageObjectFactory,
			String pageTitle) {
		this(pageObjectFactory);
		webElmtPageTitle = pageTitle;
	}

	public String getWebElmtPageTitle() {
		return webElmtPageTitle;
	}

	public void setWebElmtPageTitle(String webElmtPageTitle) {
		this.webElmtPageTitle = webElmtPageTitle;
	}

	public CommonFunctions getCommonFunctions() {
		return CommonFunctions;
	}

	public void setCommonFunctions(WebDriver d1) {
		CommonFunctions common = new CommonFunctions(d1, driverType,
				Environment, Reporter);
		this.CommonFunctions = common;
		Reporter.driver = d1;

	}

	public PageDictionary getDictionary() {
		String initialValue,formattedValue;
		java.util.Set<String> keySet=Dictionary.keySet();
		
		try{
		for(String key: keySet){
			if(Dictionary.get(key).contains("Date:=")){
				initialValue=Dictionary.get(key);
//				System.out.println("KEY= "+key+"VALUE1= "+initialValue);
				formattedValue=initialValue.split("Date:=")[1];
//				System.out.println("VALUE2= "+formattedValue);
				Dictionary.put(key, formattedValue);
			}
			else if(Dictionary.get(key).contains("{{CURRENTDATE}}")){
				initialValue=Dictionary.get(key);
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Date date = new Date();
				formattedValue=initialValue.replace("{{CURRENTDATE}}", dateFormat.format(date));
				Dictionary.put(key, formattedValue);
			}
			else if(Dictionary.get(key).contains("{{MONTHDATE}}")){
				initialValue=Dictionary.get(key);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
				Date date = new Date();
				formattedValue=initialValue.replace("{{MONTHDATE}}", dateFormat.format(date));
				Dictionary.put(key, formattedValue);
			}
		}
		}
		catch(Exception e){
//			System.out.println("Value of corresponding key is NOT of type String");
		}
		return Dictionary;
	}

	public Reporting getReporter() {
		return Reporter;
	}

	public boolean IsPageDisplayed() {
//		if(!getCommonFunctions().fWaitForPageToLoad())
//			return false;

		if (getCommonFunctions().fCommonCheckObjectExistance(webElmtPageTitle) == false) {
//			if (this.webElmtPageTitle.contains("Sign Out") == true) {
//				//CommonFunctions.fCommonSync(30000);
//				if (getCommonFunctions().fCommonCheckObjectExistance(
//						webElmtPageTitle) == false) {
//					Reporter.fnWriteToHtmlOutput("IsPageDisplayed",
//							webElmtPageTitle + " should be displayed",
//							webElmtPageTitle + " is not displayed", "Fail");
//					return false;
//				}
//			}
//			Reporter.fnWriteToHtmlOutput("IsPageDisplayed", webElmtPageTitle
//					+ " should be displayed", webElmtPageTitle
//					+ " is not displayed", "Fail");
//			return false;
		}
		Reporter.fnWriteToHtmlOutput("IsPageDisplayed", webElmtPageTitle
				+ " should be displayed", webElmtPageTitle + " is displayed",
				"Pass");

		try {
//			getCommonFunctions().getWaitObject(3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body//*[contains(@class,'fis-start-menu-opened')]")));
//			getCommonFunctions().fCommonClick("xpath:=//button[@ng-if='showMenuButton()']", "Menu button");
		} catch(Exception e) {}	
		
		return true;
	}

	public PageObjectFactory getPageObjectFactory() {
		return pageObjectFactory;
	}

	public HashMap<String, String> getEnvironment() {
		return Environment;
	}

	public WebDriver getDriver() {
		return driver;
	}

	// *****************************************************************************************
	// * Name : Comparescount
	// * Description : Compare count after adding/deleting
	// * Author : Abhijeet Gorecha
	// * Input Params : original and final count
	// * Return Values : Boolean - Depending on the success
	// ****************************************************************************************
	public boolean CompareCount(int originalcount, int finalcount,
			String Operation) {
		String change = "";
		if (Operation.equalsIgnoreCase("Add")) {
			change = "increased";
			if (finalcount > originalcount) {
				getReporter().fnWriteToHtmlOutput(
						"Comparecount",
						this.getClass().getSimpleName()
								+ " count should in;crease",
						this.getClass().getSimpleName() + " count increased ",
						"Pass");
				return true;
			}
		} else if (Operation.equalsIgnoreCase("Delete")) {
			change = "decreased";
			if (finalcount == originalcount - 1) {
				getReporter().fnWriteToHtmlOutput(
						"Comparecount",
						this.getClass().getSimpleName()
								+ " count should decrease by one",
						this.getClass().getSimpleName()
								+ " count decreased by one", "Pass");
				return true;
			}
		} else if (Operation.equalsIgnoreCase("Equal")) {
			if (finalcount == originalcount - 1) {
				getReporter().fnWriteToHtmlOutput(
						"Comparecount",
						this.getClass().getSimpleName()
								+ " count should be equal",
						this.getClass().getSimpleName() + " count is equal",
						"Pass");
				return true;
			} else {
				getReporter()
						.fnWriteToHtmlOutput(
								"Comparecount",
								this.getClass().getSimpleName()
										+ " count should be equal",
								this.getClass().getSimpleName()
										+ " count is not equal", "Fail");
				return false;
			}
		}
		getReporter().fnWriteToHtmlOutput(
				"Comparecount",
				this.getClass().getSimpleName() + " count should " + change
						+ " by one",
				this.getClass().getSimpleName() + " count did not " + change
						+ " by one", "Fail");
		return false;
	}

	/**
	 * Compare count.
	 * 
	 * @nx.param "initialCount", "finalCount", "operation"
	 * @return true, if successful
	 */
	public boolean CompareCount() {
		int originalcount = getDictionary().getInt("initialCount");
		int finalcount = getDictionary().getInt("finalCount");
		String Operation = getDictionary().get("operation");
		String change = "";
		if (Operation.equalsIgnoreCase("Add")) {
			change = "increased";
			if (finalcount > originalcount) {
				getReporter().fnWriteToHtmlOutput(
						"Comparecount",
						this.getClass().getSimpleName()
								+ " count should increase",
						this.getClass().getSimpleName() + " count increased ",
						"Pass");
				return true;
			}
		} else if (Operation.equalsIgnoreCase("Delete")) {
			change = "decreased";
			if (finalcount < originalcount) {
				getReporter().fnWriteToHtmlOutput(
						"Comparecount",
						this.getClass().getSimpleName()
								+ " count should decrease by one",
						this.getClass().getSimpleName()
								+ " count decreased by one", "Pass");
				return true;
			}
		}

		else if (Operation.equalsIgnoreCase("Equal")) {
			if (finalcount == originalcount) {
				getReporter().fnWriteToHtmlOutput(
						"Comparecount",
						this.getClass().getSimpleName()
								+ " count should be equal",
						this.getClass().getSimpleName() + " count is equal",
						"Pass");
				return true;
			} else {
				getReporter()
						.fnWriteToHtmlOutput(
								"Comparecount",
								this.getClass().getSimpleName()
										+ " count should be equal",
								this.getClass().getSimpleName()
										+ " count is not equal", "Fail");
				return false;
			}
		}
		getReporter().fnWriteToHtmlOutput(
				"Comparecount",
				this.getClass().getSimpleName() + " count should " + change
						+ " by one",
				this.getClass().getSimpleName() + " count did not " + change
						+ " by one (From " +originalcount+" To "+finalcount+" )", "Fail");
		return false;
	}

	/**
	 * Replace special character in web element locator
	 *
	 * @return updated locator value
	 */
	public String formatSpecialCharacter(String argument){
		return argument.replace(">", "→");
	}
	
	public boolean resizeBrowser() {
		Dimension d = new Dimension(getDictionary().getInt("NewWidth"),getDictionary().getInt("NewHeight"));
		//Resize current window to the set dimension
		        driver.manage().window().setSize(d);
		return true;
	}
}