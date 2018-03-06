package com.ptc.integrity.automation.pageobjects.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.Alert;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

/**
 * The Class AddTrade.
 */
public class LoginPage extends PageObjectBase {

    /**
     * Instantiates a new adds the trade.
     *
     * @param pageObjectFactory the page object factory
     */
    //
    public LoginPage(PageObjectFactory pageObjectFactory) {
        super(pageObjectFactory, "xpath:=//form[@fis-unique-id='seleniumTitleAddTrade']");
    }

    public LoginPage(PageObjectFactory pageObjectFactory, String pageName) {
        super(pageObjectFactory, pageName);
    }

    private String webElementSelectExchange = "cssselector:=.fis-selected div[fis-unique-id='seleniumDDTradeExchange'] select";
    
    /**
	 * It will Launch the Application with the URL mentioned in the environment
	 * .
	 * 
	 * @return true / false whether application launch success or failure .
	 */
	public boolean LaunchApplication() {
		System.out.println("Launching URL  "+getEnvironment().get("URL"));
		return getCommonFunctions().fCommonLaunchEnvironemnt(
				getEnvironment().get("URL"));
	}

	/**
	 * Enter credentials.
	 * @nx.param "USER_ID" 
	 * @nx.param "PASSWORD" 
	 * @return true, if successful
	 */
	public boolean EnterCredentials() {
		return true;
	}
	
	
	
    /**
     * Check empty CCPAccount element
     *
     * @return - true if successful
     */
    public boolean CheckIsCCPAccountEmpty() {
        return getCommonFunctions().fCommonCheckObjectExistance(webElementSelectExchange);
    }
   
}
