/**
 * Dec 30, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.test.MissingTestServiceAsync;
import com.gdevelop.gwt.syncrpc.test.TestService;
import com.gdevelop.gwt.syncrpc.test.TestServiceAsync;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class RemoteServiceInvocationHandlerTest extends TestCase {

	private class TestMissingProxyAsync implements MissingTestServiceAsync {
	}

	private class TestProxy implements TestService {
	}

	private class TestProxyAsync implements TestServiceAsync {
	}

	public void testDetermineProxyServiceBaseInterface()
			throws ClassNotFoundException {
		assertEquals("Wrong service class", TestService.class,
				RemoteServiceInvocationHandler
				.determineProxyServiceBaseInterface(new TestProxy()));
		assertEquals(
				"Wrong service class for async",
				TestService.class,
				RemoteServiceInvocationHandler
				.determineProxyServiceBaseInterface(new TestProxyAsync()));
		try {
			RemoteServiceInvocationHandler
			.determineProxyServiceBaseInterface(new TestMissingProxyAsync());
			fail("Should have thrown exception unfound class");
		} catch (ClassNotFoundException cnfe) {

		}
	}
}
