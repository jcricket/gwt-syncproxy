package com.gdevelop.gwt.syncrpc.spawebtest.client;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProfileServiceTest extends GWTTestCase {
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	public void testProfileService() {
		ProfileServiceAsync service = (ProfileServiceAsync) GWT
				.create(ProfileService.class);
		service.getMyProfile(new AsyncCallback<UserInfo>() {
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(UserInfo result) {
				assertNotNull("Failed to get UserInfo", result);
				finishTest();
			}
		});
		delayTestFinish(5000);
	}
}
