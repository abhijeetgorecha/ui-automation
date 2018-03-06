package com.ptc.integrity.automation.core.keydriven;

import java.util.List;

/**
 * Driver class hold all test cases.
 */
public class Driver {

	/** The batch test cases. */
	List<List<TestCase>> batchTestCases;

	/** The sequential test cases. */
	List<TestCase> sequentialTestCases;

	/**
	 * Gets the batch test cases.
	 * 
	 * @return the batch test cases
	 */
	public List<List<TestCase>> getBatchTestCases() {
		return batchTestCases;
	}

	/**
	 * Gets the sequential test cases.
	 * 
	 * @return the sequential test cases
	 */
	public List<TestCase> getSequentialTestCases() {
		return sequentialTestCases;
	}

	/**
	 * Sets the batch test cases.
	 * 
	 * @param batchTestCases
	 *            the new batch test cases
	 */
	public void setBatchTestCases(final List<List<TestCase>> batchTestCases) {
		this.batchTestCases = batchTestCases;
	}

	/**
	 * Sets the sequential test cases.
	 * 
	 * @param sequentialTestCases
	 *            the new sequential test cases
	 */
	public void setSequentialTestCases(final List<TestCase> sequentialTestCases) {
		this.sequentialTestCases = sequentialTestCases;
	}

}
