package com.ptc.integrity.automation.tests.keydriven;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import com.ptc.integrity.automation.core.framework.CommonFunctions;
import com.ptc.integrity.automation.core.framework.PageDictionary;
import com.ptc.integrity.automation.core.framework.Reporting;
import com.ptc.integrity.automation.core.keydriven.ExecutionEngine;
import com.ptc.integrity.automation.core.keydriven.KeyDrivenTestingSuiteBase;
import com.ptc.integrity.automation.core.keydriven.KeywordDrivenConstant;
import com.ptc.integrity.automation.core.keydriven.TestCase;

/**
 * Test Cases runner class which start running of the test case.
 */
public class KeyDrivenTestCasesSuiteRunnerCI extends KeyDrivenTestingSuiteBase {
	final private static Logger LOGGER = Logger
			.getLogger(ExecutionEngine.class);
  
	/** The local driver. */
	private final ThreadLocal<WebDriver> localDriver = new ThreadLocal<WebDriver>();
	
	private final Map<String,WebDriver> driverCache = new ConcurrentHashMap<String,WebDriver>();

	/** The local reporter. */
	private final ThreadLocal<Reporting> localReporter = new ThreadLocal<Reporting>();
	
	private final Map<String,Reporting> reporterCache = new ConcurrentHashMap<String,Reporting>();
	
		
	/** The counter pass. */
	public int counterPass = 0;

	/**
	 * After test method.
	 * 
	 * @param testResultStatus
	 *            test result status whether test is fail or pass .
	 */
	public synchronized void afterTest(final boolean testResultStatus,final String testCaseName) {

		try {
			if (("Y").equalsIgnoreCase(config
					.getString("ui.automation.csv.update"))) {
				if (testResultStatus) {
					Dictionary.put("RESULT_" + driverType, "P");
				} else if (testResultStatus) {
					Dictionary.put("RESULT_" + driverType, "F");
				} else {
					Dictionary.put("RESULT_" + driverType, "N");

				}

			}
			// Close Summary Report
			//Get Chrome console logs if test fail
			String FilePath="";
			if (!testResultStatus) {
				 
				WebDriver d = driverCache.get(testCaseName);
				LogEntries logEntries = d.manage().logs().get(LogType.BROWSER);
				 for (LogEntry entry : logEntries) {
					 FilePath=Environment.get("HTMLREPORTSPATH")+File.separator+"ConsoleLogs";
					 Files.createDirectories(Paths.get(FilePath));
					 File file = new File(FilePath+File.separator+testCaseName+".txt");
					 FileWriter fileWriter=null;
					try {
						fileWriter = new FileWriter(file,true);
						fileWriter.write(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
						 fileWriter.write("\r\n");
						 fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					 
			            System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
			            //do something useful with the data
			        }
			
			}
		//	localReporter.get().setG_iTestCaseNo(getTestNumber());
			//localReporter.get().fnCloseHtmlReport();
			reporterCache.get(testCaseName).setG_iTestCaseNo(getTestNumber());
			if (!testResultStatus) {
			reporterCache.get(testCaseName).fnCloseHtmlReport(FilePath+File.separator+testCaseName+".txt");
			xmlReport.addTestCasetoXML(testCaseName, "Fail", xmlOutputFile,testTotalTime);
			}
			else{
				reporterCache.get(testCaseName).fnCloseHtmlReport("");
				xmlReport.addTestCasetoXML(testCaseName, "Pass", xmlOutputFile,testTotalTime);
			}		
 			System.out.println("AfterMethod end - " + driverType);

		} catch (final Exception e) {
			System.out.println("exception "
					+ Dictionary.get("TEST_NAME_" + driverType) + driverType
					+ ": " + e.toString());
		}

		// Quit Driver
		closeDriverQuietly(testCaseName);

	}

	/**
	 * Before test method.
	 * 
	 * @param testCase
	 *            test case to be executed
	 */
	public synchronized void beforeTest(final TestCase testCase) {
		PageDictionary dictonary = new PageDictionary();
 
		dictonary.put(KeywordDrivenConstant.TEST_ID,
				testCase.getJiraId());
		dictonary.put(KeywordDrivenConstant.TEST_NAME,
				testCase.getTestCaseName());
		dictonary.put(KeywordDrivenConstant.GROUP_NAME,
        				testCase.getGroupname());
		adapter.setDictionary(dictonary);
		WebDriver webDriver = null;
		try {
			webDriver = adapter.fGetWebDriver(driverType,
					config.getString(KeywordDrivenConstant.DRIVER_PATH));
			//localDriver.set(webDriver);
			//System.out.println("Push webdriver of "+testCase.getTestCaseName());
			driverCache.put(testCase.getTestCaseName(), webDriver);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		final Reporting reporter = new Reporting(webDriver, driverType,
				Environment);
		reporter.setDictionary(dictonary);
		reporter.driver = webDriver;
		//localReporter.set(reporter);
		reporterCache.put(testCase.getTestCaseName(), reporter);
		reporter.fnCreateHtmlReport(dictonary
				.get(KeywordDrivenConstant.TEST_NAME));
		objCommon = new CommonFunctions(webDriver, driverType, Environment,
				reporter);
		dictonary.put("TEST_NAME_" + driverType,
				dictonary.get(KeywordDrivenConstant.TEST_NAME));

		createIntegrityPageObjectFactory(webDriver, driverType, dictonary,
				Environment, reporter);
	}

	/**
	 * Close driver instance.
	 */
	public void closeDriverQuietly(final String testCaseName) {

		//final WebDriver driver = localDriver.get();
		//System.out.println("pull webdriver of "+testCaseName);
		final WebDriver driver = driverCache.get(testCaseName);
		if (driver != null) {
			try {
				driver.quit();
			} catch (final Exception e) {
			}

		}
		// remove thread local variable
		//localDriver.remove();
		//localReporter.remove();
		driverCache.remove(testCaseName);
		reporterCache.remove(testCaseName);
	}

	/**
	 * Method to initiate execution engine and submit test to be executed.
	 * 
	 * @param testCase
	 *            the test case
	 */
	private void runTest(final TestCase testCase) {

		final ExecutionEngine engine = new ExecutionEngine();
		// before test
		beforeTest(testCase);

		final boolean testResultStatus = engine.executeTest(testCase,
				getPageObjectFactory());

		// after test
		afterTest(testResultStatus,testCase.getTestCaseName());

	}
	
	public int getTestPassCount(){
	return counterPass;
	}

	/**
	 * Method to initiate execution engine and submit batch of test cases to be
	 * executed.
	 * 
	 * @param testCases
	 *            List of test cases .
	 */
	 
	<#list testList as testcase>
		@Test(groups = {"${testcase.groupname}"},priority = ${testcase.priority})
		@Parameters({ "browser" })
			public void ${testcase.jiraId}$${testcase.testCaseName}(@Optional("") final String browser){				
					
			final ExecutionEngine engine = new ExecutionEngine();
			final TestCase testCase = testCaseMap.get("${testcase.testCaseName}");
			boolean testResultStatus = false;
			try {
				// before test
				beforeTest(testCase);
				//System.out.println("driver map : "+driverCache);
				LOGGER.info("Start Executing Test Case : [ "
						+ testCase.getTestCaseName()+" ]");
				System.out.println("Executing test case : "+testCase.getTestCaseName());
				testResultStatus = engine.executeTest(testCase,
						getPageObjectFactory());
				// increment test pass/fail count that will be used in summary
				// report .
				if (testResultStatus) {
					incrementTestPassCount();
					counterPass++;
				}
				incrementTestNumberCount();
				LOGGER.info("Completed Executing Test Case : [ "
						+ testCase.getTestCaseName()+" ]");
			} catch (final Exception ex) {
				ex.printStackTrace();
			} finally {
				// after test
				if(testResultStatus==false){
				reporterCache.get("${testcase.testCaseName}").fnWriteToHtmlOutput("${testcase.testCaseName}","Test case should gets pass","Test case Failed" , "Fail");
				}
				else{
					reporterCache.get("${testcase.testCaseName}").fnWriteToHtmlOutput("${testcase.testCaseName}","Test case should gets pass","Test case Passed" , "Pass");	
				}
				afterTest(testResultStatus,testCase.getTestCaseName());
				Assert.assertTrue(testResultStatus);
			}

				
	
			}
								
    </#list>
	 
}
