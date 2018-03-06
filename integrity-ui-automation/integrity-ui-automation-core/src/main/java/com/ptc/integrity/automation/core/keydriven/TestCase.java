package com.ptc.integrity.automation.core.keydriven;

import java.util.List;

import com.ptc.integrity.automation.core.framework.CSVAdapter;

public class TestCase {

	int serialNo;
	String executionMode;
	String testCaseName;
	String skip;
	String URL;
	String jiraId;
	List<TestStep> testStepList;
	String workBookPath;
	int priority;
	String groupname;
	String sanityOrFull;

	public String getSanityOrFull() {
		return sanityOrFull;
	}

	public void setSanityOrFull(String sanityOrFull) {
		this.sanityOrFull = sanityOrFull;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getBatchId() {
		return executionMode;
	}

	public String getJiraId() {
		return jiraId;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public String getSkip() {
		return skip;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public List<TestStep> getTestStepList() {
		return testStepList;
	}

	public String getURL() {
		return URL;
	}

	public String getWorkBookPath() {
		return workBookPath;
	}

	public void setBatchId(final String batchId) {
		this.executionMode = batchId;
	}

	public void setJiraId(final String jiraId) {
		this.jiraId = jiraId.replace("-", "_");
	}

	public void setSerialNo(final int serialNo) {
		this.serialNo = serialNo;
	}

	public void setSkip(final String skip) {
		this.skip = skip;
	}

	public void setTestCaseName(final String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public void setTestStepList(final List<TestStep> testStepList) {
		this.testStepList = testStepList;
	}

	public void setURL(final String uRL) {
		URL = uRL;
	}

	public void setWorkBookPath(String workBookPath) {
		if(!CSVAdapter.isRemoteMode) {
			workBookPath = workBookPath.replace("\\", "/");  // handling path format
			//System.out.println("Test case path: " + workBookPath);
			//System.out.println(CSVAdapter.rootFrameworkPath);
			String formatedPath[]=null;
			if(workBookPath.contains("/integrity-ui-automation/")) {
				formatedPath = workBookPath.split("/integrity-ui-automation/");
			}else if(workBookPath.contains("/ui/")) {
				formatedPath = workBookPath.split("/ui/");
			}
			//System.out.println("Formated path is " + CSVAdapter.rootFrameworkPath + "ui/" + formatedPath[1]);
			if(formatedPath!=null)
//			this.workBookPath=CSVAdapter.rootFrameworkPath+"ui/"+formatedPath[1];
			this.workBookPath=workBookPath;
			else
				this.workBookPath=workBookPath;
		}else{
			//workBookPath = workBookPath.replace("\\", "/");     // handling path format
			this.workBookPath = workBookPath;
		}

	}

	@Override
	public String toString() {
		return "TestCase [serialNo=" + serialNo + ", BatchId=" + executionMode
				+ ", testCaseName=" + testCaseName + ", skip=" + skip
				+ ", URL=" + URL + ", jiraId=" + jiraId + ", testStepList="
				+ testStepList + ", workBookPath=" + workBookPath + "]";
	}

}
