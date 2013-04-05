package com.gdevelop.gwt.syncrpc.test.client;

import java.util.HashSet;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestService;
import com.google.gwt.user.client.rpc.CoreJavaTestService;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestService;
import com.google.gwt.user.client.rpc.EnumsTestService;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;
import com.google.gwt.user.client.rpc.ExceptionsTestService;
import com.google.gwt.user.client.rpc.ExceptionsTestServiceAsync;
import com.google.gwt.user.client.rpc.InheritanceTestService;
import com.google.gwt.user.client.rpc.MixedSerializableEchoService;
import com.google.gwt.user.client.rpc.ObjectGraphTestService;
import com.google.gwt.user.client.rpc.RecursiveClassTestService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestService;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestServiceAsync;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedFieldClass;
import com.google.gwt.user.client.rpc.UnicodeEscapingService;
import com.google.gwt.user.client.rpc.UnicodeEscapingService.InvalidCharacterException;
import com.google.gwt.user.client.rpc.UnicodeEscapingServiceAsync;
import com.google.gwt.user.client.rpc.UnicodeEscapingTest;
import com.google.gwt.user.client.rpc.ValueTypesTestService;
import com.google.gwt.user.client.rpc.XsrfTestService;

@SuppressWarnings("unused")
public class AllTest implements EntryPoint {
	public AllTest() {
	}

	@Override
	public void onModuleLoad() {
		GWT.create(ValueTypesTestService.class);
		GWT.create(EnumsTestService.class);
		GWT.create(InheritanceTestService.class);
		GWT.create(CollectionsTestService.class);
		GWT.create(CoreJavaTestService.class);
		GWT.create(CustomFieldSerializerTestService.class);
		GWT.create(ExceptionsTestService.class);
		GWT.create(ObjectGraphTestService.class);
		// GWT.create(RemoteService.class);
		// GWT.create(RpcTokenTest.class);
		GWT.create(UnicodeEscapingService.class);
		GWT.create(MixedSerializableEchoService.class);
		GWT.create(RecursiveClassTestService.class);
		GWT.create(TypeCheckedObjectsTestService.class);
		GWT.create(XsrfTestService.class);

		GWT.create(ProfileService.class);
		GWT.create(LargePayloadService.class);

		// testProfileService();
		// testEnums();
		// testTypeCheckedObjects();
		// testUnicodeEscaping();
		testLargePayload();
	}

	private void testProfileService() {
		ProfileServiceAsync service = (ProfileServiceAsync) GWT
				.create(ProfileService.class);
		service.getMyProfile(new AsyncCallback<UserInfo>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(UserInfo result) {
				Window.alert(result.getEmail());
			}
		});
	}

	private void testEnums() {
		EnumsTestServiceAsync service = (EnumsTestServiceAsync) GWT
				.create(EnumsTestService.class);
		service.echo(Basic.A, new AsyncCallback<Basic>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(EnumsTestService.Basic result) {
				System.out.println("result: " + result);
			}
		});
	}

	private void testExceptions() {
		ExceptionsTestServiceAsync service = (ExceptionsTestServiceAsync) GWT
				.create(ExceptionsTestService.class);
		service.echo(TestSetFactory.createUmbrellaException(),
				new AsyncCallback<UmbrellaException>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(UmbrellaException result) {
						System.out.println("result: " + result);
					}
				});
	}

	private void testTypeCheckedObjects() {
		TypeCheckedObjectsTestServiceAsync service = (TypeCheckedObjectsTestServiceAsync) GWT
				.create(TypeCheckedObjectsTestService.class);

		TypeCheckedFieldClass<HashSet<Integer>, String> arg1 = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedFieldClass();
		service.echo(arg1,
				new AsyncCallback<TypeCheckedFieldClass<Integer, String>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							TypeCheckedObjectsTestSetFactory.TypeCheckedFieldClass<Integer, String> result) {
						System.out.println("result: " + result);
					}
				});
	}

	private void testUnicodeEscaping() {
		UnicodeEscapingServiceAsync service = (UnicodeEscapingServiceAsync) GWT
				.create(UnicodeEscapingService.class);
		ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "unicodeEscape");

		int start = Character.MAX_SURROGATE + 1;
		int end = start + 64;
		String data = UnicodeEscapingTest.getStringContainingCharacterRange(
				start, end);
		try {
			service.verifyStringContainingCharacterRange(start, end, data,
					new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(Boolean result) {
							System.out.println("Result: " + result);
						}
					});
		} catch (InvalidCharacterException e) {
			e.printStackTrace();
		}
	}

	private void testLargePayload() {
		LargePayloadServiceAsync service = (LargePayloadServiceAsync) GWT
				.create(LargePayloadService.class);

		service.testLargeResponsePayload(new AsyncCallback<List<UserInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<UserInfo> result) {
				System.out.println("result: " + result);
			}
		});

		service.testLargeResponseArray(new AsyncCallback<int[]>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(int[] result) {
				System.out.println("result len: " + result.length);
			}
		});
	}
}
