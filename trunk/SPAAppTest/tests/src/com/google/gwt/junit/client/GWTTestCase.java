/**
 * Jan 8, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.google.gwt.junit.client;

import junit.framework.TestCase;

/**
 * Replace with simple Testing frame for SafeHtmlTests
 *
 * @author Preethum
 * @since 0.6
 *
 */
public abstract class GWTTestCase extends TestCase {
	/**
	 * DOES NOTHING
	 *
	 * @param mod
	 */
	public abstract String getModuleName();

	/**
	 * DOES NOTHING
	 */
	protected void gwtSetUp() throws Exception {
	}

	/**
	 * DOES NOTHING
	 */
	protected void gwtTearDown() throws Exception {
	}
}
