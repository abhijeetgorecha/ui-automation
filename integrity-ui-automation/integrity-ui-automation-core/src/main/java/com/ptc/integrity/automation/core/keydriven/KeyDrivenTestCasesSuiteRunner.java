package com.ptc.integrity.automation.core.keydriven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import org.testng.internal.thread.ThreadUtil;

import com.ptc.integrity.automation.core.framework.CommonFunctions;
import com.ptc.integrity.automation.core.framework.PageDictionary;
import com.ptc.integrity.automation.core.framework.Reporting;

/**
 * Test Cases runner class which start running of the test case.
 */
public class KeyDrivenTestCasesSuiteRunner extends KeyDrivenTestingSuiteBase {
	final private static Logger LOGGER = Logger
			.getLogger(ExecutionEngine.class);
	private TestStep testStep = new TestStep();
	private Date testStartTime;
	private Date testEndTime;


	/**
	 * 
	 * Worker class to run batch of test cases
	 * 
	 */
	/**
	 * Worker class to run batch of test cases.
	 */
	private class SuiteBatchWorker implements Runnable {

		/** The test cases. */
		private final List<TestCase> testCases;

		/**
		 * Instantiates a new suite batch worker.
		 * 
		 * @param testCases
		 *            the test cases
		 */
		public SuiteBatchWorker(final List<TestCase> testCases) {
			this.testCases = testCases;
		}

		@Override
		public void run() {
			runTestCases(testCases);
		}
	}

	/**
	 * Worker class to one dedicated test cases.
	 */
	private class SuiteDedicatedWorker implements Runnable {

		/** The test case. */
		private final TestCase testCase;

		/**
		 * Instantiates a new suite dedicated worker.
		 * 
		 * @param testCase
		 *            the test case
		 */
		public SuiteDedicatedWorker(final TestCase testCase) {
			this.testCase = testCase;
		}

		@Override
		public void run() {
			runTest(testCase);
		}
	}

	/** The local driver. */
	private final ThreadLocal<WebDriver> localDriver = new ThreadLocal<WebDriver>();

	private final Map<String,WebDriver> driverCache = new ConcurrentHashMap<String,WebDriver>();

	/** The local reporter. */
	private final ThreadLocal<Reporting> localReporter = new ThreadLocal<Reporting>();

	private final Map<String,Reporting> reporterCache = new ConcurrentHashMap<String,Reporting>();

	/**
	 * After test method.
	 * 
	 * @param testResultStatus
	 *            test result status whether test is fail or pass .
	 */
	public synchronized void afterTest(final boolean testResultStatus,final String testCaseName) {
		testEndTime = new Date();
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
			//	localReporter.get().setG_iTestCaseNo(getTestNumber());
			//localReporter.get().fnCloseHtmlReport();
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
			//Chrome console logs code end
			reporterCache.get(testCaseName).setG_iTestCaseNo(getTestNumber());
			String testTotalTime=Reporter.fnTimeDiffference(testStartTime.getTime(), testEndTime.getTime());
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
		testStartTime = new Date();
		PageDictionary dictonary = new PageDictionary();

		dictonary.put(KeywordDrivenConstant.TEST_ID,
				testCase.getJiraId());
		dictonary.put(KeywordDrivenConstant.TEST_NAME,
				testCase.getTestCaseName());
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
		afterTest(testResultStatus,testCase.getTestCaseName());

	}

	/**
	 * Method to initiate execution engine and submit batch of test cases to be
	 * executed.
	 * 
	 * @param testCases
	 *            List of test cases .
	 */
	private void runTestCases(final List<TestCase> testCases) {
		final Iterator<TestCase> testIterator = testCases.iterator();
		while (testIterator.hasNext()) {
			final ExecutionEngine engine = new ExecutionEngine();
			final TestCase testCase = testIterator.next();
			boolean testResultStatus = false;
			try {
				// before test
				beforeTest(testCase);
				//System.out.println("driver map : "+driverCache);
				LOGGER.info("Start Executing Test Case : [Name= "+ testCase.getTestCaseName()+" JIRA ID="+testCase.getJiraId()+" ]");
				System.out.println("Executing test case : "+testCase.getTestCaseName());
				testResultStatus = engine.executeTest(testCase,
						getPageObjectFactory());
				// increment test pass/fail count that will be used in summary
				// report .
				if (testResultStatus) {
					reporterCache.get(testCase.getTestCaseName()).fnWriteToHtmlOutput(testCase.getTestCaseName(),"Test case should gets pass","Test case Passed" , "Pass");
					incrementTestPassCount();
				}
				else{
					reporterCache.get(testCase.getTestCaseName()).fnWriteToHtmlOutput(testCase.getTestCaseName(),"Test case should gets pass","Test case Failed" , "Fail");
					failCount.add(testCase.getTestCaseName());
				}
				incrementTestNumberCount();
				LOGGER.info("Completed Executing Test Case : [ "
						+ testCase.getTestCaseName()+" ]");
			} catch (final Exception ex) {
				ex.printStackTrace();
			} finally {
				// after test
				afterTest(testResultStatus,testCase.getTestCaseName());
			}

		}

	}

	/**
	 * Test runner.
	 * 
	 * @param browser
	 *            the browser
	 */
	@Test
	@Parameters({ "browser" })
	public void testRunner(@Optional("") final String browser) {

		//		final CSVReaderImpl csvReader = new CSVReaderImpl(
		//				config.getString(KeywordDrivenConstant.DRIVER_SHEET_PATH),config.getString(KeywordDrivenConstant.ENVIRONMENT_SHEET_PATH));

		String driverPath=null;
		if (config.getBoolean("ui.mode.remoteMode")) {
			driverPath = System.getProperty("suitename");
		} else {
			driverPath = config
					.getString(KeywordDrivenConstant.DRIVER_SHEET_PATH);
		}
		final CSVReaderImpl csvReader = new CSVReaderImpl(driverPath,
				config.getString(KeywordDrivenConstant.ENVIRONMENT_SHEET_PATH));
		///////////
		final int poolSize = config.getInt(KeywordDrivenConstant.POOL_SIZE);
		// get driver object which contains all the test cases .
		final Driver driver = csvReader.getDriver();
		// get all batch test cases
		final List<List<TestCase>> batchTestCases = driver.getBatchTestCases();
		// get all sequential test cases
		final List<TestCase> sequentialTestCaseList = driver
				.getSequentialTestCases();

		// create worker of batch test cases .
		final Iterator<List<TestCase>> testCaseIterator = batchTestCases
				.iterator();
		final List<Runnable> tasks = Lists.newArrayList(batchTestCases.size());
		while (testCaseIterator.hasNext()) {
			tasks.add(new SuiteBatchWorker(testCaseIterator.next()));
		}
		// run sequential test cases .
		runTestCases(sequentialTestCaseList);

		// submit task to run in parallel . its a blocking method which block
		// the execution on below line until all thread get finished .
		ThreadUtil.execute("testRunner",tasks, poolSize, 0, false);


	}

}
