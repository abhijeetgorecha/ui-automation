package com.ptc.integrity.automation.core.keydriven;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.log4j.Logger;

import com.ptc.integrity.automation.core.framework.PageDictionary;
import com.ptc.integrity.automation.core.framework.PageObjectFactory;
import com.ptc.integrity.automation.core.framework.Reporting;
import com.ptc.integrity.automation.core.utils.MethodUtils;

/**
 * Class to perform test execution .
 * 
 */
public class ExecutionEngine {

	/**
	 * Object repository instance which hold mapping of page object key and
	 * value .
	 */
	final private ObjectRepository repository = ObjectRepository
			.getRepository();

	final private static Logger LOGGER = Logger
			.getLogger(ExecutionEngine.class);
	/**
	 * temporary object holder which hold temporary value returned by test step.
	 */
	final private Map<String, Object> tempObjectHolder = new HashMap<String, Object>();

	/** The temporary page object dictionary. */
	final private PageDictionary tempPageObjectDictonary = new PageDictionary();

	/** The provider. */
	final private MultiBrowserProvider provider = new MultiBrowserProvider();

	public static String ignore = null;

	public static void setignore(String ignoreValue){
ignore = ignoreValue;
	}

	public static String getignore(){
return ignore;
	}

	/**
	 * Method to test one steps of one test case . This method also support of
	 * multiple browser scenario in which first browser is default browser and
	 * if test step contain different browser id it will open new browser to run
	 * test step for that browser .
	 * 
	 * @param testStep
	 *            the test step
	 * @param pageObjectFactory
	 *            the page object factory
	 * @return boolean true/false based on test case failure or success .
	 */

	public Boolean executeStep(final TestStep testStep,
			final PageObjectFactory pageObjectFactory) {
setignore(testStep.getIgnore());
		Boolean success = true;
		// get method parameter from test step
		final Object[] methodParam = null;
		String browserId = testStep.getBrowserId();

		// resolve out parameter if any and inject dictionary of this step in
		// page object
		this.resolveAndInjectStepDictionary(testStep, pageObjectFactory);

		// check whether this is default browser
		if (!provider.isDefaultBrowserRegistered()) {
			// register default browser
			provider.registerAsDefaultBrowser(browserId, pageObjectFactory);
		}

		PageObjectFactory newPageObjectFactory = null;

		// check whether this browser is open or not .
		if (provider.isBrowserOpen(browserId)) {
			// return existing page object factory
			newPageObjectFactory = provider
					.getBrowserPageObjectFactory(browserId);
		} else {
			// open new browser and return its page object factory
			newPageObjectFactory = provider.newBrowser(browserId,
					pageObjectFactory);
		}
		final Object[] constructorParam = { newPageObjectFactory };

		Object pageObject = null;
		Object testOutput = null;
		try {
			final String pageObjectClassName = repository.getValue(StringUtils
					.trimToEmpty(testStep.getPageObjectName()));
			if (null == pageObjectClassName) {
				throw new ClassNotFoundException(
						"Cannot find page object class");
			}
			// creating page object instance .
			pageObject = ConstructorUtils.invokeConstructor(
					Class.forName(pageObjectClassName), constructorParam);
			System.out.println("test step : "+ pageObjectClassName.substring(pageObjectClassName.lastIndexOf(".")+1) + ", "+testStep.getKeyWord());
			// invoke method mentioned in the test step. final Object
			testOutput = MethodUtils
					.invokeMethod(pageObject,
							StringUtils.trimToEmpty(testStep.getKeyWord()),
							methodParam);

		} catch (NoSuchMethodException e1) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[-- Error --]  Test Step : PageObjectName - [ "
						+ testStep.getPageObjectName() + " ] : Keyword - [ "
						+ testStep.getKeyWord()
						+ " ] : Error Message [ Cannot find Keyword ]");
			}
			e1.printStackTrace();
			success = false;
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			success = false;
		} catch (InvocationTargetException e1) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[-- Error --]  Test Step : PageObjectName - [ "
						+ testStep.getPageObjectName() + " ] : Keyword - [ "
						+ testStep.getKeyWord()
						+ " ] : Error Message [ Cannot find Keyword ]");
			}
			e1.printStackTrace();
			success = false;
		} catch (InstantiationException e1) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[-- Error --]  Test Step : PageObjectName - [ "
						+ testStep.getPageObjectName() + " ] : Keyword - [ "
						+ testStep.getKeyWord()
						+ " ] : Error Message [ Cannot find PageObjectName ]");
			}
			success = false;
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[-- Error --]  Test Step : PageObjectName -  "
						+ testStep.getPageObjectName() + "  : Keyword - "
						+ testStep.getKeyWord()
						+ " : Error Message [ Cannot find PageObjectName ]");
			}
			e1.printStackTrace();
			success = false;
		}
		// null test output , fail test case
		if (null == testOutput) {
			return false;
		}
		// check whether test step has out parameter
		if (StringUtils.isNotEmpty(testStep.getOutputKey())
				&& StringUtils.isNotEmpty(testStep.getOutputType())) {
			tempObjectHolder.put(testStep.getOutputKey(), testOutput);
		}
		System.out.println("output : " + testOutput);
		// if test result is true/ false , return the test status .
		if (testOutput instanceof Boolean) {
			success = (Boolean) testOutput;
		}

		return success;
	}

	/**
	 * Inject and resolve dictionary for a step .
	 * 
	 * @param testStep
	 *            the test step
	 * @param pageObjectFactory
	 *            the page object factory
	 */
	private void resolveAndInjectStepDictionary(final TestStep testStep,
			final PageObjectFactory pageObjectFactory) {
		pageObjectFactory.getDictionary().clear();
		pageObjectFactory.getDictionary().putAll(tempPageObjectDictonary);
		Map<String, String> testStepDictonary = testStep.getDictonary();
		PageDictionary pageObjectDictonary = pageObjectFactory.getDictionary();
		Set<Entry<String, String>> entrySet = testStepDictonary.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			if (this.isOutParameter(entry.getValue())) {
				pageObjectDictonary
						.putObject(entry.getKey(), tempObjectHolder
								.get(getOutParameter(entry.getValue())));
			} else {
				pageObjectDictonary.putObject(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Method to execute one test case .
	 * 
	 * @param test
	 *            the test
	 * @param pageObjectFactory
	 *            the page object factory
	 * @return boolean true/false based on test case failure or success .
	 */
	public boolean executeTest(final TestCase test,
			final PageObjectFactory pageObjectFactory) {
		boolean success = true;
		final List<TestStep> testSteps = test.getTestStepList();
		final Iterator<TestStep> testStepIterator = testSteps.iterator();
		// clear temporary variable holder before executing test case
		tempObjectHolder.clear();
		tempPageObjectDictonary.clear();
		tempPageObjectDictonary.putAll(pageObjectFactory.getDictionary());
		while (testStepIterator.hasNext()) {
			final TestStep step = testStepIterator.next();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info("[-- " + test.getTestCaseName() + " --]"
						+ " Executing Test Step : PageObjectName - [ "
						+ step.getPageObjectName() + "]  : Keyword - [ "
						+ step.getKeyWord() + " ]");
			}
			Reporting.stDescription=step.getDescription();
			success = executeStep(step, pageObjectFactory);
			// break if any test step fail
			if (!success) {
				break;
			}
		}
		// clear temporary variable holder after executing test case
		tempObjectHolder.clear();
		tempPageObjectDictonary.clear();
		tempPageObjectDictonary.putAll(pageObjectFactory.getDictionary());
		provider.close();
		return success;
	}

	/**
	 * Method to execute List of test cases .
	 * 
	 * @param testCases
	 *            List of test cases .
	 * @param pageObjectFactory
	 *            the page object factory
	 * @return boolean true/false based on test case failure or success .
	 */
	public boolean executeTestCases(final List<TestCase> testCases,
			final PageObjectFactory pageObjectFactory) {
		boolean success = true;
		final Iterator<TestCase> testCaseIterator = testCases.iterator();
		while (testCaseIterator.hasNext()) {
			final TestCase test = testCaseIterator.next();
			LOGGER.info("Executing Test Case : " + test.getTestCaseName());
			success = executeTest(test, pageObjectFactory);
		}
		return success;
	}

	/**
	 * Get out parameter from input parameter . out parameter will be seperated
	 * by : symbol
	 * 
	 * @param param
	 *            input parameter
	 * @return out parameter
	 */
	private String getOutParameter(final Object param) {
		if (param instanceof String) {
			final String strParam = (String) param;
			final String[] paramToken = strParam.split(":");
			if (paramToken.length > 1) {
				return paramToken[1];
			}

		}
		return null;
	}

	/**
	 * Check whether input parameter is an actual out parameter.
	 * 
	 * @param param
	 *            input parameter
	 * @return true/false
	 */
	private boolean isOutParameter(final Object param) {
		if (param instanceof String) {
			final String strParam = (String) param;
			final String[] paramToken = strParam.split(":");
			if (paramToken.length > 0 && paramToken[0].equalsIgnoreCase("out")) {
				return true;
			}
		}
		return false;
	}

}
