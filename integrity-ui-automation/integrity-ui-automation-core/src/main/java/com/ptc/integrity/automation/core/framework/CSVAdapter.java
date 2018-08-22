package com.ptc.integrity.automation.core.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.ptc.integrity.automation.core.utils.IntegrityAutomationConstant;

public class CSVAdapter {

	String rootPath;
	String executionPath;
	String datasheetPath;
	String curExecutionFolder;
	String htmlReportsPath;
	String snapShotsPath;
	String dataSheetsPath;
	String dataSheet;
	String User;	
	String driverType;
	String enviromentsPath;
	String referentialJsonPath;
	String staticJsonPath;
	String screenShotOption;
	String drivercloseoption;
	String ngTestingToolPath;
	String dtoFilesPath;
	String imagesPath;
	String suiteName;
	List<String> methodList=null;
	HashMap <String, String> orgDictionary = new HashMap<String, String>();
	PageDictionary Dictionary = new PageDictionary();
	HashMap <String, String> Environment = new HashMap<String, String>();
	public static ArrayList<Object> arlst = new ArrayList<Object>();
	public static String rootFrameworkPath;
	public static boolean isRemoteMode;
	//	Driver d;
	List<String[]> updateCSV = new ArrayList<String[]>();
	Map<String, PageDictionary> dictonaryMap = null;
	public void setDictionary(PageDictionary dictionary) {
		Dictionary = dictionary;
	}
	//private Properties prop = new Properties();
	String[] csvData = new String[30];
	//Constructor
	public CSVAdapter(String DT, HashMap<String, String> GEnvironment,
			Map<String, PageDictionary> dictonaryMap,
			List<String> methodList, Configuration config)
	{
		driverType = DT;
		Environment = GEnvironment;
		this.dictonaryMap = dictonaryMap;
		this.methodList=methodList;

		isRemoteMode=config.getBoolean("ui.mode.remoteMode");
		//		isRemoteMode=Boolean.parseBoolean(config.getString("ui.mode.remoteMode"));

		//Get Root Path
		rootPath = config.getString("ui.automation.root.path");
		if(config.getBoolean("ui.automation.execution.jar")==true){
			rootPath= rootPath.replace("\\src\\main\\resources", "");
		}
		if(!isRemoteMode) {
			rootPath = rootPath.replace("\\", "/");
			rootFrameworkPath = rootPath.replace("ui/integrity-ui-automation-tests/src/main/resources", "");
		}
		User = System.getProperty("user.name");
		System.out.println(rootPath);
		//Set paths
		executionPath = config.getString("ui.automation.execution.path");
		dataSheetsPath = config.getString("ui.automation.data.path");
		enviromentsPath = config.getString("ui.automation.enviroments.path");
		ngTestingToolPath = config.getString("ui.automation.NGTestingTool.path");
		dtoFilesPath = config.getString("ui.automation.dto.path");
		imagesPath =  config.getString("ui.automation.images.path");
		staticJsonPath= config
				.getString("ui.automation.static.path");

		screenShotOption=config.getString("ui.automation.pass.screenshot");
		drivercloseoption=config.getString("ui.automation.driver.close");
		suiteName=config.getString("ui.automation.driversheet.path");
		String[] suite=suiteName.split("TestSuite");

		if(config.getBoolean("ui.automation.execution.jar")==true){
			staticJsonPath=System.getProperty("user.dir")+File.separator+"static";
			executionPath = System.getProperty("user.dir")+File.separator+"Execution";
			dataSheetsPath = System.getProperty("user.dir")+File.separator+"data";
			enviromentsPath = System.getProperty("user.dir")+File.separator+"environments";

			dtoFilesPath=System.getProperty("user.dir")+File.separator+"DTOFiles";
		}

		//Add to Env Variables
		Environment.put(IntegrityAutomationConstant.ROOTPATH, rootPath);
		Environment.put(IntegrityAutomationConstant.EXECUTIONFOLDERPATH, executionPath);
		Environment.put(IntegrityAutomationConstant.ENVIRONMENTXLSPATH, enviromentsPath);
		Environment.put(IntegrityAutomationConstant.STATICJSONPATH, staticJsonPath);
		Environment.put(IntegrityAutomationConstant.SNAPSHOTOPTION, screenShotOption);
		Environment.put(IntegrityAutomationConstant.DRIVERCLOSEOPTION, drivercloseoption);
		Environment.put(IntegrityAutomationConstant.SUITENAME, suite[1]);
		Environment.put(IntegrityAutomationConstant.NGTESTINGTOOLPATH, ngTestingToolPath);
		Environment.put(IntegrityAutomationConstant.DTOFILESPATH, dtoFilesPath);
		Environment.put(IntegrityAutomationConstant.IMAGESPATH, imagesPath);
	}
	public boolean fetchEnvironmentDetails(){
		try {
			boolean bFlag = false;
			String enviromentsExcel = null;

			enviromentsExcel = Environment.get(IntegrityAutomationConstant.ENVIRONMENTXLSPATH) + File.separator + IntegrityAutomationConstant.ENVIRONMENT_XLS;

			CSVReader csvReader = new CSVReader(new FileReader(enviromentsExcel));
			List<String[]> environmentDetails = csvReader.readAll();
			int rowNum=0;
			for (String[] strings : environmentDetails) {
				if(strings[rowNum].equalsIgnoreCase(Environment.get("ENV_CODE"))){
					Environment.put(com.ptc.integrity.automation.core.utils.IntegrityAutomationConstant.ENVIRONMENT, strings[0]);
					Environment.put(IntegrityAutomationConstant.URL, strings[1]);
					Environment.put(IntegrityAutomationConstant.CACHEDSCENARIO, strings[3]);
					Environment.put(IntegrityAutomationConstant.SCREENSHOTS, strings[2]);
					Environment.put(IntegrityAutomationConstant.INSTALLEDAPPLICATIONPATH, strings[4]);
					Environment.put(IntegrityAutomationConstant.USER_NAME, strings[5]);
					Environment.put(IntegrityAutomationConstant.PASSWORD, strings[6]);
					Environment.put(IntegrityAutomationConstant.DESCRIPTION, strings[7]);
					bFlag = true;
				}
			}
			if (bFlag == false)
			{
				System.out.println("Environment Code " + Environment.get("ENV_CODE") + " not found in the Environment xls");
				return false;
			}
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean fGetDataForTest(String className)
	{
		String dataSheet = null;

		dataSheet = dataSheetsPath + File.separator + className + ".csv";

		orgDictionary.clear();
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(dataSheet));
			List<String[]> regressionData;
			regressionData = csvReader.readAll();
			int csvHeaderSkip =0;
			boolean flag = false;

			for (String[] regressionTestcase : regressionData) {
				if(regressionTestcase[7].equalsIgnoreCase("Y") && csvHeaderSkip!=0){
					populateMapEntry(regressionTestcase);
					methodList.add(regressionTestcase[4]);
					flag=true;
				}else{
					updateCSV.add(regressionTestcase);
				}
				csvHeaderSkip++;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				csvReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	private void populateMapEntry(String[] envTestCases)
	{
		int count=10;
		PageDictionary singleTestMap = new PageDictionary();
		envTestCases[7]="N";
		updateCSV.add(envTestCases);
		singleTestMap.put(IntegrityAutomationConstant.TEST_NAME,envTestCases[3]);
		singleTestMap.put(IntegrityAutomationConstant.ACTION,envTestCases[4]);
		singleTestMap.put(IntegrityAutomationConstant.TEST_ID, envTestCases[5]);
		singleTestMap.put(IntegrityAutomationConstant.USER_ID, envTestCases[8]);
		singleTestMap.put(IntegrityAutomationConstant.PASSWORD, envTestCases[9]);

		while(count<envTestCases.length && !envTestCases[count].isEmpty()){
			singleTestMap.put(envTestCases[count], envTestCases[count+1]);
			count=count+2;
		}
		dictonaryMap.put(envTestCases[4], singleTestMap);
	}

	public boolean createExecutionFolders(String Classname) throws IOException
	{		
		//******************* Fetch Current TimeStamp ************************
		java.util.Date today = new java.util.Date();
		Timestamp now = new java.sql.Timestamp(today.getTime());
		String tempNow[] = now.toString().split("/.");
		String timeStamp = tempNow[0].replaceAll(":", ".").replaceAll(" ", "T");

		System.out.println("Time stamp while creating Execution folder  --  " + timeStamp);
		String fixedHTMLPath ="";
		File file = new File(System.getProperty("user.dir")+File.separator+"lastenv.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(fixedHTMLPath+File.separator+"HTML_Reports" + timeStamp);
		bufferedWriter.close();
		//Set execution paths
		curExecutionFolder = executionPath + File.separator+ Classname + File.separator + User;
		htmlReportsPath = curExecutionFolder+ File.separator + driverType + File.separator+"HTML_Reports" + timeStamp;
		snapShotsPath = htmlReportsPath + File.separator+"Snapshots";	

		//Put in Environments
		Environment.put(IntegrityAutomationConstant.CURRENTEXECUTIONFOLDER, curExecutionFolder);
		Environment.put(IntegrityAutomationConstant.HTMLREPORTSPATH, htmlReportsPath);
		Environment.put(IntegrityAutomationConstant.SNAPSHOTSFOLDER, snapShotsPath);		

		return (new File(snapShotsPath)).mkdirs();
	}

	public WebDriver fGetWebDriver(String driverType,String driverPath) throws MalformedURLException
	{
		//Define webdriver
		if (driverType.equalsIgnoreCase("CHROME"))
		{	
			System.setProperty("webdriver.chrome.driver", driverPath);
			if(Environment.get("CACHEDSCENARIO").equalsIgnoreCase("Y")==true){
				ChromeOptions options = new ChromeOptions();
				options.addArguments("user-data-dir=C:"+ File.separator +"Users"+ File.separator +""+System.getProperty("user.name").toLowerCase()+ File.separator +"AppData"+ File.separator +"Local"+ File.separator +"Google"+ File.separator +"Chrome"+ File.separator +"User Data");
				options.addArguments("--start-maximized");
				WebDriver driver = new ChromeDriver(options);
				return driver;
			}
			//Else block has code which includes saving downloaded file to desired location
			else{
				//******************* Fetch Current TimeStamp ************************
				//				java.util.Date today = new java.util.Date();
				//				Timestamp now = new java.sql.Timestamp(today.getTime());
				//				String tempNow[] = now.toString().split("/.");
				//				String timeStamp = tempNow[0].replaceAll(":", ".").replaceAll(" ", "T");
				//
				//				Map<String, Object> prefs = new HashMap<String, Object>();
				//				String csvExportedFilesPath=Environment.get("ROOTPATH")+ File.separator +"CSV Exported Files"+ File.separator +"CSV_Export_data_"+timeStamp;
				//				Environment.put("CSVExportedFilePath", csvExportedFilesPath);
				//				prefs.put("download.default_directory", csvExportedFilesPath);
				//
				////				DesiredCapabilities caps = DesiredCapabilities.chrome();
				////
				//				ChromeOptions options = new ChromeOptions();
				//				options.addArguments("--start-maximized");
				//				options.setExperimentalOption("prefs", prefs);
				//				options.setCapability(ChromeOptions.CAPABILITY, options);

				WebDriver driver = new ChromeDriver();
				return driver;
			}
		}
		else if(driverType.contains("CHROME_")){
			//			String tempDriverType[] = driverType.split("_");
			//			ChromeOptions chromeOptions = new ChromeOptions();
			//			Map<String, String> mobileEmulation = new HashMap<>();
			//
			//			mobileEmulation.put("deviceName", tempDriverType[1]);
			//			chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
			//
			//			WebDriver driver = new ChromeDriver(chromeOptions);
			//			return driver;
			WebDriver driver = new ChromeDriver();
			return driver;
		}
		else if (driverType.contains("FIREFOX")){
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile pf = profile.getProfile("default");
			//			return new FirefoxDriver(pf);
			//return new FirefoxDriver();
		}else if (driverType.contains("REMOTE")){			
			String[] driverData = getSeleniumGridData(Environment.get("node"));
			return getWebdriver(driverData[0],driverData[1],driverData[2],driverData[3]);
			//return new FirefoxDriver();
		}
		else{
			System.out.println("Driver type " + driverType + " is invalid");
			return null;
		}
		return null;
	}

	private String[] getSeleniumGridData(String node) {
		return new String[] { "chrome", "39", node,"http://10.196.77.180:4444/wd/hub"};
	}
	public RemoteWebDriver getWebdriver(String browser, String version, String platform, String hubUrl) throws MalformedURLException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setBrowserName(browser);
//		desiredCapabilities.setVersion(version);
		desiredCapabilities.setPlatform(Enum.valueOf(Platform.class, platform.toUpperCase()));
		desiredCapabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
		//        String PROXY = "10.10.10.10:8080";
		//        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		//        proxy.setHttpProxy(PROXY)
		//                .setFtpProxy(PROXY)
		//                .setSslProxy(PROXY);
		//        desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

		if (browser.equals("chrome")) {
			java.util.Date today = new java.util.Date();
			Timestamp now = new java.sql.Timestamp(today.getTime());
			String tempNow[] = now.toString().split("/.");
			String timeStamp = tempNow[0].replaceAll(":", ".").replaceAll(" ", "T");

			Map<String, Object> prefs = new HashMap<String, Object>();
			String csvExportedFilesPath=Environment.get("ROOTPATH")+ File.separator +"CSV Exported Files"+File.separator+"CSV_Export_data_"+timeStamp;
			Environment.put("CSVExportedFilePath", csvExportedFilesPath);
			prefs.put("download.default_directory", csvExportedFilesPath);

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("incognito");
			desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		}

		RemoteWebDriver driver = new RemoteWebDriver(new URL(hubUrl), desiredCapabilities);
		driver.setFileDetector(new LocalFileDetector());

		return driver;
	}


	public void fUpdateTestCaseRowSkip()
	{   

		dataSheet = dataSheetsPath + File.separator + Environment.get(IntegrityAutomationConstant.CLASS_NAME) + ".csv";

		CSVWriter csvWriter = null;
		try {
			csvWriter = new CSVWriter(new FileWriter(dataSheet));
			csvWriter.writeAll(updateCSV);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void fUpdateTimeTaken(String methodName)
	{   String[] dictDetails = new String[30];
	String timeTaken = null;
	for (String[] dictString : updateCSV) {
		if(dictString[4].equalsIgnoreCase(methodName)){
			timeTaken = Dictionary.get("TIME_TAKEN").toString();
			dictDetails=dictString;
		}
	}
	dictDetails[13]=Dictionary.get("TIME_TAKEN").toString();

	dataSheet = dataSheetsPath + File.separator + Environment.get(IntegrityAutomationConstant.CLASS_NAME) + ".csv";

	CSVWriter csvWriter = null;
	try {
		csvWriter = new CSVWriter(new FileWriter(dataSheet));
		csvWriter.writeAll(updateCSV);
	} catch (IOException e) {
		e.printStackTrace();
	}finally{
		try {
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
}
