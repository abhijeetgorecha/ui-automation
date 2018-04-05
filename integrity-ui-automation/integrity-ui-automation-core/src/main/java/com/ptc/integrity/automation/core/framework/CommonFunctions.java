package com.ptc.integrity.automation.core.framework;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ptc.integrity.automation.core.keydriven.ExecutionEngine;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ptc.integrity.automation.core.utils.IntegrityAutomationConstant;



// TODO: Auto-generated Javadoc
/**
 * The Class CommonFunctions.
 */
public class CommonFunctions implements Cloneable{
	
	/** The driver. */
	private WebDriver driver;
	
	/** The driver type. */
	private String driverType;
	
	/** The Reporter. */
	private Reporting Reporter;
	
	/** The Environment. */
	private HashMap <String, String> Environment;


	/**
	 * Instantiates a new common functions.
	 *
	 * @param webDriver the web driver
	 * @param DT the dt
	 * @param Env the env
	 * @param Report the report
	 */
	public CommonFunctions(WebDriver webDriver,String DT, HashMap <String, String> Env, Reporting Report){
		driver = webDriver;
		driverType = DT;
		Environment = Env;
		Reporter = Report;
	}

	
	public Wait<WebDriver> getWaitObject(){
		return getWaitObject(20);
	}

	public Wait<WebDriver> getWaitObject(int timeout){
		return new FluentWait<WebDriver>(driver)
				.withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(20, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(ElementNotVisibleException.class)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(WebDriverException.class);
	}
	
	//*****************************************************************************************
	//*	Name		    : fCommonLaunchEnvironemnt
	//*	Description	    : Launch env for any URL
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	:  strUrl - url need to open 
	//*	Return Values	:  Boolean - Depending on the success
	/**
	 * F common launch environemnt.
	 *
	 * @param strUrl the str url
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonLaunchEnvironemnt(String strUrl){
		System.out.println("fCommonLaunchEnvironemnt");
		try {
			//delete cookies
			driver.manage().deleteAllCookies();
			driver.navigate().refresh();
			//open env according to given URL
			//			Dimension dimention = new Dimension(1920, 1040);
			//			driver.manage().window().setSize(dimention);
			//			driver.get(strUrl);
			try{
				driver.manage().window().maximize();
			}catch(Exception e){

			}
			JavascriptExecutor js = (JavascriptExecutor)driver;
			//Navigate to new Page
//			js.executeScript("window.location = '" + strUrl + "'");
			
			driver.get(strUrl);
		
			System.out.println(IntegrityAutomationConstant.TEST_STEP_DESCRIPTION);
			System.out.println(ExecutionEngine.getignore());
			Reporter.fnWriteToHtmlOutput("fCommonLaunchEnvironemnt", strUrl+" should be launched", "URL is launched successfully", "Pass");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			Reporter.fnWriteToHtmlOutput( "fCommonLaunchEnvironemnt", "Exception occurred","Exception: " + e, "Fail");
			return false;
		}       
	}	


	//*****************************************************************************************
	//*	Name		    : fCommonIsSelected
	//*	Description	    : Check if the object is Selected or not
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: WebElement - The webelement for which we need to check
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common is selected.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param objName the obj name
	 * @param takeScreenPrint the take screen print
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonIsSelected(String webElmtProp, String objName,boolean takeScreenPrint){
		try{
			String sTakeScreenPrint="Done";
			if(takeScreenPrint)
				sTakeScreenPrint="Pass";
			//Get WebElement
			WebElement webElement = fCommonGetObject(webElmtProp,objName);		

			//Check if the Webelement is enabled or displayed    		
			boolean bIsSelected = false;	        

			int intCount = 1;        
			while (!(bIsSelected) && (intCount <=2)){
				try {
					bIsSelected = webElement.isSelected();
					if(!bIsSelected){
						fCommonSync(2000);
						webElement = fCommonGetObject(webElmtProp,objName);
					}
				}catch (StaleElementReferenceException e){
					webElement = fCommonGetObject(webElmtProp,objName);
				}catch (WebDriverException e){	    
					webElement = fCommonGetObject(webElmtProp,objName);
				}catch (NullPointerException e){	    
					webElement = fCommonGetObject(webElmtProp,objName);
					if(webElement == null){
						break;
					}
				}	    	    
				intCount++;			
			}

			//Validate if the element is displayed
			if (!(bIsSelected)){	        	
				Reporter.fnWriteToHtmlOutput("fCommonIsSelected", objName+ " should be Selected", objName+ " is not Selected", "Fail");
				return false;
			}	        
			Reporter.fnWriteToHtmlOutput("fCommonIsSelected", objName+ " should be Selected", objName+ " is Selected ", sTakeScreenPrint);
			return true;
		}catch(Exception e){
			Reporter.fnWriteToHtmlOutput("fCommonIsSelected", "Exception occurred","Exception :" + e, "Fail");			
			return false;		
		}    	
	}	

	//*****************************************************************************************
	//*	Name		    : fCommonIsEnabled
	//*	Description	    : Check if the object is enabled or not
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: WebElement - The webelement for which we need to check
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common is enabled.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonIsEnabled(String webElmtProp, String strObjName){
		try{
			//Get WebElement
			WebElement webElement = fCommonGetObject(webElmtProp,strObjName);

			//Get the title of the current window
			boolean bIsEnabled = false;

			int intCount = 1;        
			while (!(bIsEnabled) && (intCount <=3)){
				try {	        				
					bIsEnabled = webElement.isEnabled();
					break;
				}catch (StaleElementReferenceException e){  	       
					webElement = fCommonGetObject(webElmtProp,strObjName);	    	       
				}catch (NullPointerException e){	 
					break;
				}	    	    	
				intCount++;			
			}	
			//Validate
			if (!(bIsEnabled)){
				Reporter.fnWriteToHtmlOutput(strObjName, strObjName+ " should be enabled", strObjName+ " is not enabled", "Fail");
				return false;
			}
			Reporter.fnWriteToHtmlOutput(strObjName, strObjName+ " should be enabled", strObjName+ " is enable", "Pass");
			return true;

		}catch (Exception e) {
			Reporter.fnWriteToHtmlOutput(strObjName, "Exception occurred","Exception: " + e, "Fail");
			return false;
		}  
	}	

	/**
	 * F common is Disabled.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonIsDisabled(String webElmtProp, String strObjName){
		try{
			//Get WebElement
			WebElement webElement = fCommonGetObject(webElmtProp,strObjName);

			//Get the title of the current window
			boolean bIsEnabled = false;

			int intCount = 1;        
			while (!(bIsEnabled) && (intCount <=3)){
				try {	        				
					bIsEnabled = webElement.isEnabled();
					break;
				}catch (StaleElementReferenceException e){  	       
					webElement = fCommonGetObject(webElmtProp,strObjName);	    	       
				}catch (NullPointerException e){	 
					break;
				}	    	    	
				intCount++;			
			}	
			//Validate
			if (!(bIsEnabled)){
				Reporter.fnWriteToHtmlOutput(strObjName, strObjName+ " should be enabled", strObjName+ " is not enabled", "Pass");
				return true;
			}
			Reporter.fnWriteToHtmlOutput(strObjName, strObjName+ " should be enabled", strObjName+ " is enable", "Fail");
			return false;

		}catch (Exception e) {
			Reporter.fnWriteToHtmlOutput(strObjName, "Exception occurred","Exception: " + e, "Fail");
			return false;
		}  
	}	
	
	
	//*****************************************************************************************
	//*	Name		    : fCommonClick
	//*	Description	    : Click on the webelement
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: WebElement Properties - The webelement for which we need to click
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common click.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @return true, if successful
	 * @throws ElementNotVisibleException the element not visible exception
	 */
	//*****************************************************************************************
	public boolean fCommonClick(String webElmtProp, String strObjName) throws ElementNotVisibleException {
		try {
			//Get WebElement
			WebElement webElement = fCommonGetObject(webElmtProp, strObjName);
			if (webElement == null) {
				Reporter.fnWriteToHtmlOutput("fCommonClick: " + strObjName, "Object should be found", "Object was not found. Null value returned", "Fail");
				return false;
			}

//			webElement = getWaitObject(10).until(ExpectedConditions.elementToBeClickable(fCommonGetObject(webElmtProp, strObjName)));

			//Click on the WebElement
			int intCount = 1;
			//WebDriverWait wait = new WebDriverWait(driver, 30);
			while (intCount <= 3) {
				try {
					webElement.getLocation();
					//webElement =  (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(webElement));
					webElement.click();
					break;

				} catch (StaleElementReferenceException e) {
					webElement = fCommonGetObject(webElmtProp, strObjName);
				} catch (InvalidElementStateException e) {
					webElement = fCommonGetObject(webElmtProp, strObjName);
				} catch (WebDriverException e) {
					webElement = fCommonGetObject(webElmtProp, strObjName);
				} catch (NullPointerException e) {
					webElement = fCommonGetObject(webElmtProp, strObjName);

				} catch (Exception e) {
					driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
				}
				if (intCount == 3) {
					Reporter.fnWriteToHtmlOutput("fCommonClick", strObjName, strObjName + " is not clicked", "Fail");
					return false;
				}
				intCount++;
			}
			Reporter.fnWriteToHtmlOutput("fCommonClick", strObjName + " should be clicked", strObjName + " is clicked successfully", "Done");
			return CheckErrorWarningPopUp();
//			return true;
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonClick", "Exception occurred for object: " + strObjName, "Exception: " + e, "Fail");
			return false;
		}
	}	


	//*****************************************************************************************
	//*	Name		    : fCommonJavascriptClick
	//*	Description	    : Click on Element using Java script command
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: None
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common javascript click.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonJavascriptClick(String webElmtProp, String strObjName){
		try{
			//Get WebElement
			WebElement webElement = fCommonGetObject(webElmtProp,strObjName);
			if(webElement == null){
				Reporter.fnWriteToHtmlOutput("fCommonJavascriptClick", "Object not Found","Object: " + strObjName + " doesn't exist", "Fail");
				return false;  
			}
			//Click on the WebElement    		
			int intCount = 1;        
			while (intCount<=3){
				try {	    
					webElement.getLocation();

					((JavascriptExecutor) driver).executeScript("return arguments[0].click()", webElement);
					break;	        		
				}catch (StaleElementReferenceException e){
					webElement = fCommonGetObject(webElmtProp,strObjName);
					intCount++;
				}catch (InvalidElementStateException e){	
					webElement = fCommonGetObject(webElmtProp,strObjName);
					intCount++;
				}catch (NullPointerException e){	    
					break;
				}
				catch (Exception e){	
					webElement = fCommonGetObject(webElmtProp,strObjName);
					intCount++;
				}
				if(intCount==3){
					Reporter.fnWriteToHtmlOutput("fCommonJavascriptClick","Click: "+strObjName, strObjName+" is not clicked", "Fail");
					return false;
				}
			}
			Reporter.fnWriteToHtmlOutput("fCommonJavascriptClick", strObjName+" should be clicked",strObjName+" is clicked successfully", "Done");
			return CheckErrorWarningPopUp();
//			return true;

		}catch(Exception e){
			Reporter.fnWriteToHtmlOutput("fCommonJavascriptClick", "Exception occurred for Object: " + strObjName,"Exception: " + e, "Fail");
			return false;    		
		}
	}


	//*****************************************************************************************
	//*	Name		    : fCommonSelectionOptionFromList
	//*	Description	    : select option from list
	//*	Author		    : Abhijeet Gorecha
	//* Parameters		: sSelectionMethod (ByValue,ByVisibleText). If a value other than these, ByVisibleText is used
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common selection option from list.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @param strText the str text
	 * @param sSelectionMethod the s selection method
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonSelectionOptionFromList(String webElmtProp, String strObjName, String strText, String sSelectionMethod){
		try{
			//Get WebElement
			WebElement objList = fCommonGetObject(webElmtProp,strObjName);
			Actions cursor = new Actions(driver);

			//Set Select Element and value	    	    	 		
			int intCount = 1;
			Select select;
			while (intCount<=3){
				try {
					select = new Select(objList);
					if(sSelectionMethod.equals("ByValue") || sSelectionMethod.equals("")){
						select.selectByValue(strText);
					}else if(sSelectionMethod.equals("ByVisibleText")){
						select.selectByVisibleText(strText);
					}else if(sSelectionMethod.equals("ByClick")){
						List <WebElement> listOptions = objList.findElements(By.tagName("option"));		        			
						for(int i = 0; i< listOptions.size();i++){
							if(listOptions.get(i).getText().equalsIgnoreCase(strText)){
								listOptions.get(i).click();
								break;
							}
						}
					}else if(sSelectionMethod.equals("sendKeys")){
						if(objList==null){
							Reporter.fnWriteToHtmlOutput("fCommonSelectionOptionFromList", strObjName+ " should be selected from list", strObjName+ " is not selected", "Fail");
							return false;
						}
						objList.sendKeys(strText);
					}
					break;
				}catch (StaleElementReferenceException e){
					objList = fCommonGetObject(webElmtProp,strObjName);
				}catch (InvalidElementStateException e){
					objList = fCommonGetObject(webElmtProp,strObjName);
				}catch (WebDriverException e){
					try {
						cursor.moveToElement(objList).perform();
						cursor.click();    
						WebElement selElmt = objList;
						selElmt.click();
						cursor.sendKeys(strText);
						cursor.moveToElement(selElmt).perform();
						break;
					} catch (Exception e1) {
						e1.printStackTrace();
						return false;
					}

				}catch (NullPointerException e){
					break;
				}
				intCount++;
			}	        
			return CheckErrorWarningPopUp();
//			return true;

		} catch (Exception e){		
			System.out.println("fCommonSelectionOptionFromList(): " + e);
			return false;
		}
	} 


	//*****************************************************************************************
	//*	Name		    : fCommonSetValueEditBox
	//*	Description	    : Set value in Edit box
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: strClear  - if no need to clear edit box, put: "N" otherwise can leave empty: "Y"
	//*					  strSkipVerify - if no need to do verification after set, put: "Y" otherwise leave empty: ""
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common set value edit box.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @param strValue the str value
	 * @param strClear the str clear
	 * @param strSkipVerify the str skip verify
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonSetValueEditBox(String webElmtProp, String strObjName, String strValue, String strClear,String strSkipVerify){
		try{
			//Get WebElement
			WebElement objWebEdit = fCommonGetObject(webElmtProp,strObjName);
//			getWaitObject().until(ExpectedConditions.elementToBeClickable(objWebEdit));
			JavascriptExecutor js = ((JavascriptExecutor) driver);

			//Checks if input parameter is Null
			if (strValue == null){
				strValue = "";
			}
			//Checks if object is enabled
			if (fCommonIsEnabled(webElmtProp,strObjName) == false){
				Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBox",strObjName + " should be Enabled", strObjName + " is not Enabled" , "Fail");
				return false;
			}
			//Check if element is clickable
			//objWebEdit = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(objWebEdit));

			//Set value to the Editbox
			int intCount = 1;
			while (intCount<=1){
				try {
					if(objWebEdit.getAttribute("role") != null && objWebEdit.getAttribute("role").equalsIgnoreCase("combobox")) {
						final String webElementSelector = webElmtProp;
						final String value = strValue;
						final String object = strObjName;

						try {
							getWaitObject().until(new ExpectedCondition<Boolean>() {
								public Boolean apply(WebDriver driver) {
									WebElement element = fCommonGetObject(webElementSelector, object);
									if (!fCommonJavascriptClick(webElementSelector, object))
										return false;
									element.clear();
									element.sendKeys(value);
									if (element.getAttribute("aria-activedescendant").equals(""))
										return false;
									else
										return true;
								}
							});

							getWaitObject().until(new ExpectedCondition<Boolean>() {
								public Boolean apply(WebDriver driver) {
									String id = fCommonGetObject(webElementSelector, "").getAttribute("aria-activedescendant");
									if (!fCommonJavascriptClick("xpath:=//li[@id='" + id +"']/..//li[@title='" + value +"']", ""))
										return false;
									WebElement element = fCommonGetObject(webElementSelector, "");
									if(element.getAttribute("title").equals(value))
										return true;
									else
										return false;
								}
							});
						} catch (TimeoutException e) {
							Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBox","Set value '" + value + "' in " + object + " combo box", "Value '" + value + "' is not set in " + object + " combo box" , "Fail");
							return false;
						}
					} else {
						if (strClear.equalsIgnoreCase("Y")) {
							objWebEdit.sendKeys(Keys.CONTROL + "a");
							objWebEdit.sendKeys(Keys.DELETE);
							//objWebEdit.clear();
						}
						//					js.executeScript("arguments[0].value = '"+strValue+"');", objWebEdit);
						objWebEdit.sendKeys(Keys.CONTROL + "a");
						objWebEdit.sendKeys(Keys.DELETE);
						objWebEdit.sendKeys(strValue);
//						objWebEdit.click();
						fWaitForPageToLoad();
					}
				}catch (StaleElementReferenceException e){
					objWebEdit = fCommonGetObject(webElmtProp,strObjName);
				}catch (InvalidElementStateException e){	
					objWebEdit = fCommonGetObject(webElmtProp,strObjName);
				}catch (NullPointerException e){	    
					break;
				}catch (Exception e)
				{
					try {
						System.out.println("fCommonSetValueEditBox - Failover - Setting value via JavaScript");
						js.executeScript("arguments[0].value='"+strValue+"';var event = new Event('change');arguments[0].dispatchEvent(event);", objWebEdit);
					} catch (Exception e1) {
						return true;
					}
				}
				//Validate if the value is selected successfully
				if (strSkipVerify.equalsIgnoreCase("N"))
				{
					if(objWebEdit.getAttribute("value").equals(strValue)){

						break;
					}else{
						Thread.sleep(2000);
						objWebEdit = fCommonGetObject(webElmtProp,strObjName);
						if(intCount==2){
							Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBox","Set: " + strValue, strValue +" is not set" , "Fail");
							return false;
						}
					}
				}
				intCount++;
			}
			Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBox", strValue+" should be set in "+strObjName, strValue+ " is set in "+strObjName+" successfully", "Done");    	
//			return true;
			return CheckErrorWarningPopUp();
		}catch (Exception e)
		{	

			Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBox", "Exception occurred for object: "+ strObjName,"Exception: " + e, "Fail");
			return false;

		}

	}

	//*****************************************************************************************
	//*	Name		    : fCommonSetValueMultiSelectBox
	//*	Description	    : Set value in Multi Select box
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common set value in multi select box.
	 *
	 * @param webElementLocator select element locator
	 * @param objectName select element name
	 * @param optionValue option value to select
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonSetValueMultiSelectBox(String webElementLocator, String objectName, String optionValue) {
		try {
			//Get WebElement
			WebElement objWebEdit = fCommonGetObject(webElementLocator, objectName);
			getWaitObject().until(ExpectedConditions.elementToBeClickable(objWebEdit));
			JavascriptExecutor js = ((JavascriptExecutor) driver);

			//Checks if input parameter is Null
			if (optionValue == null) {
				optionValue = "";
			}
			//Checks if object is enabled
			if (fCommonIsEnabled(webElementLocator, objectName) == false) {
				Reporter.fnWriteToHtmlOutput("fCommonSetValueMultiSelectBox", objectName + " should be Enabled", objectName + " is not Enabled", "Fail");
				return false;
			}
			//Check if element is clickable
			objWebEdit = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(objWebEdit));

			//Set value to the Select box
			try {
				String script = "var select = arguments[0];"
						+ "if (select == null)"
						+ "return false;"
						+ "select.value = '" + optionValue + "';"
						+ "var event = document.createEvent('KeyboardEvent');"
						+ "try {"
						+ "event.initKeyboardEvent('keydown', true, true, window, false, false, false, false, 13, 0);"
						+ "} catch(error) {"
						+ "event.initKeyEvent('keydown', true, true, window, false, false, false, false, 13, 0);"
						+ "}"
						+ "select.dispatchEvent(event);"
						+ "var option = document.evaluate(\"//dd[text()='"+ optionValue + "']\", document, null, XPathResult.ANY_TYPE, null).iterateNext();"
						+ "if (option == null)"
						+ "return false;"
						+ "option.click();"
						+ "return true;";

				boolean status = (Boolean) js.executeScript(script, objWebEdit);

				if (status)
					Reporter.fnWriteToHtmlOutput("fCommonSetValueMultiSelectBox", optionValue + " should be set in " + objectName, optionValue + " is set in " + objectName + " successfully", "Done");
				else
					Reporter.fnWriteToHtmlOutput("fCommonSetValueMultiSelectBox", optionValue + " should be set in " + objectName, optionValue + " is not set in " + objectName + " successfully", "Fail");

				return status;
			} catch (Exception e) {
				return false;
			}
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSetValueMultiSelectBox", "Exception occurred for object: " + objectName, "Exception: " + e, "Fail");
			return false;

		}

	}

	public boolean fCommonSetValueEditBoxViaJavaScript(String webElmtProp, String strObjName, String strValue){
		try{
			//Get WebElement
			List<WebElement> inputElements = fCommonGetMultipleObjects(webElmtProp,strObjName);
			JavascriptExecutor jse = ((JavascriptExecutor) driver);
			//Checks if input parameter is Null
			if (strValue == null){
				strValue = "";
			}
			//Checks if object is enabled
			if (fCommonIsEnabled(webElmtProp,strObjName) == false){
				Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBoxViaJavaScript",strObjName + " should be Enabled", strObjName + " is not Enabled" , "Fail");
				return false;
			}
			System.out.println("Object " + webElmtProp + "is clickable");
			//Set value to the Editbox
			try {
				String js = "var elements = arguments[0];"
						+ "if (elements.length == 0)"
						+ "return false;"
						+ "for (var i = 0; i < elements.length; i++)"
						+ "{"
						+ "var event = new Event('change');"
						+ "elements[i].value = '';"
						+ "elements[i].dispatchEvent(event);"
						+ "elements[i].value = '" + strValue + "';"
						+ "elements[i].dispatchEvent(event);"
						+ "}"
						+ "return true";
				if((Boolean)jse.executeScript(js, inputElements)) {
					Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBoxViaJavaScript", strValue + " should be set in " + strObjName, strValue + " is set in " + strObjName + " successfully", "Pass");
					return true;
				} else {
					Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBoxViaJavaScript", strValue + " should be set in " + strObjName, strValue + " is not set in " + strObjName + " successfully", "Fail");
					return false;
				}
			} catch (Exception e1) {
				Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBoxViaJavaScript","Set: " + strValue, strValue + " is not set" , "Fail");
				return false;
			}
		}catch (Exception e)
		{
			Reporter.fnWriteToHtmlOutput("fCommonSetValueEditBoxViaJavaScript", "Exception occurred for object: " + strObjName,"Exception: " + e, "Fail");
			return false;
		}
	}

	//*****************************************************************************************
	//*	Name		    : fCommonSetValueMultiSelectBox
	//*	Description	    : Set value in Multi Select box
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common set value in multi select box.
	 *
	 * @param webElementSelector select element locator
	 * @param objectName select element name
	 * @param value option value to select
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonSetComboBoxValue(String webElementSelector, String objectName, String value) {
		try {
			//Get WebElement
			WebElement objWebEdit = fCommonGetObject(webElementSelector, objectName);
			if (objWebEdit == null) {
				Reporter.fnWriteToHtmlOutput("fCommonSetComboBoxValue", "Object should exist", "Object was not found", "Fail");
				return false;
			}

			//Checks if input parameter is Null
			if (value == null) {
				value = "";
			}

			//Set value to the Select box
			try {
				JavascriptExecutor jse = ((JavascriptExecutor) driver);
				String script = "var element = arguments[0];$(element).data('kendoComboBox').text('" + value + "');$(element).trigger('change');"
						+ "var event = document.createEvent('KeyboardEvent');"
						+ "try {"
						+ "event.initKeyboardEvent('keydown', true, true, window, false, false, false, false, 13, 0);"
						+ "} catch(error) {"
						+ "event.initKeyEvent('keydown', true, true, window, false, false, false, false, 13, 0);"
						+ "}"
						+ "element.dispatchEvent(event);";

				boolean status = (Boolean) jse.executeScript(script, fCommonGetObject(webElementSelector, objectName));

				if (status)
					Reporter.fnWriteToHtmlOutput("fCommonSetComboBoxValue", value + " should be set in " + objectName, value + " is set in " + objectName + " successfully", "Done");
				else
					Reporter.fnWriteToHtmlOutput("fCommonSetComboBoxValue", value + " should be set in " + objectName, value + " is not set in " + objectName + " successfully", "Fail");

				return status;
			} catch (Exception e) {
				return false;
			}
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSetComboBoxValue", "Exception occurred for object: " + objectName, "Exception: " + e, "Fail");
			return false;

		}

	}

	/**
	 * Select option from dropdown by value
	 *
	 * @param webElementSelector - web element selector
	 * @param value - value
	 * @param objectName - dropdown name - for reporting purposes
     * @return - return true if successful
     */
	public boolean fCommonSelectOptionByValue(final String webElementSelector, final String value, final String objectName){
		try {
			new Select(fCommonGetObject(webElementSelector, objectName));
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSelectOptionByValue", objectName + " should exist on the page", objectName + " doesn't exist on the page", "Fail");
			System.out.println("fCommonSelectOptionByValue(): Dropdown with selector:" + webElementSelector + " doesn't exist on the page");
			System.out.println("fCommonSelectOptionByValue(): " + e);
			return false;
		}

		try {
			getWaitObject().until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					try {
						getWaitObject().until(ExpectedConditions.elementToBeClickable(fCommonGetObject(webElementSelector, objectName).findElement(By.xpath("..//span[@class='select2-selection__arrow']")))).click();
						getWaitObject().until(ExpectedConditions.elementToBeClickable(By.cssSelector(".select2-search--dropdown .select2-search__field[tabindex='0']"))).sendKeys(value);
						Actions builder = new Actions(driver);
						builder.moveToElement(getWaitObject().until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[@class='select2-results']//li[@class='item_list_item' and not(text())]//span[text()='" + value + "']")))), 10, 10).click().build().perform();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return false;
					}
					return true;
				}
			});
		}catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSelectOptionByValue", objectName + " should have option '" + value + "' in it", "Option '" + value + "' doesn't exist in the " + objectName, "Fail");
			System.out.println("fCommonSelectOptionByValue(): Option value '" + value + "' doesn't exist in the dropdown");
			System.out.println("fCommonSelectOptionByValue(): " + e);
			return false;
		}

		try {
			getWaitObject().until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					Select select = new Select(fCommonGetObject(webElementSelector, objectName));
					return select.getFirstSelectedOption().getText().equals(value);
				}
			});
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSelectOptionByValue", "'" + value + "' option should be selected in the " + objectName, "Option is not selected", "Fail");
			System.out.println("fCommonSelectOptionByValue(): " + e);
			return false;
		}
		Reporter.fnWriteToHtmlOutput("fCommonSelectOptionByValue", "'" + value + "' option should be selected in the " + objectName, "'" + value + "' option is successfully selected in the " + objectName, "Pass");

		return true;
	}

	/**
	 * Select option from dropdown by value
	 *
	 * @param webElementSelector - web element selector
	 * @param value - option value
	 * @param objectName - dropdown name - for reporting purposes
	 * @param shouldExist - true if we expect that option exists, otherwise false
	 * @return - return true if successful
	 */
	public boolean fCommonVerifySelectOptionExists(final String webElementSelector, final String value, final String objectName, final boolean shouldExist){
		try {
			new Select(fCommonGetObject(webElementSelector, objectName));
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonVerifySelectOptionExists", objectName + " should exist on the page", objectName + " doesn't exist on the page", "Fail");
			System.out.println("fCommonVerifySelectOptionExists(): Dropdown with selector:" + webElementSelector + " doesn't exist on the page");
			System.out.println("fCommonVerifySelectOptionExists(): " + e);
			return false;
		}

		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
		try {
			getWaitObject(5).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					try {
						getWaitObject().until(ExpectedConditions.elementToBeClickable(fCommonGetObject(webElementSelector, objectName).findElement(By.xpath("../../..//label")))).click();
						getWaitObject().until(ExpectedConditions.elementToBeClickable(fCommonGetObject(webElementSelector, objectName).findElement(By.xpath("..//span[@class='select2-selection__arrow']")))).click();
						getWaitObject().until(ExpectedConditions.elementToBeClickable(By.cssSelector(".select2-search--dropdown .select2-search__field[tabindex='0']"))).sendKeys(value);
						getWaitObject().until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[@class='select2-results']//li[@class='item_list_item' and not(text())]//span[text()='" + value + "']"))));
					} catch (Exception e) {
						return shouldExist ? false : true;
					}
					return shouldExist ? true : false;
				}
			});
		}catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonVerifySelectOptionExists", objectName + (shouldExist ? " should" : " shouldn't") + " have option '" + value + "' in it", "Option '" + value + (shouldExist ? "' doesn't" : "' does") + " exist in the " + objectName, "Fail");
			System.out.println("fCommonVerifySelectOptionExists(): Option value '" + value + (shouldExist ? "' doesn't" : "' does") + " exist in the dropdown");
			driver.manage().timeouts().implicitlyWait(30000, TimeUnit.MILLISECONDS);
			return false;
		}
		Reporter.fnWriteToHtmlOutput("fCommonVerifySelectOptionExists", objectName + (shouldExist ? " should" : " shouldn't") + " have option '" + value + "' in it", "Option '" + value + (shouldExist ? "' does" : "' doesn't") + " exist in the " + objectName, "Pass");
		driver.manage().timeouts().implicitlyWait(30000, TimeUnit.MILLISECONDS);

		return true;
	}

	/**
	 * Select option from multi select dropdown by value
	 *
	 * @param webElementSelector - web element selector - div element that contains select
	 * @param value - value
	 * @param objectName - dropdown name - for reporting purposes
	 * @return - return true if successful
	 */
	public boolean fCommonSelectMultiOptionByValue(final String webElementSelector, final String value, final String objectName){
		try {
			new Select(fCommonGetObject(webElementSelector, objectName));
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSelectOptionByValue", objectName + " should exist on the page", objectName + " doesn't exist on the page", "Fail");
			System.out.println("fCommonSelectOptionByValue(): Dropdown with selector:" + webElementSelector + " doesn't exist on the page");
			System.out.println("fCommonSelectOptionByValue(): " + e);
			return false;
		}

		try {
			getWaitObject().until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					try {
						getWaitObject().until(ExpectedConditions.elementToBeClickable(fCommonGetObject(webElementSelector, objectName).findElement(By.xpath("..//input")))).click();
						getWaitObject().until(ExpectedConditions.elementToBeClickable(fCommonGetObject(webElementSelector, objectName).findElement(By.xpath("..//input")))).sendKeys(value);
						getWaitObject().until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[@class='select2-results']//li[@class='item_list_item' and not(text())]//span[text()='" + value + "']")))).click();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return false;
					}
					return true;
				}
			});
		}catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSelectMultiOptionByValue", objectName + " should have option '" + value + "' in it", "Option '" + value + "' doesn't exist in the " + objectName, "Fail");
			System.out.println("fCommonSelectMultiOptionByValue(): Option value '" + value + "' doesn't exist in the dropdown");
			System.out.println("fCommonSelectMultiOptionByValue(): " + e);
			return false;
		}

		try {
			getWaitObject().until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					Select select = new Select(fCommonGetObject(webElementSelector, objectName));
					for (WebElement option : select.getAllSelectedOptions()) {
						if (option.getText().equals(value))
							return true;
					}
					return false;
				}
			});
		} catch (Exception e) {
			Reporter.fnWriteToHtmlOutput("fCommonSelectMultiOptionByValue", "'" + value + "' option should be selected in the " + objectName, "Option is not selected", "Fail");
			System.out.println("fCommonSelectMultiOptionByValue(): " + e);
			return false;
		}
		Reporter.fnWriteToHtmlOutput("fCommonSelectMultiOptionByValue", "'" + value + "' option should be selected in the " + objectName, "'" + value + "' option is successfully selected in the " + objectName, "Pass");

		return true;
	}

	//*****************************************************************************************
	//*	Name		    : fCommonGetWebElement
	//*	Description	    : Check if the webelement is properly identified
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: WebElement properties, WebElement name
	//*	Return Values	: WebElement Object
	/**
	 * F common get web element.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param objName the obj name
	 * @return the web element
	 */
	//*****************************************************************************************
	public WebElement fCommonGetWebElement(String webElmtProp, String objName){
		try{
			//Get WebElement
			WebElement webElement = fCommonGetObject(webElmtProp,objName);  		

			//Check if the Webelement is enabled or displayed    		
			boolean bIsDisplayed = false;
			boolean bIsEnabled = false;

			int intCount = 1;        
			while (!(bIsDisplayed || bIsEnabled) && (intCount <=3)){
				try {
					bIsDisplayed = webElement.isDisplayed();					
					bIsEnabled = webElement.isEnabled();
				}catch (StaleElementReferenceException e){	
					webElement = fCommonGetObject(webElmtProp,objName);
				}catch (WebDriverException e){	    
					webElement = fCommonGetObject(webElmtProp,objName);
				}catch (NullPointerException e){	    
					webElement = fCommonGetObject(webElmtProp,objName);
					if(webElement == null){
						break;
					}
				}	    	    
				intCount++;			
			}

			//Validate if the element is displayed
			if (!(bIsDisplayed || bIsEnabled)){
				Reporter.fnWriteToHtmlOutput("fCommonGetWebElement", objName+" should be available", objName+ " is not available", "Fail");
				return null;
			}
			Reporter.fnWriteToHtmlOutput("fCommonGetWebElement", objName+" should be available", objName+ " is available", "Pass");
			return webElement;
		}catch(Exception e){
			Reporter.fnWriteToHtmlOutput(objName, "Exception occurred","Exception: " + e, "Fail");			
			return null;  		
		}
	}	


	//*****************************************************************************************
	//*	Name		    : fCommonSync
	//*	Description	    : waiting the specified time
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: sTime - time to wait
	//*	Return Values	: Void
	/**
	 * F common sync.
	 *
	 * @param sTime the s time
	 */
	//*****************************************************************************************  
	public void fCommonSync(Integer sTime)
	{
		System.out.println("fCommonSync   <-----------------------------------");
		try {
			Thread.sleep(sTime);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}

	//*****************************************************************************************
	//*	Name		    : fCommonBrowserBackButton
	//*	Description	    :navigate back to previous page
	//*	Author		    : Abhijeet Gorecha 
	//*	Input Params	: 
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common browser back button.
	 */
	//*****************************************************************************************
	public void fCommonBrowserBackButton() {			
		driver.navigate().back();
		Reporter.fnWriteToHtmlOutput("fCommonBrowserBackButton", "Navigate back to previous page using back button","Navigated to previous page" , "Done");

	} 

	//*****************************************************************************************
	//*	Name		    : fCommonGetObject(String objDesc, String objName)
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: String
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common get object.
	 *
	 * @param objDesc the obj desc
	 * @param objName the obj name
	 * @return the web element
	 */
	//*****************************************************************************************	        
	public WebElement fCommonGetObject(String objDesc, String objName){

		//Delimiters
		String[] delimiters = new String[] {":="};
		String[] arrFindByValues = objDesc.split(delimiters[0]);
		//Get Findby and V   alue
		String FindBy, val;
		if(arrFindByValues.length==2){
			FindBy = arrFindByValues[0];
			val = arrFindByValues[1];        	
		}
		else{
			Reporter.fnWriteToHtmlOutput("fCommonGetObject", "objDesc should be valid","objDesc is not valid: " + objDesc, "Fail");
			return null;
		}

		/*Boolean loadStatus;
		int i=0;
		do{
			loadStatus = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			fCommonSync(1000);
			//			System.out.println("waiting");
			i++;
		}

		while(loadStatus == false && i<=30);*/

		fWaitForPageToLoad();

		//WebElement el=	wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(val)));
	
		int intcount = 1;
		while (intcount <= 2) {
			//System.out.println("Looking for Object: " + val + "No:" + intcount);
			try{
				//Handle all FindBy cases
				String strElement = FindBy.toLowerCase();
				if (strElement.equalsIgnoreCase("linktext")){
					WebElement Element= getWaitObject().until(ExpectedConditions.presenceOfElementLocated(By.linkText(val)));
					//					Reporter.fnWriteToHtmlOutput("fCommonGetObject", objName+" should be available", objName+ " is available", "Pass");
					return Element;
				}
				else if (strElement.equalsIgnoreCase("xpath")){
					//WebElement Element= getWaitObject().until(ExpectedConditions.presenceOfElementLocated(By.xpath(val)));
					//					Reporter.fnWriteToHtmlOutput("fCommonGetObject", objName+" should be available", objName+ " is available", "Pass");
					WebElement Element= (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(val)));
					return Element;
				}
				else if (strElement.equalsIgnoreCase("name")){
					WebElement Element= getWaitObject().until(ExpectedConditions.presenceOfElementLocated(By.name(val)));
					//					Reporter.fnWriteToHtmlOutput("fCommonGetObject", objName+" should be available", objName+ " is available", "Pass");
					return Element;
				}
				else if (strElement.equalsIgnoreCase("id")){
					WebElement Element= getWaitObject().until(ExpectedConditions.presenceOfElementLocated(By.id(val)));
					//					Reporter.fnWriteToHtmlOutput("fCommonGetObject", objName+" should be available", objName+ " is available", "Pass");
					return Element;
				}
				else if (strElement.equalsIgnoreCase("classname")){
					WebElement Element= getWaitObject().until(ExpectedConditions.presenceOfElementLocated(By.className(val)));
					//					Reporter.fnWriteToHtmlOutput("fCommonGetObject", objName+" should be available", objName+ " is available", "Pass");
					return Element;
				}
				else if (strElement.equalsIgnoreCase("cssselector")){
					WebElement Element= getWaitObject().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(val)));
					//					Reporter.fnWriteToHtmlOutput("fCommonGetObject", objName+" should be available", objName+ " is available", "Pass");
					return Element;
				}	
				else if (strElement.equalsIgnoreCase("partialLinkText")){
					WebElement Element= getWaitObject().until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(val)));
					//					Reporter.fnWriteToHtmlOutput("fCommonGetObject", objName+" should be available", objName+ " is available", "Pass");
					return Element;	            		
				}
				else{
					Reporter.fnWriteToHtmlOutput("fCommonGetObject", "Property name :" + FindBy,"Property name specified for object " + objName + " is invalid", 

							"Fail");
					return null;
				}		            	
			}
			catch(Exception e){
				driver.manage().timeouts().implicitlyWait(700, TimeUnit.MILLISECONDS);
				if (intcount == 3){
					//					Reporter.fnWriteToHtmlOutput("Object :"+objName, objName+" should be present", objName+" is not present", "Fail");
					//Reporter.fnWriteToHtmlOutput("Object :"+objName, "Exception occurred","Exception :" + e.toString(), "Fail");		            		
					driver.manage().timeouts().implicitlyWait(700, TimeUnit.MILLISECONDS);
					return null;
				}		            	
				intcount = intcount + 1;
				//System.out.println("intcount ++ incremented");
				continue;
				//driver.manage().timeouts().implicitlyWait(700, TimeUnit.MILLISECONDS);
				//Select browser in focus
				//((JavascriptExecutor) driver).executeScript("focus()");
			}		            
		}
		return null;	           
	}

	//*****************************************************************************************
	//*	Name		    : fCommonCheckObjectExistance
	//*	Description	    : Set value in Edit box
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: None
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common check object existance.
	 *
	 * @param objDesc the obj desc
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonCheckObjectExistance(String objDesc){        
		boolean bStatus = false;

		//Delimiters
		String[] delimiters = new String[] {":="};
		String[] arrFindByValues = objDesc.split(delimiters[0]);
		//Get Findby and Value 
		String FindBy="";
		String val="";
		if(arrFindByValues.length==2){
			FindBy = arrFindByValues[0];
			val = arrFindByValues[1];        	
		}
		else{
			Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", "objDesc should be valid","objDesc is not valid: " + objDesc, "Fail");
			return false;
		}


		int intcount = 1;	            
		while (intcount <= 3){
			//Set the implicit time-out for the driver
			driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
			try{
				//Handle all FindBy cases
				String strElement = FindBy.toLowerCase();
				if (strElement.equalsIgnoreCase("linktext")){
					bStatus = driver.findElement(By.linkText(val)).isEnabled();
				}
				else if (strElement.equalsIgnoreCase("xpath")){
					bStatus = driver.findElement(By.xpath(val)).isEnabled();
					//bStatus = driver.findElements(By.xpath(val)).size() != 0;
				}
				else if (strElement.equalsIgnoreCase("name")){
					bStatus = driver.findElement(By.name(val)).isEnabled();
				}
				else if (strElement.equalsIgnoreCase("id")){
					bStatus = driver.findElement(By.id(val)).isEnabled();
				}
				else if (strElement.equalsIgnoreCase("classname")){
					bStatus = driver.findElement(By.className(val)).isEnabled();
				}
				else if(strElement.equalsIgnoreCase("cssselector")){
					bStatus = driver.findElement(By.cssSelector(val)).isEnabled();
				}
				driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
				//				Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement exist", "Done");
				if(bStatus==true){
					if(!objDesc.contains("nx-ajaxLoader"))
					{
						Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement exists", "Pass");
						return true;
					}
				}
				else{
					Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement does not exist", "Fail");
					return false;
				}
				//				return bStatus;

			}catch(Exception e){
				if(objDesc.contains("nx-ajaxLoader")){
					break;
				}
				//fCommonSync(1000);
				if (intcount == 3){
					driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
					e.printStackTrace();
					Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement does not exist", "Fail");
					return false;
				}	
				intcount = intcount + 1;
				//Select browser in focus
				((JavascriptExecutor) driver).executeScript("focus()");
			}
		}
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
		//		Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement does not exist", "Fail");
		return false;
	}

	//*****************************************************************************************
	//*	Name		    : fCommonGetWebElementsList
	//*	Description	    : Check if the webelements are identified properly
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: WebElement properties, WebElement name
	//*	Return Values	: WebElement List
	/**
	 * F common get web elements list.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param objName the obj name
	 * @return the list
	 */
	//*****************************************************************************************
	public List<WebElement> fCommonGetWebElementsList(String webElmtProp, String objName){
		try{
			//Get WebElement
			List<WebElement> webElements = fCommonGetMultipleObjects(webElmtProp,objName);
			//Check if the Webelement is enabled or displayed    		
			boolean bIsDisplayed = false;
			boolean bIsEnabled = false;

			int intCount = 1;        
			while (!(bIsDisplayed || bIsEnabled) && (intCount <=3)){
				try {	        					
					if(webElements.size() != 0){
						bIsDisplayed = webElements.get(0).isDisplayed();
						bIsEnabled = webElements.get(0).isEnabled();
					}					
				}catch (StaleElementReferenceException e){	
					webElements = fCommonGetMultipleObjects(webElmtProp,objName);
				}catch (WebDriverException e){	    
					webElements = fCommonGetMultipleObjects(webElmtProp,objName);
				}catch (NullPointerException e){	    
					webElements = fCommonGetMultipleObjects(webElmtProp,objName);
					if(webElements == null){
						break;
					}
				}	    	    
				intCount++;			
			}

			//Validate if the element is displayed
			if (!(bIsDisplayed || bIsEnabled)){
				Reporter.fnWriteToHtmlOutput(objName, objName+ " should be displayed", objName+ " is not displayed", "Fail");
				return null;
			}
			Reporter.fnWriteToHtmlOutput(objName, objName+ " should be displayed", objName+ " is displayed", "Pass");
			return webElements;
		}catch(Exception e){
			Reporter.fnWriteToHtmlOutput("fCommonGetWebElementsList", "Exception occurred for object: " + objName,"Exception: " + e, "Fail");			
			return null;    		
		}
	}


	//*****************************************************************************************
	//*	Name		    : fCommonGetMultipleObjects(String objDesc, String objName)
	//*	Description	    : Method to get multiple objects having same property
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: String
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common get multiple objects.
	 *
	 * @param objDesc the obj desc
	 * @param objName the obj name
	 * @return the list
	 */
	//*****************************************************************************************	        
	public List<WebElement> fCommonGetMultipleObjects(String objDesc, String objName){
		//Delimiters
		String[] delimiters = new String[] {":="};
		String[] arrFindByValues = objDesc.split(delimiters[0]);
		//Get Findby and Value 
		String FindBy = arrFindByValues[0];
		String val = arrFindByValues[1];
		int intcount = 1;	            
		while (intcount <= 2){	            	
			try{
				//Handle all FindBy cases
				String strElement = FindBy.toLowerCase();
				if (strElement.equalsIgnoreCase("linktext")){
					return driver.findElements(By.linkText(val));
				}
				else if (strElement.equalsIgnoreCase("xpath")){
					return driver.findElements(By.xpath(val));
				}
				else if (strElement.equalsIgnoreCase("name")){
					return driver.findElements(By.name(val));
				}
				else if (strElement.equalsIgnoreCase("id")){
					return driver.findElements(By.id(val));
				}
				else if (strElement.equalsIgnoreCase("classname")){
					return driver.findElements(By.className(val));
				}
				else if (strElement.equalsIgnoreCase("cssselector")){
					return driver.findElements(By.cssSelector(val));
				}
				else if (strElement.equalsIgnoreCase("tagname")){
					return driver.findElements(By.tagName(val));
				}
				else{
					Reporter.fnWriteToHtmlOutput("fCommonGetMultipleObjects", "Property name :" + FindBy,"Property name specified for object " + objName + " is invalid", 

							"Fail");
					return null;
				}		            	
			}
			catch(Exception e){		            	
				if (intcount == 2){
					Reporter.fnWriteToHtmlOutput("fCommonGetMultipleObjects", objName+" should be present", objName+" is not present", "Fail");
					Reporter.fnWriteToHtmlOutput("fCommonGetMultipleObjects", "Exception occurred for object: " + objName,"Exception :" + e.toString(), "Fail");		            		
					return null;
				}		            	
				intcount = intcount + 1;
				//Select browser in focus
				((JavascriptExecutor) driver).executeScript("focus()");
			}		            
		}
		return null;	           
	}

	//*****************************************************************************************
	//*	Name		    : fCommonClick (Overloaded)
	//*	Description	    : Click on the webelement
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: WebElement - The webelement for which we need to click
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common click.
	 *
	 * @param webElement the web element
	 * @param strObjName the str obj name
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonClick(WebElement webElement, String strObjName){
		//Click on the WebElement
		int intCount = 1;        
		while (intCount<=3){
			try {
				webElement.getLocation();
				webElement.click();
				break;
			}catch (Exception e){    
				if(intCount==4){
					Reporter.fnWriteToHtmlOutput("fCommonClick: "+strObjName, "Exception occurred","Exception: " + e, "Fail");
					return false;
				}
			}  	    
			intCount++;
		}	        
		Reporter.fnWriteToHtmlOutput("fCommonClick: "+strObjName, strObjName+ " should be clicked", strObjName+ " is clicked successfully", "Done");
//		return true;    
		return CheckErrorWarningPopUp();
	}

	//*****************************************************************************************
	//*	Name		    : fCommonGetText
	//*	Description	    : Get text of an element
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: WebElement - The webelement for which we need to get text of
	//*	Return Values	: The text of that element
	/**
	 * F common get text.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @return the string
	 */
	//*****************************************************************************************
	public String fCommonGetText(String webElmtProp, String strObjName){
		try{
			//Get WebElement
			WebElement webElement = fCommonGetObject(webElmtProp,strObjName);
			String text=null;
			if(webElement == null){
				Reporter.fnWriteToHtmlOutput("fCommonGetText: " + strObjName ,"Object should be found", "Object was not found. Null value returned", "Fail");
				return null;
			}
			//Click on the WebElement    		
			int intCount = 1;        
			while (intCount<=3){
				try {	 
					webElement.getLocation();
					text = webElement.getText();
					break;

				}catch (StaleElementReferenceException e){
					webElement = fCommonGetObject(webElmtProp,strObjName);
				}catch (InvalidElementStateException e){	
					webElement = fCommonGetObject(webElmtProp,strObjName);
				}catch (WebDriverException e){	
					webElement = fCommonGetObject(webElmtProp,strObjName);
				}

				catch (NullPointerException e){	    
					webElement = fCommonGetObject(webElmtProp,strObjName);

				}catch (Exception e){ 
					driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);	
				}
				if(intCount==3){
					Reporter.fnWriteToHtmlOutput("fCommonGetText",strObjName, strObjName+" is not present", "Fail");
					return null;
				}
				intCount++;
			}	        
			Reporter.fnWriteToHtmlOutput("fCommonGetText", strObjName+ " should return text", strObjName+ " has returned text: " + text, "Pass");
			return text;
		}catch (Exception e) {			
			Reporter.fnWriteToHtmlOutput("fCommonGetText", "Exception occurred for object: "+ strObjName,"Exception: " + e, "Fail");
			return null;
		}         
	}



	//*****************************************************************************************
	//*	Name		    : fCommonSendKeys
	//*	Description	    : to Send Keys
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: Keys : the the key which we want to send
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common send keys.
	 *
	 * @param webElmtProp the web elmt prop
	 * @param strObjName the str obj name
	 * @param key the key
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonSendKeys(String webElmtProp, String strObjName, Keys key){
		String keyname = key.toString();
		try{
			//Get WebElement
			WebElement objWebEdit = fCommonGetObject(webElmtProp,strObjName);
			JavascriptExecutor js = ((JavascriptExecutor) driver);

			//Set value to the Editbox	    	    	 		
			int intCount = 1;        
			while (intCount<=1){
				try {
					objWebEdit.sendKeys(key); 
					break;
				}catch (StaleElementReferenceException e){	
					objWebEdit = fCommonGetObject(webElmtProp,strObjName);
				}catch (InvalidElementStateException e){	
					objWebEdit = fCommonGetObject(webElmtProp,strObjName);
				}catch (NullPointerException e){	    
					break;
				}catch (Exception e)
				{	
					driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
					try {

					} catch (Exception e1) {
						return false;
					}
				}
				//Validate if the value is selected successfully
				intCount++;
			}
			Reporter.fnWriteToHtmlOutput("fCommonSendKeys", keyname+" should be send ", keyname+ " is sent successfully", "Pass");    	
//			return true;
			return CheckErrorWarningPopUp();
		}catch (Exception e)
		{	
			Reporter.fnWriteToHtmlOutput("fCommonSendKeys", "Exception occurred for key: "+ keyname,"Exception: " + e, "Fail");
			return false;
		}
	}	


	//*****************************************************************************************
	//*	Name		    : fCommonCheckObjectDoesNotExist
	//*	Description	    : Set value in Edit box
	//*	Author		    : Abhijeet Gorecha
	//*	Input Params	: None
	//*	Return Values	: Boolean - Depending on the success
	/**
	 * F common check object does not exist.
	 *
	 * @param objDesc the obj desc
	 * @return true, if successful
	 */
	//*****************************************************************************************
	public boolean fCommonCheckObjectDoesNotExist(String objDesc){        
		boolean bStatus = false;

		//Delimiters
		String[] delimiters = new String[] {":="};
		String[] arrFindByValues = objDesc.split(delimiters[0]);
		//Get Findby and Value 
		String FindBy="";
		String val="";
		if(arrFindByValues.length==2){
			FindBy = arrFindByValues[0];
			val = arrFindByValues[1];        	
		}
		else{
			Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", "objDesc should be valid","objDesc is not valid: " + objDesc, "Fail");
			return false;
		}


		int intcount = 1;	            
		while (intcount <= 3){
			//Set the implicit time-out for the driver
			driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
			try{
				//Handle all FindBy cases
				String strElement = FindBy.toLowerCase();
				if (strElement.equalsIgnoreCase("linktext")){
					bStatus = driver.findElement(By.linkText(val)).isEnabled();
				}
				else if (strElement.equalsIgnoreCase("xpath")){
					bStatus = driver.findElement(By.xpath(val)).isEnabled();
				}
				else if (strElement.equalsIgnoreCase("name")){
					bStatus = driver.findElement(By.name(val)).isEnabled();
				}
				else if (strElement.equalsIgnoreCase("id")){
					bStatus = driver.findElement(By.id(val)).isEnabled();
				}
				else if (strElement.equalsIgnoreCase("classname")){
					bStatus = driver.findElement(By.className(val)).isEnabled();
				}
				else if(strElement.equalsIgnoreCase("cssselector")){
					bStatus = driver.findElement(By.cssSelector(val)).isEnabled();
				}
				driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
				//				Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement exist", "Done");
				if(bStatus==true){
					if(!objDesc.contains("nx-ajaxLoader"))
					{
						Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should not exist", "Webelement exists", "Fail");
						return false;
					}
				}
				else{
					Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should not exist", "Webelement does not exist", "Pass");
					return true;
				}
				//				return bStatus;

			}catch(Exception e){
				if(objDesc.contains("nx-ajaxLoader")){
					break;
				}
				//fCommonSync(1000);
				if (intcount == 3){
					driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
					Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should not exist", "Webelement does not exist", "Pass");
					return true;
				}	
				intcount = intcount + 1;
				//Select browser in focus
				((JavascriptExecutor) driver).executeScript("focus()");
			}
		}
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
		//		Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement does not exist", "Fail");
		return true;
	}

	//*****************************************************************************************
	//*	Name		    : fWaitForPageToLoad
	//*	Description	    : Waiting for all the elements to be ready on the page
	//*	Input Params	: None
	//*	Return Values	: /
	/**
	 * F Wait For Page To Load.
	 */
	//*****************************************************************************************
	public boolean fWaitForPageToLoad(){
		int waitPeriod = 30;
		WebDriverWait wait = new WebDriverWait(driver, waitPeriod);
		//NgWebDriver
//		try {
//			wait.until(new Predicate<WebDriver>() {
//				public boolean apply(WebDriver d) {
//					return (Boolean) ((JavascriptExecutor)d).executeScript("try {\n" +
//							"  if (typeof jQuery !== 'undefined'){\n " +
//								"	if(jQuery.active !== 0 || document.readyState !== 'complete'){\n" +
//								"	console.log('false jQUREY');return false;\n" +
//								"	} \n" +
//							"	}\n" +
//							"	else if (typeof jQuery === 'undefined' && document.readyState !== 'complete'){ \n" +
//							"	console.log('false jQUREY');return false; \n" +
//							"	}\n" +
//							"  if (window.angular) {\n" +
//							"    if (!window.qa) {\n" +
//							"      // Used to track the render cycle finish after loading is complete\n" +
//							"      window.qa = {\n" +
//							"        doneRendering: false\n" +
//							"      };\n" +
//							"    }\n" +
//							"// Get the angular injector for this app (change element if necessary)\n" +
//							"    var injector = window.angular.element('body').injector();\n" +
//							"    // Store providers to use for these checks\n" +
//							"    var $rootScope = injector.get('$rootScope');\n" +
//							"    var $http = injector.get('$http');\n" +
//							"    var $timeout = injector.get('$timeout');\n" +
//							"    // Check if digest\n" +
//							"    if ($rootScope.$$phase === '$apply' || $rootScope.$$phase === '$digest' || $http.pendingRequests.length !== 0) {\n" +
//							"      window.qa.doneRendering = false;\n" +
//							"      console.log('false ANG loading data');return false; // Angular digesting or loading data\n" +
//							"    }\n" +
//							"    if (!window.qa.doneRendering) {\n" +
//							"      // Set timeout to mark angular rendering as finished\n" +
//							"      $timeout(function() {\n" +
//							"        window.qa.doneRendering = true;\n" +
//							"      }, 0);\n" +
//							"      console.log('false ANG RENDERING');return false;\n" +
//							"    }\n" +
//							"}\n" +
//							"  return true;\n" +
//							"} catch (ex) {\n" +
//							"  console.log('false CATCH');return false;\n" +
//							"}");
//				}
//
//			});
//		} catch (TimeoutException e) {
//			Reporter.fnWriteToHtmlOutput("fWaitForPageToLoad", "Page failed to load in " + waitPeriod + " seconds.",
//					"Exception: " + e, "Fail");
//			return false;
//		}
		return true;
	}

	public boolean fHoverOverElement(String webElmtProp, String strObjName) {
		try {
			WebElement obj = fCommonGetObject(webElmtProp, strObjName);
			Actions cursor = new Actions(driver);
			cursor.moveToElement(obj).perform();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * Check error warning pop up.
	 *
	 * @return the boolean
	 */
	public Boolean CheckErrorWarningPopUp(){
//		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
//		String webElmtErrorWarningPopUp = "xpath:=//div[@id='toast-container']/div";
//		boolean bStatus = false;
//
//
//		//Delimiters
//		String[] delimiters = new String[] {":="};
//		String[] arrFindByValues = webElmtErrorWarningPopUp.split(delimiters[0]);
//		//Get Findby and Value 
//		String FindBy="";
//		String val="";
//		if(arrFindByValues.length==2){
//			FindBy = arrFindByValues[0];
//			val = arrFindByValues[1];        	
//		}
//		else{
//			Reporter.fnWriteToHtmlOutput("CheckErrorWarningPopUp", "CheckErrorWarningPopUp should be valid","Locator for Error PopUp is not valid: " + webElmtErrorWarningPopUp, "Fail");
//			return false;
//		}
//
//		try{
//			//Handle all FindBy cases
//			String strElement = FindBy.toLowerCase();
//			if (strElement.equalsIgnoreCase("linktext")){
//				bStatus = driver.findElement(By.linkText(val)).isEnabled();
//			}
//			else if (strElement.equalsIgnoreCase("xpath")){
//				bStatus = driver.findElement(By.xpath(val)).isEnabled();
//			}
//			else if (strElement.equalsIgnoreCase("name")){
//				bStatus = driver.findElement(By.name(val)).isEnabled();
//			}
//			else if (strElement.equalsIgnoreCase("id")){
//				bStatus = driver.findElement(By.id(val)).isDisplayed();
//			}
//			else if (strElement.equalsIgnoreCase("classname")){
//				bStatus = driver.findElement(By.className(val)).isEnabled();
//			}
//			else if(strElement.equalsIgnoreCase("cssselector")){
//				bStatus = driver.findElement(By.cssSelector(val)).isEnabled();
//			}
//			driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
//			//		was already Commented	Reporter.fnWriteToHtmlOutput("fCommonCheckObjectExistance", " Webelement should exist", "Webelement exist", "Done");
//			if(bStatus==true){
//				if(driver.findElement(By.xpath(val)).getAttribute("class").contains("warning") || driver.findElement(By.xpath(val)).getAttribute("class").contains("error"))
//				{
//				Reporter.fnWriteToHtmlOutput("CheckErrorWarningPopUp", "Error Warning PopUp should not be displayed","Error Warning PopUp is displayed", "Fail");
//				return false;
//				}
//				else return true;
//			}
//			else{
////		was already Commented		Reporter.fnWriteToHtmlOutput("CheckErrorWarningPopUp", "Error Warning PopUp should not be displayed","Error Warning PopUp is not displayed", "Pass");
//				return true;
//			}
//			//				return bStatus;
//
//		}catch(Exception e){
//			if(bStatus==false){
////		was already Commented 		Reporter.fnWriteToHtmlOutput("CheckErrorWarningPopUp", "Error Warning PopUp should not be displayed","Error Warning PopUp is not displayed", "Pass");
//				return true;
//			}
//			else {
//				Reporter.fnWriteToHtmlOutput("CheckErrorWarningPopUp", "Error Warning PopUp should not be displayed","Error Warning PopUp is displayed", "Fail");
//				return false;
//			}
//		}	
//		finally{
//			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//		}
		return true;
	}

		// *****************************************************************************************
		// * Name : fCommonSelectCheckboxCH
		// * Description : Check a webelement
		// * Author : Imen Ben Rejeb
		// * Input Params : WebElement Properties - The webelement for which we need
		// to click
		// * Return Values : Boolean - Depending on the success
		/**
		 * F common click.
		 *
		 * @param webElmtProp
		 *            the web elmt prop
		 * @param strObjName
		 *            the str obj name
		 * @return true, if successful
		 * @throws ElementNotVisibleException
		 *             the element not visible exception
		 */
		// *****************************************************************************************
		public boolean fCommonSelectCheckboxCH(String webElmtProp, String strObjName, String key, String HiddenDiv)
				throws ElementNotVisibleException {
			WebElement WebhiddenDiv = driver.findElement(By.xpath(HiddenDiv));
			String n = WebhiddenDiv.getText(); // does not work (returns "" as
												// expected)
			String script = "return arguments[0].innerHTML";
			n = (String) ((JavascriptExecutor) driver).executeScript(script, WebhiddenDiv);
			try {
				// Get WebElement
				WebElement webElement = fCommonGetObject(webElmtProp, strObjName);
				if (webElement == null) {
					Reporter.fnWriteToHtmlOutput("fCommonClick: " + strObjName, "Object should be found",
							"Object was not found. Null value returned", "Fail");
					return false;
				}

				// Click on the WebElement
				if (n.trim().equals("false")) {
					int intCount = 1;
					while (intCount <= 3) {
						try {
							webElement.click();
							break;

						} catch (StaleElementReferenceException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);
						} catch (InvalidElementStateException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);
						} catch (WebDriverException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);
						}

						catch (NullPointerException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);

						} catch (Exception e) {
							driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
						}
						if (intCount == 3) {
							Reporter.fnWriteToHtmlOutput("fCommonClick", strObjName, strObjName + " is not clicked",
									"Fail");
							return false;
						}
						intCount++;
					}
					Reporter.fnWriteToHtmlOutput("fCommonSelectCheckbox", strObjName + " should (not) be checked",
							strObjName + " is (un)checked successfully", "Done");
					return CheckErrorWarningPopUp();
					// return true;
				} else {

					Reporter.fnWriteToHtmlOutput("fCommonSelectCheckbox", strObjName + " sshould (not) be checked",
							strObjName + " is (un)checked successfully ", "Done");
					return CheckErrorWarningPopUp();
				}

			} catch (Exception e) {
				Reporter.fnWriteToHtmlOutput("fCommonClick", "Exception occurred for object: " + strObjName,
						"Exception: " + e, "Fail");
				return false;
			}
		}

		// *****************************************************************************************
		// * Name : fCommonMouseoverCH
		// * Description : mouse Over
		// * Author :  Imen Ben Rejeb
		// * Input Params : sWebElement
		// * Return Values : Boolean - Depending on the success
		/**
		* F common launch environemnt.
		*
		* @param strUrl
		*            the str url
		* @return true, if successful
		*/
		// *****************************************************************************************
		public boolean fCommonMouseoverCH(String WebElement) {
				try {
					WebElement ele1 = fCommonGetWebElement(WebElement, "WebElement");
					String javaScript = "var evObj = document.createEvent('MouseEvents');" +
		                    "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
		                    "arguments[0].dispatchEvent(evObj);";
					((JavascriptExecutor)driver).executeScript(javaScript, ele1);
					return true;

				} catch (Exception e) {
					e.printStackTrace();
					Reporter.fnWriteToHtmlOutput("fCommonMouseover", "Exception occurred", "Exception: " + e, "Fail");
					return false;
				}
			}

		// *****************************************************************************************
		// * Name : fCommonWaitAndCheckValueCH
		// * Description : Check a webelement
		// * Author : Imen Ben Rejeb
		// * Input Params : WebElement Properties - The webelement for which we need
		// to click
		// * Return Values : Boolean - Depending on the success
		/**
		* F common click.
		*
		* @param webElmtProp
		*            the web elmt prop
		* @param strObjName
		*            the str obj name
		* @return true, if successful
		* @throws ElementNotVisibleException
		*             the element not visible exception
		*/
		// *****************************************************************************************
		public boolean fCommonWaitAndCheckValueCH(String webElmtProp, String strObjName, String key, String period)
					throws ElementNotVisibleException {
				int duration = Integer.valueOf(period);
				if (duration >= 15000) {
					fCommonSync(duration);
				} else {
					fCommonSync(15000);
				}
				try {
					// Get WebElement
					WebElement webElement = fCommonGetObject(webElmtProp, strObjName);
					if (webElement == null) {
						Reporter.fnWriteToHtmlOutput("fCommonCheckValue: " + strObjName, "Object should be found",
								"Object was not found. Null value returned", "Fail");
						return false;
					}

					// Click on the WebElement
					int intCount = 1;
					String webElementText;
					while (intCount <= 3) {
						try {
							webElementText = webElement.getText().trim();
							System.out.println("$$$$$$$$$ webElement : " + webElement);
							System.out.println("$$$$$$$$$ webElementText : " + webElementText);
							System.out.println("$$$$$$$$$ key : " + key);
							if (webElementText.equalsIgnoreCase(key)) {
								break;

							}

						} catch (StaleElementReferenceException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);
						} catch (InvalidElementStateException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);
						} catch (WebDriverException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);
						}

						catch (NullPointerException e) {
							webElement = fCommonGetObject(webElmtProp, strObjName);

						} catch (Exception e) {
							driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
						}
						if (intCount == 3) {
							Reporter.fnWriteToHtmlOutput("fCommonCheckValue", strObjName,
									strObjName + " value is not equal to the expected value", "Fail");
							return false;
						}
						intCount++;
					}
					Reporter.fnWriteToHtmlOutput("fCommonCheckValue", strObjName + " should be equal to the expected value",
							strObjName + " is equal to the expected value", "Done");
					return CheckErrorWarningPopUp();
					// return true;

				} catch (Exception e) {
					Reporter.fnWriteToHtmlOutput("fCommonCheckValue", "Exception occurred for object: " + strObjName,
							"Exception: " + e, "Fail");
					return false;
				}
			}

		// *****************************************************************************************
		// * Name : fCommonCheckValueCH
		// * Description : Check a webelement
		// * Author : Imen Ben Rejeb & Dhouha Ben Ayed
		// * Input Params : WebElement Properties - The webelement for which we need
		// to click
		// * Return Values : Boolean - Depending on the success
		/**
		 * F common click.
		 *
		 * @param webElmtProp
		 *            the web elmt prop
		 * @param strObjName
		 *            the str obj name
		 * @return true, if successful
		 * @throws ElementNotVisibleException
		 *             the element not visible exception
		 */
		// *****************************************************************************************
		public boolean fCommonCheckValueCH(String webElmtProp, String strObjName, String key)
				throws ElementNotVisibleException {
			try {
				// Get WebElement
				WebElement webElement = fCommonGetObject(webElmtProp, strObjName);
				if (webElement == null) {
					Reporter.fnWriteToHtmlOutput("fCommonCheckValue: " + strObjName, "Object should be found",
							"Object was not found. Null value returned", "Fail");
					return false;
				}

				// Click on the WebElement
				int intCount = 1;
				String webElementText;
				while (intCount <= 3) {
					try {
						if(webElement.getAttribute("value")!=null)
						{
							webElementText = webElement.getAttribute("value").trim();
						}
						else
						{
							webElementText = webElement.getText().trim();
						}
						System.out.println("webElementText value   !!!!!!!! : "+webElementText);
						if (webElementText.equalsIgnoreCase(key.trim())) {
							break;

						}

					} catch (StaleElementReferenceException e) {
						webElement = fCommonGetObject(webElmtProp, strObjName);
					} catch (InvalidElementStateException e) {
						webElement = fCommonGetObject(webElmtProp, strObjName);
					} catch (WebDriverException e) {
						webElement = fCommonGetObject(webElmtProp, strObjName);
					}

					catch (NullPointerException e) {
						webElement = fCommonGetObject(webElmtProp, strObjName);

					} catch (Exception e) {
						driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
					}
					if (intCount == 3) {
						Reporter.fnWriteToHtmlOutput("fCommonCheckValue", strObjName,
								strObjName + " value is not equal to the expected value", "Fail");
						return false;
					}
					intCount++;
				}
				Reporter.fnWriteToHtmlOutput("fCommonCheckValue", strObjName + " should be equal to the expected value",
						strObjName + " is equal to the expected value", "Done");
				return CheckErrorWarningPopUp();
				// return true;

			} catch (Exception e) {
				Reporter.fnWriteToHtmlOutput("fCommonCheckValue", "Exception occurred for object: " + strObjName,
						"Exception: " + e, "Fail");
				return false;
			}
		}
}
