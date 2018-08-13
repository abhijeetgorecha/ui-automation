package com.ptc.integrity.automation.pageobjects.pages;

import com.ptc.integrity.automation.core.framework.PageObjectFactory;

public class Overview extends PageObjectBase {

	/**
	 * Instantiates a new Overview
	 *
	 * @param pageObjectFactory the page object factory
	 */
	//
	public Overview(PageObjectFactory pageObjectFactory) {
		super(pageObjectFactory, "xpath:=//*[@id='projectDetailsDiv']");
	}

	public Overview(PageObjectFactory pageObjectFactory, String pageName) {
		super(pageObjectFactory, pageName);
	}
	
	private String webElmtProjectName="xpath:=//span[@class='project-details-values' and contains(text(),'%s')]";
	private String webElmtOwner="xpath:=//span[@class='project-details-values' and contains(text(),'%s')]";
	
	public Boolean VerifyProjectDeatils() {
		if(getCommonFunctions().fCommonCheckObjectExistance(String.format(webElmtProjectName, getDictionary().get("ProjectName"))) == false)
			return false;
		if(getCommonFunctions().fCommonCheckObjectExistance(String.format(webElmtOwner, getDictionary().get("Owner"))) == false)
			return false;
		return true;
	}
}
