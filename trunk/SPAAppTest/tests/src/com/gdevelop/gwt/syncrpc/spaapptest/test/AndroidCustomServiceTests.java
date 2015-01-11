/**
 * Jan 1, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spaapptest.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class AndroidCustomServiceTests extends TestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Custom Service Tests");
		suite.addTestSuite(LargePayloadServiceTest.class);
		suite.addTestSuite(GreetingServiceTest.class);
		suite.addTestSuite(SyncedServicesTest.class);
		suite.addTestSuite(CookieServiceTest.class);
		return suite;
	}
}
