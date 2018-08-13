package com.ptc.integrity.automation.core.framework;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.util.List;

public class SuiteListener implements IAlterSuiteListener {
    @Override
    public void alter(List<XmlSuite> suites) {
        int count = Integer.parseInt(System.getProperty("threadCount", "-1"));
        if (count > 0) {
            System.out.println("Alter Suite - Set thread count to " + count);
            for (XmlSuite suite : suites) {
                suite.setThreadCount(count);
            }
        }

        String userProfile = System.getProperty("userProfile");
        if (userProfile != null) {
            System.out.println("Alter Suite - Include user profile group " + userProfile);
            for (XmlSuite suite : suites) {
                suite.setIncludedGroups(Arrays.asList(userProfile.split("\\s*,\\s*")));
            }
        }
    }
}

