package com.ptc.integrity.automation.core.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.ptc.integrity.automation.core.utils.IntegrityAutomationConstant;

public class Reporting {

	private String g_strTestCaseReport;
	private String g_strSnapshotFolderName;
	private String g_strScriptName;
	private String g_ScreenShotRelative;
	// Counters and Integers
	private int g_iSnapshotCount;
	private int g_OperationCount;
	private int g_iPassCount;
	private int g_iFailCount;
	private int g_iTCPassed;
	public static String stDescription;

	private int g_iTestCaseNo;
	protected Date g_StartTime;
	protected Date g_EndTime;
	protected Date g_SummaryStartTime;
	protected Date g_SummaryEndTime;
	public WebDriver driver;
	private String driverType;
	private PageDictionary Dictionary = null;
	private HashMap<String, String> Environment = new HashMap<String, String>();

	public Reporting(WebDriver webDriver, String DT, HashMap<String, String> Env) {
		driver = webDriver;
		Environment = Env;
		driverType = DT;
	}

	private FileOutputStream foutStrm = null;

	// *****************************************************************************************
	// * Name : fnCreateSummaryReport
	// * Description : The function creates the summary HTML file
	// * Author :
	// * Input Params : None
	// * Return Values : None
	// *****************************************************************************************
	public void fnCreateSummaryReport(String suiteType) {
		// Setting counter value
		g_iTCPassed = 0;
		g_iTestCaseNo = 0;
		g_SummaryStartTime = new Date();

		try {
			System.out.println("HTML SELENIUM REPORT PATH    "
					+ Environment.get("HTMLREPORTSPATH"));
			// Open the test case report for writing
			foutStrm = new FileOutputStream(Environment.get("HTMLREPORTSPATH")
					+ "\\SummaryReport.html", true);

			// Close the html file
			new PrintStream(foutStrm)
					.println("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100% BGCOLOR=BLACK>");
			// original
			// new
			// PrintStream(foutStrm).println("<TR><TD WIDTH=90% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=ORANGE SIZE=3><B>Integrity Automation</B></FONT></TD></TR><TR><TD ALIGN=CENTER BGCOLOR=ORANGE><FONT FACE=VERDANA COLOR=WHITE SIZE=3><B>Selenium Framework Reporting</B></FONT></TD></TR></TABLE><TABLE CELLPADDING=3 WIDTH=100%><TR height=30><TD WIDTH=100% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Automation Result : "
			// + new Date() + " on Machine " +
			// InetAddress.getLocalHost().getHostName() + " by user " +
			// System.getProperty("user.name") + " on Browser " + driverType
			// +"</B></FONT></TD></TR><TR height=20><TD WIDTH=100% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Environment: "+
			// Environment.get("DESCRIPTION")+"</B></FONT></TD></TR><TR HEIGHT=5></TR></TABLE>");
			// Adding new code here
			new PrintStream(foutStrm)
					.println("<TR><TD WIDTH=90% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=ORANGE SIZE=3><B>Integrity Automation</B></FONT></TD></TR><TR><TD ALIGN=CENTER BGCOLOR=ORANGE><FONT FACE=VERDANA COLOR=WHITE SIZE=3><B>Selenium Framework Reporting</B></FONT></TD></TR></TABLE><TABLE CELLPADDING=3 WIDTH=100%><TR height=30><TD WIDTH=5% ALIGN=LEFT BGCOLOR=SKYBLUE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Date : </B></FONT></TD><TD WIDTH=15% ALIGN=LEFT BGCOLOR=LIGHTGRAY><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;"
							+ new Date()
							+ "</B></FONT></TD><TD WIDTH=10% ALIGN=LEFT BGCOLOR=SKYBLUE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Test Session : </B></FONT></TD><TD WIDTH=5% ALIGN=LEFT BGCOLOR=LIGHTGRAY><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;"
							+ suiteType.toUpperCase()
							+ "</B></FONT></TD><TD WIDTH=10% ALIGN=LEFT BGCOLOR=SKYBLUE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;Machine  </B></FONT></TD><TD WIDTH=15% ALIGN=LEFT BGCOLOR=LIGHTGRAY><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;"
							+ InetAddress.getLocalHost().getHostName()
							+ "</B></FONT></TD><TD WIDTH=15% ALIGN=LEFT BGCOLOR=SKYBLUE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;User: </B></FONT></TD><TD WIDTH=25% ALIGN=LEFT BGCOLOR=LIGHTGRAY><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;"
							+ System.getProperty("user.name")
							+ "</B></FONT></TD></TR></TABLE><TABLE CELLPADDING=3 WIDTH=100%><TR height=30><TD WIDTH=15% ALIGN=LEFT BGCOLOR=SKYBLUE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Browser: </B></FONT></TD><TD WIDTH=15% ALIGN=LEFT BGCOLOR=LIGHTGRAY><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;"
							+ driverType
							+ "</B></FONT></TD><TD WIDTH=15% ALIGN=LEFT BGCOLOR=SKYBLUE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Environment: </B></FONT></TD><TD WIDTH=15% ALIGN=LEFT BGCOLOR=LIGHTGRAY><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;"
							+ Environment.get("DESCRIPTION")
							+ "</B></FONT></TD><TD WIDTH=5% ALIGN=LEFT BGCOLOR=SKYBLUE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; URL: </B></FONT></TD><TD WIDTH=35% ALIGN=LEFT BGCOLOR=LIGHTGRAY><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp;"
							+ Environment.get("URL")
							+ "</B></FONT></TD></TR><TR HEIGHT=5></TR></TABLE>");
			new PrintStream(foutStrm)
					.println("<TABLE  CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
			new PrintStream(foutStrm)
					.println("<TR COLS=6 BGCOLOR=ORANGE><TD WIDTH=10%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>TC No.</B></FONT></TD><TD  WIDTH=50%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Test Name</B></FONT></TD><TD  WIDTH=10%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Test ID</B></FONT></TD><TD BGCOLOR=ORANGE WIDTH=15%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Status</B></FONT></TD><TD  WIDTH=15%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Test Duration</B></FONT></TD></TR>");

			// Close the object
			foutStrm.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

		foutStrm = null;
	}

	// *****************************************************************************************
	// * Name : fnCreateHtmlReport
	// * Description : The function creates the result HTML file
	// * In Case the file already exists, it will overwrite it and also delete
	// the existing folders.
	// * Author :
	// * Input Params : None
	// * Return Values : None
	// *****************************************************************************************
	public void fnCreateHtmlReport(String strTestName) {

		// Set the default Operation count as 0
		g_OperationCount = 0;

		// Number of default Pass and Fail cases to 0
		g_iPassCount = 0;
		g_iFailCount = 0;

		// Snapshot count to start from 0
		g_iSnapshotCount = 0;

		// script name
		g_strScriptName = strTestName;

		String TEST_ID = Dictionary.get("TEST_ID");
		// Set the name for the Test Case Report File
		g_strTestCaseReport = Environment.get("HTMLREPORTSPATH") + "\\Report_"
				+ TEST_ID + "_" + g_strScriptName + ".html";

		// Snap Shot folder
		g_strSnapshotFolderName = Environment.get("SNAPSHOTSFOLDER") + "\\"
				+ g_strScriptName;
		g_ScreenShotRelative = "\\Snapshots\\" + g_strScriptName;
		// Delete the Summary Folder if present
		File file = new File(g_strSnapshotFolderName);

		if (file.exists()) {
			file.delete();
		}

		// Make a new snapshot folder
		file.mkdir();

		// Open the report file to write the report

		try {
			foutStrm = new FileOutputStream(g_strTestCaseReport);
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		}

		// Close the html file
		try {
			new PrintStream(foutStrm)
					.println("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100% BGCOLOR=ORANGE>");
			new PrintStream(foutStrm)
					.println("<TR><TD WIDTH=90% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=ORANGE SIZE=3><B>Report</B></FONT></TD></TR><TR><TD ALIGN=CENTER BGCOLOR=ORANGE><FONT FACE=VERDANA COLOR=WHITE SIZE=3><B>Selenium Framework Reporting</B></FONT></TD></TR></TABLE><TABLE CELLPADDING=3 WIDTH=100%><TR height=30><TD WIDTH=130% ALIGN=CENTER BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=//0073C5 SIZE=2><B>&nbsp; Automation Result : "
							+ new Date()
							+ " on Machine "
							+ InetAddress.getLocalHost().getHostName()
							+ " by user "
							+ System.getProperty("user.name")
							+ " on Browser "
							+ driverType
							+ "</B></FONT></TD></TR><TR HEIGHT=5></TR></TABLE>");
			new PrintStream(foutStrm)
					.println("<TABLE BORDER=0 BORDERCOLOR=WHITE CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
			new PrintStream(foutStrm)
					.println("<TR><TD BGCOLOR=BLACK WIDTH=10%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Test     Name:</B></FONT></TD><TD COLSPAN=6 BGCOLOR=BLACK><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>"
							+ g_strScriptName
							+ "</B></FONT></TD><TD BGCOLOR=BLACK WIDTH=10%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Test ID-"
							+ TEST_ID + "</B></FONT></TD></TR>");
			new PrintStream(foutStrm)
					.println("</TABLE><BR/><TABLE WIDTH=100% CELLPADDING=3>");
			// new
			// PrintStream(foutStrm).println("<TR WIDTH=100%><TH BGCOLOR=ORANGE WIDTH=5%><FONT FACE=VERDANA SIZE=2>Step No.</FONT></TH><TH BGCOLOR=ORANGE WIDTH=28%><FONT FACE=VERDANA SIZE=2>Step Description</FONT></TH><TH BGCOLOR=ORANGE WIDTH=25%><FONT FACE=VERDANA SIZE=2>Expected Value</FONT></TH><TH BGCOLOR=ORANGE WIDTH=25%><FONT FACE=VERDANA SIZE=2>Obtained Value</FONT></TH><TH BGCOLOR=ORANGE WIDTH=7%><FONT FACE=VERDANA SIZE=2>Result</FONT></TH></TR>");
			new PrintStream(foutStrm)
					.println("<TR WIDTH=100%><TH BGCOLOR=ORANGE WIDTH=5%><FONT FACE=VERDANA SIZE=2>Step No.</FONT></TH><TH BGCOLOR=ORANGE WIDTH=10%><FONT FACE=VERDANA SIZE=2>Description</FONT></TH><TH BGCOLOR=ORANGE WIDTH=15%><FONT FACE=VERDANA SIZE=2>Keyword Name</FONT></TH><TH BGCOLOR=ORANGE WIDTH=15%><FONT FACE=VERDANA SIZE=2>Expected Value</FONT></TH><TH BGCOLOR=ORANGE WIDTH=15%><FONT FACE=VERDANA SIZE=2>Obtained Value</FONT></TH><TH BGCOLOR=ORANGE WIDTH=10%><FONT FACE=VERDANA SIZE=2>Screen Shot</FONT></TH></TR>");

			foutStrm.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		// Deference the file pointer
		foutStrm = null;

		// Get the start time of the execution
		g_StartTime = new Date();

	}

	// *****************************************************************************************
	// * Name : fnWriteTestSummary
	// * Description : The function Writes the final outcome of a test case to a
	// summary file.
	// * Author :
	// * Input Params :
	// * strTestCaseName(String) - the name of the test case
	// * strResult(String) - the result (Pass/Fail)
	// * Return Values :
	// * (Boolean) TRUE - Succeessful write
	// * FALSE - Report file not created
	// *****************************************************************************************
	public void fnWriteTestSummary(String strTestCaseName, String strResult,
			String strDuration) {
		String sColor, sRowColor;

		// Close the file
		try {
			// Open the test case report for writing
			foutStrm = new FileOutputStream(Environment.get("HTMLREPORTSPATH")
					+ "\\SummaryReport.html", true);

			// Check color result
			if (strResult.toUpperCase().equals("PASSED")
					|| strResult.toUpperCase().equals("PASS")) {
				sColor = "GREEN";
				g_iTCPassed++;
			} else if (strResult.toUpperCase().equals("FAILED")
					|| strResult.toUpperCase().equals("FAIL")) {
				sColor = "RED";
			} else {
				sColor = "ORANGE";
			}

			g_iTestCaseNo++;

			if (g_iTestCaseNo % 2 == 0) {
				sRowColor = "#EEEEEE";
			} else {
				sRowColor = "#D3D3D3";
			}

			// Write the result of Individual Test Case
			new PrintStream(foutStrm)
					.println("<TR COLS=3 BGCOLOR="
							+ sRowColor
							+ "><TD  WIDTH=10%><FONT FACE=VERDANA SIZE=2>"
							+ g_iTestCaseNo
							+ "</FONT></TD><TD  WIDTH=50%><FONT FACE=VERDANA SIZE=2>"
							+ strTestCaseName
							+ "</FONT></TD><TD  WIDTH=10%><FONT FACE=VERDANA SIZE=2>"
							+ Dictionary.get("TEST_ID")
							+ "</FONT></TD><TD  WIDTH=15%><A HREF='"
							+ strTestCaseName
							+ ".html'><FONT FACE=VERDANA SIZE=2 COLOR="
							+ sColor
							+ "><B>"
							+ strResult
							+ "</B></FONT></A></TD><TD  WIDTH=15%><FONT FACE=VERDANA SIZE=2>"
							+ strDuration + "</FONT></TD></TR>");

			foutStrm.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		foutStrm = null;

	}

	// *****************************************************************************************
	// * Name : fnCloseHtmlReport
	// * Description : The function Closes the HTML file
	// * Author :
	// * Input Params : None
	// * Return Values : None
	// *****************************************************************************************
	public void fnCloseHtmlReport(String ConsoleLogsPath) {

		// Declaring variables

		String strTestCaseResult = null;

		// Open the report file to write the report
		try {
			foutStrm = new FileOutputStream(g_strTestCaseReport, true);

		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		}

		// Get the current time
		g_EndTime = new Date();

		// Fetch the time difference
		String strTimeDifference = fnTimeDiffference(g_StartTime.getTime(),
				g_EndTime.getTime());

		// Close the html file
		try {
			// Write the number of test steps passed/failed and the time which
			// the test case took to run

			if (!ConsoleLogsPath.isEmpty() || ConsoleLogsPath.length() != 0) {
				new PrintStream(foutStrm)
						.println("<TR></TR><TR><TD BGCOLOR=BLACK WIDTH=5%></TD><TD BGCOLOR=BLACK WIDTH=28%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Time Taken : "
								+ strTimeDifference
								+ "</B></FONT></TD><TD BGCOLOR=BLACK WIDTH=25%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Pass Count : "
								+ g_iPassCount
								+ "</B></FONT></TD><TD BGCOLOR=BLACK WIDTH=25%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Fail Count : "
								+ g_iFailCount
								+ "</b></FONT></TD><TD BGCOLOR=Black WIDTH=7%></TD><TD BGCOLOR=Black WIDTH=10% ALIGN=CENTER><A COLOR=White HREF='"
								+ ConsoleLogsPath
								+ "'><FONT FACE=VERDANA COLOR=RED SIZE=2>Console Logs</FONT></A></TD></TR>");
			} else {
				new PrintStream(foutStrm)
						.println("<TR></TR><TR><TD BGCOLOR=BLACK WIDTH=5%></TD><TD BGCOLOR=BLACK WIDTH=28%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Time Taken : "
								+ strTimeDifference
								+ "</B></FONT></TD><TD BGCOLOR=BLACK WIDTH=25%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Pass Count : "
								+ g_iPassCount
								+ "</B></FONT></TD><TD BGCOLOR=BLACK WIDTH=25%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Fail Count : "
								+ g_iFailCount
								+ "</b></FONT></TD><TD BGCOLOR=Black WIDTH=7%></TD><TD BGCOLOR=Black WIDTH=10%></TD></TR>");
			}
			// Close the HTML tags
			new PrintStream(foutStrm)
					.println("</TABLE><TABLE WIDTH=100%><TR><TD ALIGN=RIGHT><FONT FACE=VERDANA COLOR=ORANGE SIZE=1>&copy; Reporting</FONT></TD></TR></TABLE></BODY></HTML>");
			// Close File stream
			foutStrm.close();

		} catch (IOException io) {
			io.printStackTrace();
		}

		// Deference the file pointer
		foutStrm = null;

		// Check if test case passed or failed

		if (g_iFailCount != 0) {
			strTestCaseResult = "Fail";
		} else {
			strTestCaseResult = "Pass";
		}

		// Write into the Summary Report
		fnWriteTestSummary("Report_" + Dictionary.get("TEST_ID") + "_"
				+ Dictionary.get("TEST_NAME"), strTestCaseResult,
				strTimeDifference);

	}

	// *****************************************************************************************
	// * Name : fnCloseTestSummary
	// * Description : The function Closes the summary file
	// * Author :
	// * Input Params : None
	// * Return Values : None
	// *****************************************************************************************
	public void fnCloseTestSummary() {
		g_SummaryEndTime = new Date();
		// Fetch the time difference
		String strTimeDifference = fnTimeDiffference(
				g_SummaryStartTime.getTime(), g_SummaryEndTime.getTime());

		// Open the Test Summary Report File
		try {
			foutStrm = new FileOutputStream(Environment.get("HTMLREPORTSPATH")
					+ "\\SummaryReport.html", true);

			new PrintStream(foutStrm).println("</TABLE><TABLE WIDTH=100%><TR>");
			new PrintStream(foutStrm)
					.println("<TD BGCOLOR=BLACK WIDTH=10%></TD><TD BGCOLOR=BLACK WIDTH=60%><FONT FACE=VERDANA SIZE=2 COLOR=WHITE><B></B></FONT></TD><TD BGCOLOR=BLACK WIDTH=15%><FONT FACE=WINGDINGS SIZE=4>2</FONT><FONT FACE=VERDANA SIZE=2 COLOR=WHITE><B>Total Passed: "
							+ g_iTCPassed
							+ "</B></FONT></TD><TD BGCOLOR=BLACK WIDTH=15%><FONT FACE=VERDANA SIZE=2 COLOR=WHITE><B>"
							+ strTimeDifference + "</B></FONT></TD>");
			new PrintStream(foutStrm).println("</TR></TABLE>");
			new PrintStream(foutStrm)
					.println("<TABLE WIDTH=100%><TR><TD ALIGN=RIGHT><FONT FACE=VERDANA COLOR=ORANGE SIZE=1>&copy; Reporting</FONT></TD></TR></TABLE></BODY></HTML>");

			// Close File stream
			foutStrm.close();

		} catch (IOException io) {
			io.printStackTrace();
		}

		// Deference the file pointer
		foutStrm = null;
	}

	// *****************************************************************************************
	// * Name : fnWriteToHtmlOutput
	// * Description : The function Writes output to the HTML file
	// * Author :
	// * Input Params :
	// * strDescription(String) - the description of the object
	// * strExpectedValue(String) - the expected value
	// * strObtainedValue(String) - the actual/obtained value
	// * strResult(String) - the result (Pass/Fail)
	// * Return Values :
	// * (Boolean) TRUE - Successful write
	// * FALSE - Report file not created
	// *****************************************************************************************
	public void fnWriteToHtmlOutput(String strFunctionName,
			String strExpectedValue, String strObtainedValue, String strResult) {
		String sStep;
		String stepDescription = stDescription;
		if (Dictionary.containsKey("STEP")) {
			sStep = Dictionary.get("STEP") + "<NS>" + strFunctionName + "<ND>"
					+ strExpectedValue + "<ND>" + strObtainedValue + "<ND>"
					+ strResult;
			Dictionary.remove("STEP");
		} else {
			sStep = strFunctionName + "<ND>" + strExpectedValue + "<ND>"
					+ strObtainedValue + "<ND>" + strResult;
		}

		Dictionary.put("STEP", sStep);
		// Declaring Variables
		String snapshotFilePath, snapshotRelativePath, sRowColor;

		// Open the test case report for writing
		// Open the HTML file
		// Open the report file to write the report
		try {
			foutStrm = new FileOutputStream(g_strTestCaseReport, true);

		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		}

		// Increment the Operation Count
		g_OperationCount = g_OperationCount + 1;

		// Row Color
		if (g_OperationCount % 2 == 0) {
			sRowColor = "#EEEEEE";
		} else {
			sRowColor = "#D3D3D3";

		}

		// Check if the result is Pass or Fail
		if (strResult.toUpperCase().equals("PASS")) {
			if (Environment.get(IntegrityAutomationConstant.SNAPSHOTOPTION)
					.equalsIgnoreCase("Y")) {
				// Increment the Pass Count
				g_iPassCount++;
				// Increment the snapshot count
				g_iSnapshotCount++;
				// Get the Full path of the snapshot
				snapshotFilePath = g_strSnapshotFolderName + "\\SS_"
						+ g_iSnapshotCount + ".png";
				snapshotRelativePath = "." + g_ScreenShotRelative + "\\SS_"
						+ g_iSnapshotCount + ".png";
				// Capture the Snapshot
				fTakeScreenshot(snapshotFilePath, "P");

				new PrintStream(foutStrm)
						.println("<TR WIDTH=100%><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=5% ALIGN=CENTER><FONT FACE=VERDANA SIZE=2 COLOR=green><B>"
								+ g_OperationCount
								+ "</B></FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ stepDescription
								+ " </FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strFunctionName
								+ " </FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=12%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strExpectedValue
								+ " </FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=13%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strObtainedValue
								+ " </FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10% ALIGN=CENTER><A HREF='"
								+ snapshotRelativePath
								+ "'><IMG SRC='"
								+ snapshotRelativePath
								+ "' alt='Screen Shot' style='width:250px;height:200px'></A></TD></TR>");
			} else {
				strResult = "Pass";
				// Write Results into the file
				new PrintStream(foutStrm)
						.println("<TR WIDTH=100%><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=5% ALIGN=CENTER><FONT FACE=VERDANA SIZE=2 COLOR=green><B>"
								+ g_OperationCount
								+ "</B></FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ stepDescription
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strFunctionName
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=12%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strExpectedValue
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=13%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strObtainedValue
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10% ALIGN=CENTER><FONT FACE=VERDANA SIZE=2 COLOR=Green><B>"
								+ strResult + "</B></FONT></TD></TR>");
			}

		} else {
			if (strResult.toUpperCase().equals("FAIL")) {
				// Increment the SnapShot count
				g_iSnapshotCount++;

				// Increment the Fail Count
				g_iFailCount++;

				// Get the Full path of the snapshot
				snapshotFilePath = g_strSnapshotFolderName + "\\SS_"
						+ g_iSnapshotCount + ".png";
				snapshotRelativePath = "." + g_ScreenShotRelative + "\\SS_"
						+ g_iSnapshotCount + ".png";
				// Capture the Snapshot
				fTakeScreenshot(snapshotFilePath, "F");

				new PrintStream(foutStrm)
						.println("<TR WIDTH=100%><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=5% ALIGN=CENTER><FONT FACE=VERDANA SIZE=2 COLOR=red><B>"
								+ g_OperationCount
								+ "</B></FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=red>"
								+ stepDescription
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=red>"
								+ strFunctionName
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=12%><FONT FACE=VERDANA SIZE=2 COLOR=red>"
								+ strExpectedValue
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=13%><FONT FACE=VERDANA SIZE=2 COLOR=red>"
								+ strObtainedValue
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10% ALIGN=CENTER><A HREF='"
								+ snapshotRelativePath
								+ "'><IMG SRC='"
								+ snapshotRelativePath
								+ "' alt='Screen Shot' style='width:250px;height:200px'></A></TD></TR>");

			} else if (strResult.toUpperCase().equals("DONE")) {
				strResult = "Pass";
				// Write Results into the file
				new PrintStream(foutStrm)
						.println("<TR WIDTH=100%><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=5% ALIGN=CENTER><FONT FACE=VERDANA SIZE=2 COLOR=green><B>"
								+ g_OperationCount
								+ "</B></FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ stepDescription
								+ "</FONT></TD></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strFunctionName
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=12%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strExpectedValue
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=13%><FONT FACE=VERDANA SIZE=2 COLOR=green>"
								+ strObtainedValue
								+ "</FONT></TD><TD BGCOLOR="
								+ sRowColor
								+ " WIDTH=10% ALIGN=CENTER><FONT FACE=VERDANA SIZE=2 COLOR=Green><B>"
								+ strResult + "</B></FONT></TD></TR>");
			}

		}
		try {
			// Close File stream
			foutStrm.close();

		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	// *****************************************************************************************
	// * Name : fTakeScreenshot
	// * Description : The function takes the screenshot
	// * Author :
	// * Input Params : SSPath - Screenshot path
	// * Return Values : None
	// *****************************************************************************************
	public void fTakeScreenshot(String SSPath, String sStatus) {

		// String sTakeScreenShot =
		// Environment.get("SCREENSHOTS").toUpperCase();
		// added option to Take/Not Take screenshots, or Take screenshots per
		// specific status "P", "F"
		// if (sStatus.equals(sTakeScreenShot) || sTakeScreenShot.equals("Y") ||
		// sTakeScreenShot.equals(""))
		// {
		try {
			/*
			 * WebDriver screenDriver; if(driverType.contains("FIREFOX") ||
			 * driverType.contains("CHROME") || driverType.contains("IE")){
			 * screenDriver = driver; }else{ screenDriver = new
			 * Augmenter().augment(driver); }
			 */

			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(SSPath));
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// screenDriver = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

	// *****************************************************************************************
	// * Name : fnTimeDiffference
	// * Description : calculate Time of execution
	// * Author :
	// * Input Params : SSPath - Screenshot path
	// * Return Values : None
	// *****************************************************************************************
	public String fnTimeDiffference(long startTime, long endTime) {

		// Finding the difference in milliseconds
		long delta = endTime - startTime;

		// Finding number of days
		int days = (int) delta / (24 * 3600 * 1000);

		// Finding the remainder
		delta = (int) delta % (24 * 3600 * 1000);

		// Finding number of hrs
		int hrs = (int) delta / (3600 * 1000);

		// Finding the remainder
		delta = (int) delta % (3600 * 1000);

		// Finding number of minutes
		int min = (int) delta / (60 * 1000);

		// Finding the remainder
		delta = (int) delta % (60 * 1000);

		// Finding number of seconds
		int sec = (int) delta / 1000;

		// Concatenting to get time difference in the form day:hr:min:sec
		// String strTimeDifference = days + ":" + hrs + ":" + min + ":" + sec;
		String strTimeDifference = days + "d " + hrs + "h " + min + "m " + sec
				+ "s";
		return strTimeDifference;
	}

	// *****************************************************************************************
	// * Name : fnWriteThreadReport
	// * Description : The function Writes each Thread details.
	// * Author :
	// * Input Params :
	// * int iThreadCount
	// * String sReportFile(String)
	// * String sCalendar
	// * String sSummaryFile
	// * Return Values :
	// * Void
	// *****************************************************************************************
	public void fnWriteThreadReport(int iThreadCount, String sReportFile,
			String sCalendar, String sSummaryFile) {
		String sRowColor;

		// Close the file
		try {
			// Open the test case report for writing
			foutStrm = new FileOutputStream(sReportFile, true);

			// Set Row Color
			if (iThreadCount % 2 == 0) {
				sRowColor = "#EEEEEE";
			} else {
				sRowColor = "#D3D3D3";
			}

			// Write the result of Individual Test Case
			new PrintStream(foutStrm)
					.println("<TR COLS=3 BGCOLOR="
							+ sRowColor
							+ "><TD  WIDTH=10%><FONT FACE=VERDANA SIZE=2>"
							+ iThreadCount
							+ "</FONT></TD><TD  WIDTH=35%><FONT FACE=VERDANA SIZE=2>"
							+ driverType
							+ "</FONT></TD><TD  WIDTH=35%><FONT FACE=VERDANA SIZE=2>"
							+ sCalendar
							+ "</FONT></TD><TD  WIDTH=20%><A HREF='"
							+ sSummaryFile
							+ "'><FONT FACE=VERDANA SIZE=2 COLOR=GREEN><B>Report</B></FONT></A></TD></TR>");

			foutStrm.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		foutStrm = null;

	}

	public void setDictionary(PageDictionary dictionary) {
		Dictionary = dictionary;
	}

	public int getG_iTCPassed() {
		return g_iTCPassed;
	}

	public void setG_iTCPassed(int g_iTCPassed) {
		this.g_iTCPassed = g_iTCPassed;
	}

	public int getG_iTestCaseNo() {
		return g_iTestCaseNo;
	}

	public void setG_iTestCaseNo(int g_iTestCaseNo) {
		this.g_iTestCaseNo = g_iTestCaseNo;
	}
}
