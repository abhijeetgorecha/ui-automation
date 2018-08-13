package com.ptc.integrity.automation.core.keydriven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;
import com.ptc.integrity.automation.core.framework.ConfigurationManager;

/**
 * Excel reader component for reader input excel files.
 */
public class CSVReaderImpl {

	/** The file path. */
	private String DriverfilePath;

	private String EnvfilePath;

	private String logPageObjectName;

	private String logCSVName;

	private String logKeywordName;

	protected Configuration config = ConfigurationManager.getInstance()
			.getConfig();

	/**
	 * Object repository instance which hold mapping of page object key and
	 * value .
	 */
	final private ObjectRepository repository = ObjectRepository
			.getRepository();

	public CSVReaderImpl(final String driverfilePath,
			final String environmentfilePath) {
		this.DriverfilePath = driverfilePath;
		this.EnvfilePath = environmentfilePath;
	}

	/**
	 * Method to fetch environment details.
	 * 
	 * @param EnvName
	 *            environment name
	 * @return Map of environment
	 */
	public Map<String, String> fetchEnvironmentDetails(final String EnvName) {

		//Config settings
		final ConfigurationManager configManager = ConfigurationManager
				.getInstance();
		configManager.setConfigPath(System.getProperty("user.dir")
				+ File.separator + "integrity-ui-automation.properties");
		final Configuration config = configManager.getConfig();

		final Map<String, String> environment = new HashMap<String, String>();

		if(config.getBoolean("ui.automation.execution.jar")==true){
			EnvfilePath=System.getProperty("user.dir") +File.separator+"TestSuite";
			EnvfilePath=EnvfilePath.replace("src\\main\\resources\\", "");
		}

		BufferedReader br = null;
		String headers[] = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			if(config.getBoolean("ui.automation.execution.jar")==true){
				br = new BufferedReader(new FileReader(EnvfilePath+File.separator+"Environment.csv"));
			}
			else{
				br = new BufferedReader(new FileReader(EnvfilePath));
			}
			String text = br.readLine();
			headers = text.split(",");
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		boolean bFlag = false;
		try {
			if(config.getBoolean("ui.automation.execution.jar")==true){
				br = new BufferedReader(new FileReader(EnvfilePath+File.separator+"Environment.csv"));
			}
			else{
				br = new BufferedReader(new FileReader(EnvfilePath));
			}
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] data = line.split(cvsSplitBy);
				// Check if the version and the environment value is matching
				final String strEnvironment = data[0].trim().toUpperCase();
				// Currently checking only on the basis of environment
				if (!strEnvironment.equals(EnvName)) {
					continue;
				}

				// Set the flag value to true
				bFlag = true;
				String strKey = "";
				String strValue = "";
				// Loop through all the columns
				for (int iCell = 0; iCell < data.length; iCell++) {
					strKey = headers[iCell];
					strValue = data[iCell].trim();

					if (config.getBoolean("ui.mode.remoteMode")) {
						if (strKey.trim().equalsIgnoreCase("URL")) {
							environment.put(strKey.trim(),
									System.getProperty("targetURL"));
							continue;
						}
						if (strKey.trim().equalsIgnoreCase("USER_NAME")) {
							environment.put(strKey.trim(),
									System.getProperty("userName"));
							continue;
						}
						if (strKey.trim().equalsIgnoreCase("PASSWORD")) {
							environment.put(strKey.trim(),
									System.getProperty("password"));
							continue;
						}
						environment.put(strKey.trim(), strValue.trim());
					} else if (config.getBoolean("ui.mode.remoteMode") == false) {
						environment.put(strKey.trim(), strValue.trim());
					}
				}
				break;
			}
			// If bFlag is true
			if (bFlag == false) {
				System.out.println("Environment Code "
						+ environment.get("ENV_CODE")
						+ " not found in the Environment xls");
				return null;
			}
		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e1) {
			e1.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}
		return environment;

	}

	/**
	 * Get driver instance which contains list of test case to be executed .
	 * 
	 * @return driver instance
	 */

	public Driver getDriver() {

		final Driver driver = new Driver();
		final List<List<TestCase>> batchTestCases = new ArrayList<List<TestCase>>();
		final List<TestCase> sequentialTestCases = new ArrayList<TestCase>();
		updateDriver(batchTestCases, sequentialTestCases);
		driver.setBatchTestCases(batchTestCases);
		driver.setSequentialTestCases(sequentialTestCases);
		return driver;



	}

	/**
	 * Get list of test cases .
	 * 
	 * @return List of TestCase
	 */
	public List<TestCase> getTestCases() {
		final List<TestCase> testCases = new ArrayList<TestCase>();

		BufferedReader br = null;
		String steps[] = null;
		String line = "";
		if(config.getBoolean("ui.automation.execution.jar")==true){
			DriverfilePath= config
					.getString(KeywordDrivenConstant.DRIVER_SHEET_PATH).replace("\\src\\main\\resources", "");
		}
		if(DriverfilePath.contains("->"))
		{
			String testcasename = DriverfilePath.split("->")[1].replace(".csv", "");

			// set DriverfilePath
			DriverfilePath = DriverfilePath.split("->")[0].replace(".csv", "") + ".csv";
			try {
				br = new BufferedReader(new FileReader(DriverfilePath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				skipLine(br);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int x = 0;
			try {
				while ((line = br.readLine()) != null) {
					if(line.contains(testcasename))
					{
						final TestCase testCase = new TestCase();
						steps = line.split(",");
						testCase.setPriority(1);

						testCase.setSanityOrFull(steps[1]);
						testCase.setTestCaseName(steps[2]);

						testCase.setSkip(steps[3]);

						testCase.setJiraId(steps[4]);

						testCase.setURL(steps[5]);

						testCase.setGroupname(steps[6]);

						testCase.setWorkBookPath(steps[7]);
						final Map<String, String> previousDictonary = new HashMap<String, String>();
						List<TestStep> testSteps = getTestStepsByPath(
								StringUtils.trimToEmpty(testCase.getWorkBookPath()),
								previousDictonary);
						testCase.setTestStepList(testSteps);

						testCases.add(testCase);

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else{
			int i = 1;
			try {
				if(config.getBoolean("ui.automation.execution.jar")==true){
					DriverfilePath=config
							.getString(KeywordDrivenConstant.DRIVER_SHEET_PATH).replace("\\src\\main\\resources", "");
				}
				br = new BufferedReader(new FileReader(DriverfilePath));
				skipLine(br);
				while ((line = br.readLine()) != null) {
					final TestCase testCase = new TestCase();
					steps = line.split(",");
					final String skipFlag = steps[3];
					String sanityOrFull = steps[1];
					if(sanityOrFull.isEmpty() || config.getString("ui.automation.sanity.or.full").equalsIgnoreCase("Full"))
						sanityOrFull="Full";

					if (StringUtils.isNoneEmpty(skipFlag)
							&& skipFlag
							.equalsIgnoreCase(KeywordDrivenConstant.SKIP_FLAG_OFF)
									&& sanityOrFull
									.equalsIgnoreCase(config.getString("ui.automation.sanity.or.full"))) {
						testCase.setPriority(i);
						testCase.setSanityOrFull(steps[1]);


						testCase.setTestCaseName(steps[2]);

						testCase.setSkip(steps[3]);

						testCase.setJiraId(steps[4]);

						testCase.setURL(steps[5]);

						testCase.setGroupname(steps[6]);

						if(config.getBoolean("ui.automation.execution.jar")==true){
							String[] arrSteps = steps[7].split("resources");
							System.out.println("#@#@#@#@ user dir path in csvreader" +System.getProperty("user.dir"));
							steps[7]=System.getProperty("user.dir")+File.separator+arrSteps[1];
						}
						testCase.setWorkBookPath(steps[7]);
						List<TestStep> testSteps=null;
						final Map<String, String> previousDictonary = new HashMap<String, String>();

						testSteps = getTestStepsByPath(
								StringUtils.trimToEmpty(testCase.getWorkBookPath()),
								previousDictonary);

						testCase.setTestStepList(testSteps);

						testCases.add(testCase);

						i++;
					}

				}

			} catch (final FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (final IOException e1) {
				e1.printStackTrace();
			} finally {
				IOUtils.closeQuietly(br);
			}
		}
		return testCases;
	}

	/**
	 * Get List of test step for a testCase by excel path and sheet name .
	 * 
	 * @param csvPath
	 *            csv path location .
	 * @param previousDictonary
	 *            the previous dictonary
	 * @return List of TestStep
	 */
	public List<TestStep> getTestStepsByPath(String csvPath,
			final Map<String, String> previousDictonary) {
		List<TestStep> testSteps = null;

		BufferedReader br = null;

		try {
			// Create the FileInputStream object

			if (config.getBoolean("ui.mode.remoteMode")) {
				String folder[] = csvPath.split("/");
				String folderName = folder[folder.length - 2];
				String baseResourceDir = null;
//				if (System.getProperty("user.dir").contains(
//						"integrity-ui-automation-tests")) {
//					baseResourceDir = FilenameUtils.separatorsToSystem(System
//							.getProperty("user.dir")
//							+ "\\src\\main\\resources\\");
//				} else {
//					baseResourceDir = FilenameUtils
//							.separatorsToSystem(System.getProperty("user.dir")
//									+ "\\integrity-ui-automation-tests\\src\\main\\resources\\");
//				}
				System.out.println("########## csvreadre user dir path"+ System.getProperty("user.dir"));
				baseResourceDir = System.getProperty("user.dir")+File.separator+"\\integrity-ui-automation\\integrity-ui-automation-tests\\src\\main\\resources\\";
				csvPath = FilenameUtils.separatorsToSystem(baseResourceDir
						+ "Testcases" + File.separatorChar + folderName
						+ File.separatorChar + FilenameUtils.getName(csvPath));
			}

			if(config.getBoolean("ui.automation.execution.jar")==true){
				csvPath= csvPath.replace("\\src\\main\\resources", "");
				String tempcsvPath[]=csvPath.split("Testcases");
				csvPath=System.getProperty("user.dir")+File.separator+"Testcases"+File.separator+tempcsvPath[1];
			}
			System.out.println("######## csvPath Path  "+csvPath);
			// br = new BufferedReader(new FileReader(excelPath));
			testSteps = new ArrayList<TestStep>();

			populateStep(csvPath, testSteps, previousDictonary);

		} finally {
			IOUtils.closeQuietly(br);
		}
		return testSteps;
	}

	/**
	 * Get List of test step for a testCase by csv path .
	 * 
	 * @param csvPath
	 *            csv Path
	 * @param testSteps
	 *            test Steps
	 * @param previousDictonary
	 *            the previous dictonary
	 * @return List of TestStep
	 */
	private List<TestStep> populateStep(final String csvPath,
			List<TestStep> testSteps, Map<String, String> previousDictonary) {

		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(csvPath));

			List<String[]> steps = csvReader.readAll();
			csvReader.readNext();
			for (int i = 1; i < steps.size(); i++) {

				String pageObjectName = steps.get(i)[4];
				logPageObjectName= pageObjectName;
				String keyword = steps.get(i)[5];
				logKeywordName= keyword;
				logCSVName=csvPath;

				if (this.isCustomPageObject(pageObjectName)&& !pageObjectName.equalsIgnoreCase("")) {

					logPageObjectName=logPageObjectName+"_IsACustomKeyword";
					previousDictonary = this.getDictionary(steps.get(i),
							previousDictonary);
					List<TestStep> customTestSteps =null;

					if(config.getBoolean("ui.automation.execution.jar")==true){

						customTestSteps = getTestStepsByPath(FilenameUtils.separatorsToSystem(System
								.getProperty("user.dir")
								+ "\\CustomKeywords\\" + keyword + KeywordDrivenConstant.EXTENTION_EXCEL),previousDictonary);
					}
					else{
						customTestSteps = getTestStepsByPath(FilenameUtils.separatorsToSystem(System
								.getProperty("user.dir")
								+ "\\src\\main\\resources\\CustomKeywords\\" + keyword + KeywordDrivenConstant.EXTENTION_EXCEL),previousDictonary);
					}
					testSteps.addAll(customTestSteps);
					previousDictonary.clear();
				}
				else{
					this.populate(steps.get(i), testSteps,
							previousDictonary);
				}
			}

		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e1) {
			e1.printStackTrace();
		} finally {
			//			System.out.println("*** Path for csv " + logCSVName);
			//			System.out.println("*** Page object "+ logPageObjectName);
			//			System.out.println("*** keyword name "+ logKeywordName);
			//			IOUtils.closeQuietly(csvReader);
			try {
				csvReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Not Able to close csvReader Instance in class: " + this.getClass().getSimpleName());
				e.printStackTrace();
			}
		}
		return testSteps;
	}

	/**
	 * Checks if is custom page object.
	 * 
	 * @param pageObjectName
	 *            the page object name
	 * @return true, if is custom page object
	 */
	public boolean isCustomPageObject(final String pageObjectName) {

		return !repository.containsKey(pageObjectName);
	}

	/**
	 * Populate all steps into List of TestStep.
	 * 
	 * @param testSteps
	 *            the test steps
	 * @param previousDictonary
	 *            the previous dictonary
	 * @return List of TestStep
	 */
	public void populate(final String steps[], final List<TestStep> testSteps,
			final Map<String, String> previousDictonary) {

		final TestStep testStep = new TestStep();

		if (steps.length > 4 && !steps[4].equalsIgnoreCase("")) {
			// System.out.println(steps[1] + steps[2] + steps[3]);
			testStep.setIgnore(steps[1]);
			testStep.setBrowserId(steps[2]);
			testStep.setSerialNo(Integer.parseInt(steps[0]));
			//			IntegrityAutomationConstant.TEST_STEP_DESCRIPTION=steps[2];
			testStep.setDescription(steps[3]);
			testStep.setPageObjectName(steps[4]);
			//			System.out.println("page object name   #########"+steps[3]);
			testStep.setKeyWord(steps[5]);
			//			System.out.println("keyword name   #########"+steps[4]);
			if (steps.length > 5) {
				testStep.setOutputKey(steps[6]);
				testStep.setOutputType(steps[7]);

			}
			if (!steps[8].equalsIgnoreCase("")) {
				testStep.setDictonary(this.getDictionary(steps,
						previousDictonary));
			}

			testSteps.add(testStep);
		}

	}

	public Map<String, String> getDictionary(final String steps[],
			final Map<String, String> previousDictonary) {
		final int lastCellNumber = steps.length;
		final Map<String, String> dictionary = new HashMap<String, String>();
		for (int cell = 8; cell < lastCellNumber; cell++) {
			if (null != steps[cell]) {
				final String paramKey = steps[cell];
				String paramValue = "";
				if (!paramKey.equalsIgnoreCase("")) {

					if(previousDictonary.get(paramKey) == null)
					{
						if (steps.length - 1 > cell) {
							paramValue = steps[cell + 1];
							dictionary.put(paramKey, paramValue);

						} else {
							paramValue = "";
							dictionary.put(paramKey, paramValue);
						}
					}
					else
					{
						dictionary.put(paramKey,
								previousDictonary.get(paramKey));
					}
					// System.out.println(paramKey + paramValue);

				}

			}
			cell++;
		}
		return dictionary;
	}

	private void skipLine(BufferedReader reader) throws IOException {
		reader.readLine();
	}


	private void updateDriver(final List<List<TestCase>> batchTestCases,
			final List<TestCase> sequentialTestCases) {

		final List<TestCase> tempSequentialTestCases = new ArrayList<TestCase>();
		final Map<String, List<TestCase>> batchMap = new HashMap<String, List<TestCase>>();
		final List<TestCase> testCases = new ArrayList<TestCase>();
		BufferedReader br = null;
		String steps[] = null;
		String line = "";
		int i = 1;
		
		try {
			if(config.getBoolean("ui.automation.execution.jar")==true){
				DriverfilePath= config
						.getString(KeywordDrivenConstant.DRIVER_SHEET_PATH).replace("\\src\\main\\resources", "");
				String tempDriverPath[]=DriverfilePath.split("TestSuite");
				DriverfilePath=System.getProperty("user.dir")+File.separator+"TestSuite"+File.separator+tempDriverPath[1];
			}
			System.out.println("######## Driver File Path  "+DriverfilePath);
			if(DriverfilePath.contains("->"))
			{
				String testcasename = DriverfilePath.split("->")[1].replace(".csv", "");

				// set DriverfilePath
				DriverfilePath = DriverfilePath.split("->")[0].replace(".csv", "") + ".csv";
				try {
					br = new BufferedReader(new FileReader(DriverfilePath));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					skipLine(br);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int x = 0;
				try {
					while ((line = br.readLine()) != null) {
						if(line.contains(testcasename))
						{
							final TestCase testCase = new TestCase();
							steps = line.split(",");
							testCase.setPriority(1);


							testCase.setTestCaseName(steps[2]);

							testCase.setSkip(steps[3]);

							testCase.setJiraId(steps[4]);

							testCase.setURL(steps[5]);

							testCase.setGroupname(steps[6]);

							testCase.setWorkBookPath(steps[7]);
							
							final Map<String, String> previousDictonary = new HashMap<String, String>();
							List<TestStep> testSteps = getTestStepsByPath(
									StringUtils.trimToEmpty(testCase.getWorkBookPath()),
									previousDictonary);
							testCase.setTestStepList(testSteps);

							testCases.add(testCase);

						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else {
				br = new BufferedReader(new FileReader(DriverfilePath));
				skipLine(br);
				while ((line = br.readLine()) != null) {
					final TestCase testCase = new TestCase();
					steps = line.split(",");
					final String skipFlag = steps[3];
					String sanityOrFull = steps[1];
					if(sanityOrFull.isEmpty() || config.getString("ui.automation.sanity.or.full").equalsIgnoreCase("Full"))
						sanityOrFull="Full";

					if (StringUtils.isNoneEmpty(skipFlag)
							&& skipFlag
							.equalsIgnoreCase(KeywordDrivenConstant.SKIP_FLAG_OFF)
									&& sanityOrFull
									.equalsIgnoreCase(config.getString("ui.automation.sanity.or.full"))) {
						testCase.setSanityOrFull(steps[1]);
						testCase.setPriority(i);

//						testCase.setBatchId(steps[1]);

						testCase.setTestCaseName(steps[2]);

						testCase.setSkip(steps[3]);

						testCase.setJiraId(steps[4]);

						testCase.setURL(steps[5]);

						testCase.setGroupname(steps[6]);

						testCase.setWorkBookPath(steps[7]);
						System.out.println("@@@@@@@@@@@@@@@@@ csv reader, workbook path update driver function: "+testCase.getWorkBookPath());
						final Map<String, String> previousDictonary = new HashMap<String, String>();
						List<TestStep> testSteps = getTestStepsByPath(
								StringUtils.trimToEmpty(testCase.getWorkBookPath()),
								previousDictonary);
						testCase.setTestStepList(testSteps);

						final String batchId = testCase.getBatchId();
						if (StringUtils.isNotEmpty(batchId)) {
							if (null != batchMap.get(batchId)) {
								batchMap.get(batchId).add(testCase);
							} else {
								final List<TestCase> testList = new ArrayList<TestCase>();
								testList.add(testCase);
								batchMap.put(batchId, testList);
							}
						} else {
							tempSequentialTestCases.add(testCase);
						}

						i++;
					}

				}
			}
			sequentialTestCases.addAll(tempSequentialTestCases);
			final Set<Entry<String, List<TestCase>>> set = batchMap.entrySet();
			final Iterator<Entry<String, List<TestCase>>> batchIterator = set
					.iterator();
			while (batchIterator.hasNext()) {
				batchTestCases.add(batchIterator.next().getValue());
			}

		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e1) {
			e1.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}


	}
}
