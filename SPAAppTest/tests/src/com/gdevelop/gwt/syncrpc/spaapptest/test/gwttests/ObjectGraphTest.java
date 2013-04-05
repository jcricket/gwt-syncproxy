package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ObjectGraphTestServiceAsync;
import com.google.gwt.user.client.rpc.TSFAccessor;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableDoublyLinkedNode;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializablePrivateNoArg;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableWithTwoArrays;
import com.google.gwt.user.client.rpc.TestSetValidator;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class ObjectGraphTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	ObjectGraphTestServiceAsync service;

	public ObjectGraphTest() throws Throwable {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (ObjectGraphTestServiceAsync) SyncProxy
						.newProxyInstance(ObjectGraphTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"objectgraphs", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testAcyclicGraph() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_AcyclicGraph(TSFAccessor.createAcyclicGraph(),
						new AsyncCallback<SerializableDoublyLinkedNode>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									SerializableDoublyLinkedNode result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidAcyclicGraph(result));
								signal.countDown();
							}

						});
				return null;
			}
		};
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testComplexCyclicGraph() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_ComplexCyclicGraph(
						TSFAccessor.createComplexCyclicGraph(),
						new AsyncCallback<SerializableDoublyLinkedNode>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									SerializableDoublyLinkedNode result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidComplexCyclicGraph(result));
								signal.countDown();
							}

						});
				return null;
			}
		};
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testComplexCyclicGraph2() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final SerializableDoublyLinkedNode node = TSFAccessor
				.createComplexCyclicGraph();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_ComplexCyclicGraph(node, node,
						new AsyncCallback<SerializableDoublyLinkedNode>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									SerializableDoublyLinkedNode result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidComplexCyclicGraph(result));
								signal.countDown();
							}

						});
				return null;
			}
		};
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testDoublyReferencedArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final SerializableWithTwoArrays node = TSFAccessor
				.createDoublyReferencedArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_SerializableWithTwoArrays(node,
						new AsyncCallback<SerializableWithTwoArrays>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									SerializableWithTwoArrays result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
								signal.countDown();
							}

						});
				return null;
			}
		};
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testPrivateNoArg() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final SerializablePrivateNoArg node = TestSetFactory
				.createPrivateNoArg();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_PrivateNoArg(node,
						new AsyncCallback<SerializablePrivateNoArg>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									SerializablePrivateNoArg result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
								signal.countDown();
							}

						});
				return null;
			}
		};
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTrivialCyclicGraph() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_TrivialCyclicGraph(
						TSFAccessor.createTrivialCyclicGraph(),
						new AsyncCallback<SerializableDoublyLinkedNode>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									SerializableDoublyLinkedNode result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidTrivialCyclicGraph(result));
								signal.countDown();
							}

						});
				return null;
			}
		};
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

}
