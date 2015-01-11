/**
 * Dec 30, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.gdevelop.gwt.syncrpc.SyncProxyCoreTests;
import com.gdevelop.gwt.syncrpc.test.poj.CustomPojTests;
import com.gdevelop.gwt.syncrpc.test.poj.PojGSPTests;

/**
 * Tests core and remote tests
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class SyncProxyLibraryTests extends TestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("SyncProxy Library Tests");
		suite.addTest(SyncProxyCoreTests.suite());
		suite.addTest(CustomPojTests.suite());
		suite.addTest(PojGSPTests.suite());
		return suite;
	}
}
