package com.gdevelop.gwt.syncrpc.test.poj;

import java.net.CookieManager;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.ProxySettings;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UnauthenticateException;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.RpcTestBase;

public class ProfileServiceTest extends RpcTestBase {
	private static final String EMAIL = "test@example.com";

	private CookieManager userSession;
	private static ProfileService service;
	private static ProfileService guestSessionService;

	public ProfileServiceTest() {
		try {
			this.userSession = LoginUtils.loginAppEngine(getBaseURL(),
					getBaseURL(), EMAIL, "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void testUserProfile() throws UnauthenticateException {
		service = SyncProxy.createProxy(ProfileService.class,
				new ProxySettings().setCookieManager(userSession));

		UserInfo userInfo = service.getAuthProfile();
		assertNotNull(userInfo);
		assertTrue(EMAIL.equals(userInfo.getEmail()));
	}

	public void testUserProfile2() {
		guestSessionService = SyncProxy.createSync(ProfileService.class);

		try {
			guestSessionService.getAuthProfile();
			fail();
		} catch (UnauthenticateException e) {
			// Expected
		}
	}
}
