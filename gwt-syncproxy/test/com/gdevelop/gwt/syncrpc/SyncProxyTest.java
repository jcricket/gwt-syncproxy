/**
 * Dec 30, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc;

import java.net.CookieManager;
import java.net.CookiePolicy;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.exception.SyncProxyException;
import com.gdevelop.gwt.syncrpc.exception.SyncProxyException.InfoType;
import com.gdevelop.gwt.syncrpc.test.MissingTestServiceAsync;
import com.gdevelop.gwt.syncrpc.test.NoAnnotTestService;
import com.gdevelop.gwt.syncrpc.test.NoAnnotTestServiceAsync;
import com.gdevelop.gwt.syncrpc.test.TestService;
import com.gdevelop.gwt.syncrpc.test.TestServiceAsync;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Tests the static verification methods within the main SyncProxy class. All
 * tests included here should not require a remote service to actually read from
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class SyncProxyTest extends TestCase {
	public interface InnerMissingTestServiceAsync {
	}

	public interface InnerNoAnnotTestService extends RemoteService {

	}

	public interface InnerNoAnnotTestServiceAsync {

	}

	@RemoteServiceRelativePath("innertest")
	public interface InnerTestService extends RemoteService {

	}

	public interface InnerTestServiceAsync {

	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Reset SyncProxy
		SyncProxy.setBaseURL(null);
	}

	public void testClassGetRemoteServiceRelativePathFromAnnotation() {
		assertEquals(
				"Wrong path from annotation",
				"test",
				SyncProxy
				.getRemoteServiceRelativePathFromAnnotation(TestService.class));
		assertEquals(
				"Wrong path from Async annotation",
				"test",
				SyncProxy
				.getRemoteServiceRelativePathFromAnnotation(TestServiceAsync.class));
		try {
			SyncProxy
			.getRemoteServiceRelativePathFromAnnotation(NoAnnotTestService.class);
			fail("Should have thrown an exception");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.REMOTE_SERVICE_RELATIVE_PATH);
		}
		try {
			SyncProxy
			.getRemoteServiceRelativePathFromAnnotation(NoAnnotTestServiceAsync.class);
			fail("Should have thrown an exception");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.REMOTE_SERVICE_RELATIVE_PATH);
		}
		try {
			SyncProxy
			.getRemoteServiceRelativePathFromAnnotation(MissingTestServiceAsync.class);
			fail("Should have thrown an exception");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.SERVICE_BASE);
		}
	}

	public void testDefaultUnsetSettings() {
		ProxySettings settings = new ProxySettings();
		try {
			SyncProxy.defaultUnsetSettings(InnerTestService.class, settings);
			fail("Should have failed on lack of a server base url available");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.MODULE_BASE_URL);
		}
		// Setup in-place policy's for services test below
		String itsPolicy = "ITSPolicy";
		String inatsPolicy = "INATSPolicy";
		SyncProxy.POLICY_MAP.put(InnerTestService.class.getName(), itsPolicy);
		SyncProxy.POLICY_MAP.put(InnerNoAnnotTestService.class.getName(),
				inatsPolicy);
		String testUrl = "testUrl";
		String testUrl2 = "testUrl2";
		// Test Default assignment to moduleBaseUrl
		SyncProxy.moduleBaseURL = testUrl;
		SyncProxy.defaultUnsetSettings(InnerTestService.class, settings);
		assertEquals("Failed default module base url assignment", testUrl,
				settings.getModuleBaseUrl());
		// Test Override of moduleBaseUrl
		settings.setModuleBaseUrl(testUrl2);
		SyncProxy.defaultUnsetSettings(InnerTestService.class, settings);
		assertEquals("Failed override module base url assignment", testUrl2,
				settings.getModuleBaseUrl());
		// Test relative path assignment
		settings = new ProxySettings();
		try {
			SyncProxy.defaultUnsetSettings(InnerNoAnnotTestService.class,
					settings);
			fail("Should have failed on lack of available annotation");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.REMOTE_SERVICE_RELATIVE_PATH);
		}
		String testRelativePath = "relativePath";
		settings.setRemoteServiceRelativePath(testRelativePath);
		SyncProxy.defaultUnsetSettings(InnerNoAnnotTestService.class, settings);
		assertEquals("Failed to utilized manual relative path",
				testRelativePath, settings.getRemoteServiceRelativePath());
		SyncProxy.defaultUnsetSettings(InnerTestService.class, settings);
		assertEquals("Failed to utilized manual relative path with annotation",
				testRelativePath, settings.getRemoteServiceRelativePath());
		// Cookie manager should always be available
		assertNotNull("Cookie manager should be default if not provided",
				settings.getCookieManager());
		CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		settings.setCookieManager(cm);
		SyncProxy.defaultUnsetSettings(InnerTestService.class, settings);
		assertEquals("Wrong Cookie Manager", cm, settings.getCookieManager());
		// Test policy names for provided test classes , then remove to check
		// for exception when missing
		settings = new ProxySettings();
		SyncProxy.defaultUnsetSettings(InnerTestService.class, settings);
		assertEquals("Wrong policy for InnerTestService", itsPolicy,
				settings.getPolicyName());
		settings = new ProxySettings();
		settings.setRemoteServiceRelativePath(testRelativePath);
		SyncProxy.defaultUnsetSettings(InnerNoAnnotTestService.class, settings);
		assertEquals("Wrong policy for InnerNoAnnotTestService", inatsPolicy,
				settings.getPolicyName());
		SyncProxy.POLICY_MAP.remove(InnerTestService.class.getName());
		SyncProxy.POLICY_MAP.remove(InnerNoAnnotTestService.class.getName());
		try {
			SyncProxy.defaultUnsetSettings(InnerTestService.class, settings);
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.POLICY_NAME_MISSING);
		}
	}

	public void testInnerClassGetRemoteServiceRelativePathFromAnnotation() {
		assertEquals(
				"Wrong path from annotation",
				"innertest",
				SyncProxy
						.getRemoteServiceRelativePathFromAnnotation(InnerTestService.class));
		assertEquals(
				"Wrong path from Async annotation",
				"innertest",
				SyncProxy
						.getRemoteServiceRelativePathFromAnnotation(InnerTestServiceAsync.class));
		try {
			SyncProxy
			.getRemoteServiceRelativePathFromAnnotation(InnerNoAnnotTestService.class);
			fail("Should have thrown an exception");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.REMOTE_SERVICE_RELATIVE_PATH);
		}
		try {
			SyncProxy
			.getRemoteServiceRelativePathFromAnnotation(InnerNoAnnotTestServiceAsync.class);
			fail("Should have thrown an exception");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.REMOTE_SERVICE_RELATIVE_PATH);
		}
		try {
			SyncProxy
					.getRemoteServiceRelativePathFromAnnotation(InnerMissingTestServiceAsync.class);
			fail("Should have thrown an exception");
		} catch (SyncProxyException spe) {
			spe.verify(InfoType.SERVICE_BASE);
		}
	}
}
