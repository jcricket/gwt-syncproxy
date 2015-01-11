/**
 * Jan 8, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.test.safehtml;

import com.google.gwt.safehtml.shared.GwtSafeHtmlBuilderTest;
import com.google.gwt.safehtml.shared.SafeHtmlHostedModeUtils;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class POJGwtSafeHtmlBuilderTest extends GwtSafeHtmlBuilderTest {
	/**
	 * @see com.google.gwt.safehtml.shared.GwtSafeHtmlBuilderTest#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return null;
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#gwtSetUp()
	 */
	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		SafeHtmlHostedModeUtils.setForceCheckCompleteHtml(true);
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#gwtTearDown()
	 */
	@Override
	protected void gwtTearDown() throws Exception {
		SafeHtmlHostedModeUtils.setForceCheckCompleteHtml(false);
		super.gwtTearDown();
	}
}
