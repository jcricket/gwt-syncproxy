/**
 * Jan 8, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import com.gdevelop.gwt.syncrpc.test.safehtml.POJGwtSafeHtmlBuilderTest;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class AndroidGwtSafeHtmlBuilderTest extends POJGwtSafeHtmlBuilderTest {
	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#gwtSetUp()
	 */
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#gwtTearDown()
	 */
	@Override
	protected void gwtTearDown() throws Exception {
		super.gwtTearDown();
	}

	@Override
	public void testAppendHtmlConstant_withIncompleteHtml() {
		// Short-circuited, simulating GWT Production Mode from original test
	}
}
