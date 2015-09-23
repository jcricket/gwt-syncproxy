package com.gdevelop.gwt.syncrpc.spawebtest.client;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.spawebtest.server.ProfileServiceImpl;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UnauthenticateException;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class ProfileServiceTest extends TestCase {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalUserServiceTestConfig()).setEnvIsAdmin(true)
			.setEnvIsLoggedIn(true).setEnvEmail("test@test.com")
			.setEnvAuthDomain("GSP");

	// @Override
	// public String getModuleName() {
	// return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	// }

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		helper.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		helper.tearDown();
	}

	// public ProfileServiceAsync getAsyncService() {
	// return GWT.create(ProfileService.class);
	// }

	public void testProfileService() throws UnauthenticateException {
		// ProfileServiceAsync service = getAsyncService();
		//
		// service.getMyProfile(new AsyncCallback<UserInfo>() {
		// @Override
		// public void onFailure(Throwable caught) {
		// throw new RuntimeException(caught);
		// }
		//
		// @Override
		// public void onSuccess(UserInfo result) {
		// assertNotNull("Failed to get UserInfo", result);
		// // finishTest();
		// }
		// });
		// // delayTestFinish(5000);
		ProfileService service = new ProfileServiceImpl();
		assertNotNull(service.getAuthProfile());
	}
}
