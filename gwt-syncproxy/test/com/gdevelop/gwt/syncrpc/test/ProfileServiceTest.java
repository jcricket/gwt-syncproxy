package com.gdevelop.gwt.syncrpc.test;

import java.net.CookieManager;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.test.client.ProfileService;
import com.gdevelop.gwt.syncrpc.test.client.UnauthenticateException;
import com.gdevelop.gwt.syncrpc.test.client.UserInfo;

public class ProfileServiceTest extends TestCase {
	private static final String EMAIL = "test@example.com";

	private CookieManager userSession;
	private static ProfileService service;
	private static ProfileService guestSessionService;

	public ProfileServiceTest() {
		try {
			userSession = LoginUtils.loginAppEngine("http://localhost:8888",
					"http://localhost:8888", EMAIL, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testUserProfile() throws UnauthenticateException {
		service = (ProfileService) SyncProxy.newProxyInstance(
				ProfileService.class, RPCSyncTestSuite.BASE_URL, "profile",
				userSession);

		UserInfo userInfo = service.getMyProfile();
		assertNotNull(userInfo);
		assertTrue(EMAIL.equals(userInfo.getEmail()));
	}

	public void testUserProfile2() {
		guestSessionService = (ProfileService) SyncProxy.newProxyInstance(
				ProfileService.class, RPCSyncTestSuite.BASE_URL, "profile");

		try {
			guestSessionService.getMyProfile();
			fail();
		} catch (UnauthenticateException e) {
			e.printStackTrace();
		}
	}
}
