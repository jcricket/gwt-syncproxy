package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
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
public class ObjectGraphTest extends TestCase {
	private static ObjectGraphTestServiceAsync service = (ObjectGraphTestServiceAsync) SyncProxy
			.newProxyInstance(ObjectGraphTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "objectgraphs", true);

	public ObjectGraphTest() {
	}

	public void testAcyclicGraph() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo_AcyclicGraph(TSFAccessor.createAcyclicGraph(),
				new AsyncCallback<SerializableDoublyLinkedNode>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(SerializableDoublyLinkedNode result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValidAcyclicGraph(result));
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testComplexCyclicGraph() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo_ComplexCyclicGraph(TSFAccessor.createComplexCyclicGraph(),
				new AsyncCallback<SerializableDoublyLinkedNode>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(SerializableDoublyLinkedNode result) {
						assertNotNull(result);
						assertTrue(TestSetValidator
								.isValidComplexCyclicGraph(result));
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testComplexCyclicGraph2() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		SerializableDoublyLinkedNode node = TSFAccessor
				.createComplexCyclicGraph();
		service.echo_ComplexCyclicGraph(node, node,
				new AsyncCallback<SerializableDoublyLinkedNode>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(SerializableDoublyLinkedNode result) {
						assertNotNull(result);
						assertTrue(TestSetValidator
								.isValidComplexCyclicGraph(result));
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testDoublyReferencedArray() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		SerializableWithTwoArrays node = TSFAccessor
				.createDoublyReferencedArray();
		service.echo_SerializableWithTwoArrays(node,
				new AsyncCallback<SerializableWithTwoArrays>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(SerializableWithTwoArrays result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(result));
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testPrivateNoArg() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		SerializablePrivateNoArg node = TestSetFactory.createPrivateNoArg();
		service.echo_PrivateNoArg(node,
				new AsyncCallback<SerializablePrivateNoArg>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(SerializablePrivateNoArg result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(result));
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTrivialCyclicGraph() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo_TrivialCyclicGraph(TSFAccessor.createTrivialCyclicGraph(),
				new AsyncCallback<SerializableDoublyLinkedNode>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(SerializableDoublyLinkedNode result) {
						assertNotNull(result);
						assertTrue(TestSetValidator
								.isValidTrivialCyclicGraph(result));
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

}
