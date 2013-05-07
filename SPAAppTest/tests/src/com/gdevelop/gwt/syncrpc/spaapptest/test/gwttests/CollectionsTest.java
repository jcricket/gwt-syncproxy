package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
public class CollectionsTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	CollectionsTestServiceAsync service;

	public CollectionsTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (CollectionsTestServiceAsync) SyncProxy
						.newProxyInstance(CollectionsTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"collections", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testArrayList() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TestSetFactory.createArrayList(),
						new AsyncCallback<ArrayList<TestSetFactory.MarkerTypeArrayList>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									ArrayList<MarkerTypeArrayList> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
								signal.countDown();
								;
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

	public void testArraysAsList() throws Throwable {
		assertTrue("Fails due to NoClassDefFoundError: GWTBridge", false);
		final CountDownLatch signal = new CountDownLatch(1);
		final List<MarkerTypeArraysAsList> expected = TestSetFactory
				.createArraysAsList();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoArraysAsList(expected,
						new AsyncCallback<List<MarkerTypeArraysAsList>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									List<MarkerTypeArraysAsList> result) {
								assertNotNull(result);
								assertEquals(expected, result);
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

	public void testBooleanArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Boolean[] expected = TestSetFactory.createBooleanArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Boolean[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Boolean[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

		assertTrue("Failed to Complete", signal.await(20, TimeUnit.SECONDS));
	}

	public void testByteArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Byte[] expected = TestSetFactory.createByteArray();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Byte[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Byte[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testCharArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Character[] expected = TestSetFactory.createCharArray();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Character[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Character[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testDoubleArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Double[] expected = TestSetFactory.createDoubleArray();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Double[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Double[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testFloatArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Float[] expected = TestSetFactory.createFloatArray();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Float[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Float[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testHashMap() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final HashMap<MarkerTypeHashMapKey, MarkerTypeHashMapValue> expected = TestSetFactory
				.createHashMap();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
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
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testHashSet() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final HashSet<MarkerTypeHashSet> expected = TestSetFactory
				.createHashSet();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected,
						new AsyncCallback<HashSet<MarkerTypeHashSet>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									HashSet<MarkerTypeHashSet> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testIntegerArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Integer[] expected = TestSetFactory.createIntegerArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Integer[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Integer[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testLinkedHashMap() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> expected = TestSetFactory
				.createLinkedHashMap();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
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
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testLinkedHashMapLRU() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> expected = TestSetFactory
				.createLRULinkedHashMap();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
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
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testLongArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Long[] expected = TestSetFactory.createLongArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Long[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Long[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveBooleanArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final boolean[] expected = TestSetFactory.createPrimitiveBooleanArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<boolean[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(boolean[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveByteArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final byte[] expected = TestSetFactory.createPrimitiveByteArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<byte[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(byte[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveCharArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final char[] expected = TestSetFactory.createPrimitiveCharArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<char[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(char[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveDoubleArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final double[] expected = TestSetFactory.createPrimitiveDoubleArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<double[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(double[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveFloatArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final float[] expected = TestSetFactory.createPrimitiveFloatArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<float[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(float[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveIntegerArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final int[] expected = TestSetFactory.createPrimitiveIntegerArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<int[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(int[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveLongArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final long[] expected = TestSetFactory.createPrimitiveLongArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<long[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(long[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testPrimitiveShortArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final short[] expected = TestSetFactory.createPrimitiveShortArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<short[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(short[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testShortArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Short[] expected = TestSetFactory.createShortArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Short[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Short[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testSqlDateArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final java.sql.Date[] expected = TestSetFactory.createSqlDateArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<java.sql.Date[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(java.sql.Date[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testSqlTimeArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Time[] expected = TestSetFactory.createSqlTimeArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Time[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Time[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testSqlTimestampArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Timestamp[] expected = TestSetFactory.createSqlTimestampArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Timestamp[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Timestamp[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testStringArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final String[] expected = TestSetFactory.createStringArray();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<String[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(String[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						signal.countDown();
						;
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

	public void testStringArrayArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final String[][] expected = new String[][] { new String[] { "hello" },
				new String[] { "bye" } };
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<String[][]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(String[][] result) {
						assertNotNull(result);
						signal.countDown();
						;
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

	public void testTreeMapOptTrue() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final TreeMap<String, MarkerTypeTreeMap> expected = TestSetFactory
				.createTreeMap(true);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						true,
						new AsyncCallback<TreeMap<String, MarkerTypeTreeMap>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TreeMap<String, MarkerTypeTreeMap> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testTreeMapOptFalse() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final TreeMap<String, MarkerTypeTreeMap> expected = TestSetFactory
				.createTreeMap(false);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						false,
						new AsyncCallback<TreeMap<String, MarkerTypeTreeMap>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TreeMap<String, MarkerTypeTreeMap> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testTreeSetOptTrue() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final TreeSet<MarkerTypeTreeSet> expected = TestSetFactory
				.createTreeSet(true);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, true,
						new AsyncCallback<TreeSet<MarkerTypeTreeSet>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TreeSet<MarkerTypeTreeSet> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testTreeSetOptFalse() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final TreeSet<MarkerTypeTreeSet> expected = TestSetFactory
				.createTreeSet(false);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, false,
						new AsyncCallback<TreeSet<MarkerTypeTreeSet>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TreeSet<MarkerTypeTreeSet> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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

	public void testVector() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final Vector<MarkerTypeVector> expected = TestSetFactory.createVector();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected,
						new AsyncCallback<Vector<MarkerTypeVector>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									Vector<MarkerTypeVector> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								signal.countDown();
								;
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
