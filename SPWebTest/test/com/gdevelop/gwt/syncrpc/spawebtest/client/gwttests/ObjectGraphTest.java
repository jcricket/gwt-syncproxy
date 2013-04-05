package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ObjectGraphTestService;
import com.google.gwt.user.client.rpc.ObjectGraphTestServiceAsync;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.TSFAccessor;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableDoublyLinkedNode;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializablePrivateNoArg;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableWithTwoArrays;
import com.google.gwt.user.client.rpc.TestSetValidator;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class ObjectGraphTest extends GWTTestCase {
	private static ObjectGraphTestServiceAsync service;

	public ObjectGraphTest() {
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	public void testAcyclicGraph() {
		service = GWT.create(ObjectGraphTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/objectgraphs");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testComplexCyclicGraph() {
		service = GWT.create(ObjectGraphTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/objectgraphs");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testComplexCyclicGraph2() {
		service = GWT.create(ObjectGraphTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/objectgraphs");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testDoublyReferencedArray() {
		service = GWT.create(ObjectGraphTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/objectgraphs");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testPrivateNoArg() {
		service = GWT.create(ObjectGraphTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/objectgraphs");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testTrivialCyclicGraph() {
		service = GWT.create(ObjectGraphTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/objectgraphs");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

}
