package com.gdevelop.gwt.syncrpc.test.poj;

import java.net.CookieManager;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;

public class ProfileServiceTest extends TestCase {
	private static final String EMAIL = "test@example.com";

	private CookieManager userSession;
	private static ProfileService service;
	private static ProfileService guestSessionService;

	public ProfileServiceTest() {
		try {
			this.userSession = LoginUtils
					.loginAppEngine("http://localhost:8888",
							"http://localhost:8888", EMAIL, "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// public void testUserProfile() throws UnauthenticateException {
	// service = (ProfileService) SyncProxy.newProxyInstance(
	// ProfileService.class, RPCSyncTestSuite.BASE_URL, "profile",
	// userSession);
	//
	// UserInfo userInfo = service.getMyProfile();
	// assertNotNull(userInfo);
	// assertTrue(EMAIL.equals(userInfo.getEmail()));
	// }
	//
	// public void testUserProfile2() {
	// guestSessionService = (ProfileService) SyncProxy.newProxyInstance(
	// ProfileService.class, RPCSyncTestSuite.BASE_URL, "profile");
	//
	// try {
	// guestSessionService.getMyProfile();
	// fail();
	// } catch (UnauthenticateException e) {
	// throw new RuntimeException(e);
	// }
	// }
}
