/**
 * Dec 31, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.test.poj;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class CustomPojTests extends TestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Custom POJ Service Tests");
		suite.addTestSuite(GreetingServiceTest.class);
		suite.addTestSuite(LargePayloadTest.class);
		suite.addTestSuite(CookieServiceTest.class);
		// suite.addTestSuite(HttpsTest.class);
		// suite.addTestSuite(ProfileServiceTest.class);
		// suite.addTestSuite(SessionTest.class);
		suite.addTestSuite(SyncedServicesTest.class);
		return suite;
	}
}
