/**
 * Dec 30, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests to verify the core of SyncProxy
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class SyncProxyCoreTests extends TestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("SyncProxy Core Tests");
		suite.addTestSuite(SyncProxyTest.class);
		suite.addTestSuite(RemoteServiceInvocationHandlerTest.class);
		return suite;
	}
}
