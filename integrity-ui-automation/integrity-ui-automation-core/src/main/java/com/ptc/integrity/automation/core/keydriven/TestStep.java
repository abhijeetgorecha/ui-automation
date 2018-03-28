package com.ptc.integrity.automation.core.keydriven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStep {

	private int serialNo;
	private String browserId;
	private String pageObjectName;
	private String keyWord;
	private String outputKey;
	private String outputType;
	private String description;
	private Map<String, String> dictonary = new HashMap<String, String>();
	private List<Object> params = new ArrayList<Object>();
	private String ignore;

	public Map<String, String> getDictonary() {
		return dictonary;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public String getOutputKey() {
		return outputKey;
	}

	public String getOutputType() {
		return outputType;
	}

	public String getPageObjectName() {
		return pageObjectName;
	}

	public List<Object> getParams() {
		return params;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public String getBrowserId() {
		return browserId;
	}



	public String getDescription() {
//		description = getDictonary().get("Description");
		if(description==null){
			return "";
		}
		return description;
	}

	public void setBrowserId(String browserId) {
		this.browserId = browserId;
	}

	public void setDictonary(final Map<String, String> dictonary) {
		this.dictonary = dictonary;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	public void setOutputKey(final String outputKey) {
		this.outputKey = outputKey;
	}

	public void setOutputType(final String outputType) {
		this.outputType = outputType;
	}

	public void setPageObjectName(final String pageObjectName) {
		this.pageObjectName = pageObjectName;
	}

	public void setParams(final List<Object> params) {
		this.params = params;
	}

	public void setSerialNo(final int serialNo) {
		this.serialNo = serialNo;
	}
	
	public void setDescription(final String description) {
		getDictonary().put("Description", description);
		this.description = description;
	}

	@Override
	public String toString() {
		return "TestStep [serialNo=" + serialNo + ",ignore=" + ignore +",browserId=" + browserId +",stepDescription="+description
				+ ", pageObjectName=" + pageObjectName + ", keyWord=" + keyWord
				+ ", outputKey=" + outputKey + ", outputType=" + outputType
				+ ", dictonary=" + dictonary + "]";
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

}
