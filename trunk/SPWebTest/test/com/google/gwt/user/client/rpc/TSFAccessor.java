package com.google.gwt.user.client.rpc;

import com.google.gwt.user.client.rpc.TestSetFactory.SerializableDoublyLinkedNode;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableWithTwoArrays;

/**
 * A simple access object to access protected methods in the TestSetFactory
 * class.
 * 
 * @author Preethum
 * @since 0.4
 * 
 */
public class TSFAccessor {
	public static SerializableDoublyLinkedNode createAcyclicGraph() {
		return TestSetFactory.createAcyclicGraph();
	}

	public static SerializableDoublyLinkedNode createComplexCyclicGraph() {
		return TestSetFactory.createComplexCyclicGraph();
	}

	public static SerializableWithTwoArrays createDoublyReferencedArray() {
		return TestSetFactory.createDoublyReferencedArray();
	}

	public static SerializableDoublyLinkedNode createTrivialCyclicGraph() {
		return TestSetFactory.createTrivialCyclicGraph();
	}
}
