package com.ptc.integrity.automation.core.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.google.common.io.Files;
import com.ptc.integrity.automation.core.keydriven.CSVReaderImpl;
import com.ptc.integrity.automation.core.keydriven.Driver;
import com.ptc.integrity.automation.core.keydriven.KeywordDrivenConstant;
import com.ptc.integrity.automation.core.keydriven.TestCase;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FTLUtility {

	protected static org.apache.commons.configuration.Configuration config = com.ptc.integrity.automation.core.framework.ConfigurationManager
			.getInstance().getConfig();

	public static void main(String args[]) {

		String baseDir = System.getProperty("user.dir") + File.separator
				+ "integrity-ui-automation-tests";
		String baseResourceDir = FilenameUtils.separatorsToSystem(System
				.getProperty("user.dir")
				+ "\\integrity-ui-automation-tests\\src\\main\\resources\\");

		String output = baseDir + File.separator + "src" + File.separator
				+ "main" + File.separator + "java" + File.separator + "com"
				+ File.separator + "ptc" + File.separator + "integrity"
				+ File.separator + "automation" + File.separator + "tests" + File.separator+ "keydriven"
				+ File.separator + "KeyDrivenTestCasesSuiteRunnerCI.java";
		String ftlLocation = System.getProperty("user.dir") + File.separator
				+ "integrity-ui-automation-tests" + File.separator + "src"
				+ File.separator + "main" + File.separator + "resources";
		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(ftlLocation));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Template template = null;
		try {
			// Build the data-model
			Map<String, Object> data = new HashMap<String, Object>();
			// final CSVReaderImpl excelReader = new
			// CSVReaderImpl(config.getString(KeywordDrivenConstant.DRIVER_SHEET_PATH),
			// config.getString(KeywordDrivenConstant.ENVIRONMENT_SHEET_PATH));

			String driverPath = null;
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
				driverPath = baseResourceDir + File.separator + "TestSuite"
						+ File.separator + System.getProperty("suiteName")
						+ ".csv";
			}
			final CSVReaderImpl csvReader = new CSVReaderImpl(
					driverPath,
					config.getString(KeywordDrivenConstant.ENVIRONMENT_SHEET_PATH));
			// /////////
			template = cfg.getTemplate("runner.ftl");

			// get driver object which contains all the test cases .
			final Driver driver = csvReader.getDriver();

			// get all sequential test cases
			final List<TestCase> sequentialTestCaseList = driver
					.getSequentialTestCases();
			data.put("testList", sequentialTestCaseList);

			// File output
			// String output = "KeyDrivenTestCasesSuiteRunnerCI.java";
			File dir = new File(output);
			Files.createParentDirs(dir);
			Files.touch(dir);
			Writer writer = new FileWriter(dir);

			template.process(data, writer);
			writer.flush();
			writer.close();
			System.out.println("TestNG test cases generated");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
}
