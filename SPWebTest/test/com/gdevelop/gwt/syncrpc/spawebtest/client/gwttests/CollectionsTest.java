package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestService;
import com.google.gwt.user.client.rpc.CollectionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArrayList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArraysAsList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashMapKey;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashMapValue;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashSet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeLinkedHashMapKey;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeLinkedHashMapValue;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeTreeMap;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeTreeSet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeVector;
import com.google.gwt.user.client.rpc.TestSetValidator;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CollectionsTest extends GWTTestCase {
	private static CollectionsTestServiceAsync service;

	public CollectionsTest() {

	}

	public void testArrayList() {
		service = GWT.create(CollectionsTestService.class);
		service.echo(
				TestSetFactory.createArrayList(),
				new AsyncCallback<ArrayList<TestSetFactory.MarkerTypeArrayList>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ArrayList<MarkerTypeArrayList> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(result));
						finishTest();
					}

				});
		delayTestFinish(2000);

	}

	public void testArraysAsList() {
		service = GWT.create(CollectionsTestService.class);
		final List<MarkerTypeArraysAsList> expected = TestSetFactory
				.createArraysAsList();
		service.echoArraysAsList(expected,
				new AsyncCallback<List<MarkerTypeArraysAsList>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(List<MarkerTypeArraysAsList> result) {
						assertNotNull(result);
						assertEquals(expected, result);
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testBooleanArray() {
		service = GWT.create(CollectionsTestService.class);
		final Boolean[] expected = TestSetFactory.createBooleanArray();
		service.echo(expected, new AsyncCallback<Boolean[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Boolean[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});

		delayTestFinish(2000);
	}

	public void testByteArray() {
		service = GWT.create(CollectionsTestService.class);
		final Byte[] expected = TestSetFactory.createByteArray();

		service.echo(expected, new AsyncCallback<Byte[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Byte[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testCharArray() {
		service = GWT.create(CollectionsTestService.class);
		final Character[] expected = TestSetFactory.createCharArray();

		service.echo(expected, new AsyncCallback<Character[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Character[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testDoubleArray() {
		service = GWT.create(CollectionsTestService.class);
		final Double[] expected = TestSetFactory.createDoubleArray();

		service.echo(expected, new AsyncCallback<Double[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Double[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testFloatArray() {
		service = GWT.create(CollectionsTestService.class);
		final Float[] expected = TestSetFactory.createFloatArray();

		service.echo(expected, new AsyncCallback<Float[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Float[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testHashMap() {
		service = GWT.create(CollectionsTestService.class);
		final HashMap<MarkerTypeHashMapKey, MarkerTypeHashMapValue> expected = TestSetFactory
				.createHashMap();
		service.echo(
				expected,
				new AsyncCallback<HashMap<MarkerTypeHashMapKey, MarkerTypeHashMapValue>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							HashMap<MarkerTypeHashMapKey, MarkerTypeHashMapValue> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(expected, result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testHashSet() {
		service = GWT.create(CollectionsTestService.class);
		final HashSet<MarkerTypeHashSet> expected = TestSetFactory
				.createHashSet();
		service.echo(expected, new AsyncCallback<HashSet<MarkerTypeHashSet>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(HashSet<MarkerTypeHashSet> result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.isValid(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testIntegerArray() {
		service = GWT.create(CollectionsTestService.class);
		final Integer[] expected = TestSetFactory.createIntegerArray();
		service.echo(expected, new AsyncCallback<Integer[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testLinkedHashMap() {
		service = GWT.create(CollectionsTestService.class);
		final LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> expected = TestSetFactory
				.createLinkedHashMap();
		service.echo(
				expected,
				new AsyncCallback<LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> result) {
						assertNotNull(result);
						expected.get("SerializableSet");
						result.get("SerializableSet");
						assertTrue(TestSetValidator.isValid(expected, result));
						finishTest();
					}

				});
		delayTestFinish(2000);

	}

	public void testLinkedHashMapLRU() {
		service = GWT.create(CollectionsTestService.class);

		final LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> expected = TestSetFactory
				.createLRULinkedHashMap();
		service.echo(
				expected,
				new AsyncCallback<LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> result) {
						assertNotNull(result);
						expected.get("SerializableSet");
						result.get("SerializableSet");
						assertTrue(TestSetValidator.isValid(expected, result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testLongArray() {
		service = GWT.create(CollectionsTestService.class);
		final Long[] expected = TestSetFactory.createLongArray();
		service.echo(expected, new AsyncCallback<Long[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Long[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveBooleanArray() {
		service = GWT.create(CollectionsTestService.class);
		final boolean[] expected = TestSetFactory.createPrimitiveBooleanArray();
		service.echo(expected, new AsyncCallback<boolean[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(boolean[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveByteArray() {
		service = GWT.create(CollectionsTestService.class);
		final byte[] expected = TestSetFactory.createPrimitiveByteArray();
		service.echo(expected, new AsyncCallback<byte[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(byte[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveCharArray() {
		service = GWT.create(CollectionsTestService.class);
		final char[] expected = TestSetFactory.createPrimitiveCharArray();
		service.echo(expected, new AsyncCallback<char[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(char[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveDoubleArray() {
		service = GWT.create(CollectionsTestService.class);
		final double[] expected = TestSetFactory.createPrimitiveDoubleArray();
		service.echo(expected, new AsyncCallback<double[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(double[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveFloatArray() {
		service = GWT.create(CollectionsTestService.class);
		final float[] expected = TestSetFactory.createPrimitiveFloatArray();
		service.echo(expected, new AsyncCallback<float[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(float[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveIntegerArray() {
		service = GWT.create(CollectionsTestService.class);
		final int[] expected = TestSetFactory.createPrimitiveIntegerArray();
		service.echo(expected, new AsyncCallback<int[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(int[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveLongArray() {
		service = GWT.create(CollectionsTestService.class);
		final long[] expected = TestSetFactory.createPrimitiveLongArray();
		service.echo(expected, new AsyncCallback<long[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(long[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testPrimitiveShortArray() {
		service = GWT.create(CollectionsTestService.class);
		final short[] expected = TestSetFactory.createPrimitiveShortArray();
		service.echo(expected, new AsyncCallback<short[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(short[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testShortArray() {
		service = GWT.create(CollectionsTestService.class);
		final Short[] expected = TestSetFactory.createShortArray();
		service.echo(expected, new AsyncCallback<Short[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Short[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testSqlDateArray() {
		service = GWT.create(CollectionsTestService.class);
		final java.sql.Date[] expected = TestSetFactory.createSqlDateArray();
		service.echo(expected, new AsyncCallback<java.sql.Date[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(java.sql.Date[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testSqlTimeArray() {
		service = GWT.create(CollectionsTestService.class);
		final Time[] expected = TestSetFactory.createSqlTimeArray();
		service.echo(expected, new AsyncCallback<Time[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Time[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testSqlTimestampArray() {
		service = GWT.create(CollectionsTestService.class);
		final Timestamp[] expected = TestSetFactory.createSqlTimestampArray();
		service.echo(expected, new AsyncCallback<Timestamp[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Timestamp[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testStringArray() {
		service = GWT.create(CollectionsTestService.class);
		final String[] expected = TestSetFactory.createStringArray();
		service.echo(expected, new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testStringArrayArray() {
		service = GWT.create(CollectionsTestService.class);
		final String[][] expected = new String[][] { new String[] { "hello" },
				new String[] { "bye" } };
		service.echo(expected, new AsyncCallback<String[][]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String[][] result) {
				assertNotNull(result);
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testTreeMapOptTrue() {
		service = GWT.create(CollectionsTestService.class);
		final TreeMap<String, MarkerTypeTreeMap> expected = TestSetFactory
				.createTreeMap(true);
		service.echo(expected, true,
				new AsyncCallback<TreeMap<String, MarkerTypeTreeMap>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							TreeMap<String, MarkerTypeTreeMap> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(expected, result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testTreeMapOptFalse() {
		service = GWT.create(CollectionsTestService.class);
		final TreeMap<String, MarkerTypeTreeMap> expected = TestSetFactory
				.createTreeMap(false);
		service.echo(expected, false,
				new AsyncCallback<TreeMap<String, MarkerTypeTreeMap>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							TreeMap<String, MarkerTypeTreeMap> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(expected, result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testTreeSetOptTrue() {
		service = GWT.create(CollectionsTestService.class);
		final TreeSet<MarkerTypeTreeSet> expected = TestSetFactory
				.createTreeSet(true);
		service.echo(expected, true,
				new AsyncCallback<TreeSet<MarkerTypeTreeSet>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(TreeSet<MarkerTypeTreeSet> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(expected, result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testTreeSetOptFalse() {
		service = GWT.create(CollectionsTestService.class);
		final TreeSet<MarkerTypeTreeSet> expected = TestSetFactory
				.createTreeSet(false);
		service.echo(expected, false,
				new AsyncCallback<TreeSet<MarkerTypeTreeSet>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(TreeSet<MarkerTypeTreeSet> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(expected, result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testVector() {
		service = GWT.create(CollectionsTestService.class);
		final Vector<MarkerTypeVector> expected = TestSetFactory.createVector();
		service.echo(expected, new AsyncCallback<Vector<MarkerTypeVector>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Vector<MarkerTypeVector> result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.isValid(expected, result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}
}
