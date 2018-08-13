/*package com.ptc.integrity.automation.client;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class TestNGClient {

	public static void main(String[] args) {
		System.setProperty("one.click", "Y");
		System.setProperty("resultUpdatable", "N");
		TestNGClient testNGClient = new TestNGClient();
		if(args.length!=0){
			if(("-ENV").equalsIgnoreCase(args[0])){
				System.setProperty("envName", args[1].toUpperCase());
				testNGClient.callTestNGxml();
			}else if(("ENV").equalsIgnoreCase(args[0])){
				System.out.println("Invalid parameter mentioned");
				System.out.println("Please insert \"-\"ENV/env Environment Name");
			}else{
				System.out.println("No parameter Type specified");
			}
		}else{
			testNGClient.callTestNGxml();
		}

	}
	private void callTestNGxml(){
		TestNG testNG = new TestNG();
		List<String> suites = new ArrayList<String>();
		suites.add(System.getProperty("user.dir")+"\\RegressionTest.xml");
		testNG.setTestSuites(suites);
		testNG.run();
	}

}
 */
package com.ptc.integrity.automation.core.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.testng.TestNG;

import com.ptc.integrity.automation.core.framework.ConfigurationManager;

public class TestNGClient {

	public static void main(String[] args) {
		System.out.println("*********** Test NG Client");
		final TestNGClient testNGClient = new TestNGClient();
		testNGClient.processCMDarguements(args);
	}

	@Option(name = "-env", usage = "environment code in which you need to test the Integrity")
	public String env = null;
	@Option(name = "-suite", usage = "Suites to run")
	public String suite = null;

	private void callTestNGxml() {
		
		final TestNG testNG = new TestNG();
		final List<String> suites = new ArrayList<String>();
		final ConfigurationManager configManager = ConfigurationManager
				.getInstance();
		configManager.setConfigPath(System.getProperty("user.dir")
				+ File.separator + "integrity-ui-automation.properties");
		final Configuration config = configManager.getConfig();
		System.out.println("************* "+System.getProperty("user.dir"));
		config.setProperty("ui.automation.root.path",
				System.getProperty("user.dir"));
		config.clearProperty("ui.automation.execution.jar");
		config.setProperty("ui.automation.execution.jar", true);
		config.clearProperty("ui.mode.remoteMode");
		config.setProperty("ui.mode.remoteMode", false);
		String[] suitesToRun = null;
		if (null != suite) {
			suitesToRun = suite.split(" ");
		}
		if (suitesToRun != null) {
			for (final String suite : suitesToRun) {
				suites.add(System.getProperty("user.dir") + File.separator
						+ suite);
			}
		}
		System.out.println(suites);
		testNG.setTestSuites(suites);
		testNG.run();

	}

	private void processCMDarguements(final String[] args) {
		final CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (final CmdLineException e) {
			System.err.println(e.getMessage());
			System.err
					.println("java -jar IntegrityUIAutmation.jar-<version> [-env VAL] [-suite VAL VAL VAL]");
			parser.printUsage(System.err);
			return;
		}
		System.out.println(suite);
		if (args.length == 0) {
			callTestNGxml();
		} else {
			if (null != env) {
				System.setProperty("envName", env.toUpperCase());
			}
			callTestNGxml();
		}

	}

}
