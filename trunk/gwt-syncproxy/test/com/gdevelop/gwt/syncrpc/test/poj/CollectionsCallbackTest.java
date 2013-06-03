package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArrayList;
import com.google.gwt.user.client.rpc.TestSetValidator;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CollectionsCallbackTest extends TestCase {
	private static CollectionsTestServiceAsync service = (CollectionsTestServiceAsync) SyncProxy
			.newProxyInstance(CollectionsTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "collections", true);

	public void testArrayList() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		service.echo(TestSetFactory.createArrayList(),
				new AsyncCallback<ArrayList<MarkerTypeArrayList>>() {
					@Override
					public void onFailure(Throwable caught) {
						fail(caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<MarkerTypeArrayList> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(result));
						signal.countDown();
					}
				});
		assertTrue("Failed to complete", signal.await(10, TimeUnit.SECONDS));
	}

	public void testBooleanArray() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		final Boolean[] expected = TestSetFactory.createBooleanArray();
		service.echo(expected, new AsyncCallback<Boolean[]>() {
			@Override
			public void onFailure(Throwable caught) {
				fail(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				signal.countDown();
			}
		});
		assertTrue("Failed to complete", signal.await(10, TimeUnit.SECONDS));
	}
}
