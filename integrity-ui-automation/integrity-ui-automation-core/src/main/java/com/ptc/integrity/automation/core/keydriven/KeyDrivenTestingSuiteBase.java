package com.ptc.integrity.automation.core.keydriven;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.ptc.integrity.automation.core.framework.CSVAdapter;
import com.ptc.integrity.automation.core.framework.CommonFunctions;
import com.ptc.integrity.automation.core.framework.ConfigurationManager;
import com.ptc.integrity.automation.core.framework.PageDictionary;
import com.ptc.integrity.automation.core.framework.PageObjectFactory;
import com.ptc.integrity.automation.core.framework.Reporting;

public class KeyDrivenTestingSuiteBase {
	final private static Logger LOGGER = Logger
			.getLogger(ExecutionEngine.class);
	protected HashMap<String, String> Environment = new HashMap<String, String>();
	protected PageDictionary Dictionary = new PageDictionary();
	Reporting Reporter;
	private volatile int testPassed;
	private volatile int testNumber = -1;
	protected CSVAdapter adapter;
	WebDriver driver;

	protected String driverType;
	protected String sessionType;
	protected CommonFunctions objCommon;
	String env;
	protected Configuration config = ConfigurationManager.getInstance()
			.getConfig();
	private PageObjectFactory pageObjectFactory;

	protected Map<String, TestCase> testCaseMap;
	public ArrayList<String> failCount=new ArrayList<String>();
	

	@AfterClass(alwaysRun=true)
	public void afterClass() {
//		final File file = new File(Environment.get("STATICJSONPATH")
//				+ File.separator + "to-send");
//		final File dtofile = new File(Environment.get("DTOFILESPATH")
//				+ File.separator + "to-send");
//		try {
//			FileUtils.cleanDirectory(file);
//			FileUtils.cleanDirectory(dtofile);
//		} catch (final IOException e1) {
//			e1.printStackTrace();
//		}
		try {
			// KeyDrivenTestCasesSuiteRunnerCI objSuite= new
			// KeyDrivenTestCasesSuiteRunnerCI();
			Reporter.setG_iTCPassed(getTestPassed());
			Reporter.fnCloseTestSummary();
		} catch (final Exception e) {
			System.out.println("Exception " + ": " + e.toString());
		}
		System.out.println("Total Test Count:  "+(getTestPassed()+failCount.size()));
		System.out.println("Total Test Failed Count:  "+failCount.size());
		System.out.println("Failed Test Cases :  "+failCount);
		if(config.getString("ui.automation.sanity.or.full").isEmpty())
			sessionType="Full";
		else
			sessionType=config.getString("ui.automation.sanity.or.full");
		System.out.println("Test Session is: "+ sessionType+" pass");
	}

	@Parameters({ "browser", "envcode", "objectFactory" })
	@BeforeClass(alwaysRun=true)
	public void beforeClass(@Optional("") final String browser,
			@Optional("") final String runOnEnv,
			@Optional("") final String objectFactory) {
		try {
			driverType = browser;
			Environment.put("CLASS_NAME", this.getClass().getSimpleName());
			Environment.put("node", System.getProperty("node"));
			if (objectFactory == null || !isObjectFactoryExist(objectFactory)) {
				throw new IllegalArgumentException(
						"Object Factory class not found [" + objectFactory
								+ "]");
			}
			Environment.put("objectFactory", objectFactory);
			adapter = new CSVAdapter(driverType, Environment, null, null,
					config);
			env = System.getProperty("envName");
			if (env == null) {
				env = runOnEnv;
			}
			Assert.assertNotNull(env);
			// Add env global environments
			Environment.put("ENV_CODE", env);

			final String className = Environment.get("CLASS_NAME");
			
			String driverPath = null;
			String suiteType=config.getString("ui.automation.sanity.or.full");
			// if (config.getBoolean("ui.mode.remoteMode")) {
			// driverPath = config.getString("ui.automation.root.path")
			// + File.separator+"TestSuite"+File.separator+
			// System.getProperty("suiteName")
			// + ".csv";
			// } else {
			// driverPath = config
			// .getString(KeywordDrivenConstant.DRIVER_SHEET_PATH);
			// }

			if (System.getProperty("suiteName") == null
					|| (System.getProperty("suiteName")).length() == 0) {
				driverPath = config
						.getString(KeywordDrivenConstant.DRIVER_SHEET_PATH);
			} else {
				driverPath = config.getString("ui.automation.root.path")
						+ File.separator + "TestSuite" + File.separator
						+ System.getProperty("suiteName") + ".csv";
			}

			LOGGER.info("Executing Test Suite ="+driverPath);

			final CSVReaderImpl csvReader = new CSVReaderImpl(
					driverPath,
					config.getString(KeywordDrivenConstant.ENVIRONMENT_SHEET_PATH));
			// /////////

			final Map<String, String> environment = csvReader
					.fetchEnvironmentDetails(env);
			Environment.putAll(environment);
			adapter.createExecutionFolders(className);
			Reporter = new Reporting(driver, driverType, Environment);
			Reporter.fnCreateSummaryReport(suiteType);

			objCommon = new CommonFunctions(driver, driverType, Environment,
					Reporter);
//			objCommon.fEditAndCopyStaticJsonFiles();
//			objCommon.fEditDTOFiles();

			testCaseMap = new LinkedHashMap<String, TestCase>();

			final Driver driver = csvReader.getDriver();
			final List<TestCase> sequentialTestCaseList = driver
					.getSequentialTestCases();
			Iterator<TestCase> it = sequentialTestCaseList.iterator();
			while (it.hasNext()) {
				TestCase test = it.next();
				testCaseMap.put(test.getTestCaseName(), test);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isObjectFactoryExist(String objectFactory) {
		boolean isExist = true;
		try {
			Class.forName(objectFactory);
		} catch (Exception e) {
			e.printStackTrace();
			isExist = false;
		}
		return isExist;
	}

	public void createIntegrityPageObjectFactory(final WebDriver webDriver,
			final String driverType, final PageDictionary Dictionary,
			final Map<String, String> Env, final Reporting Report) {
		Object[] constructorParam = { webDriver, driverType, Dictionary, Env,
				Report };
		try {
			Object pageObject = ConstructorUtils.invokeConstructor(
					Class.forName(Env.get("objectFactory")), constructorParam);
			if (pageObject instanceof PageObjectFactory) {
				pageObjectFactory = (PageObjectFactory) pageObject;
			} else {
				throw new IllegalArgumentException(
						"Invalid Object Factory class");
			}
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

	public PageObjectFactory getPageObjectFactory() {
		return pageObjectFactory;
	}

	public int getTestPassed() {
		return testPassed;
	}

	protected synchronized void incrementTestNumberCount() {
		setTestNumber(getTestNumber() + 1);
	}

	public int getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	protected synchronized void incrementTestPassCount() {
		setTestPassed(getTestPassed() + 1);
	}

	public void setTestPassed(int testPassed) {
		this.testPassed = testPassed;
	}
}
