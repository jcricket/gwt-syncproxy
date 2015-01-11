/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.rpc;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArrayList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArraysAsList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEmptyKey;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEmptyList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEmptySet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEmptyValue;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEnum;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEnumMapValue;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashMapKey;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashMapValue;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashSet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeIdentityHashMapKey;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeIdentityHashMapValue;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeLinkedHashMapKey;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeLinkedHashMapValue;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeLinkedHashSet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeLinkedList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeSingleton;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeTreeMap;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeTreeSet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeVector;
import com.google.gwt.user.client.rpc.core.java.util.LinkedHashMap_CustomFieldSerializer;

/**
 * Tests collections across RPC.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0. Also setup Default TimeZone for
 * this test because local Java was using local TimeZone instead of UTC.
 */
public class CollectionsTest extends
AndroidGWTTestCase<CollectionsTestServiceAsync> {

	private CollectionsTestServiceAsync collectionsTestService;

	public CollectionsTest() throws InterruptedException {
		super(MainActivity.class);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				CollectionsTest.this.collectionsTestService = SyncProxy
						.create(CollectionsTestService.class);
				arg0[0].countDown();
				return null;
			}
		});
	}

	/**
	 * This tests sending payloads that must be segmented to avoid problems with
	 * IE6/7. This test is disabled since it sometimes fails on Safari, possibly
	 * due to SSW.
	 */
	public void disabledTestVeryLargeArray() {
		CollectionsTestServiceAsync service = getServiceAsync();
		final int[] expected = TestSetFactory.createVeryLargeArray();
		delayTestFinishForRpc();
		service.echo(expected, new AsyncCallback<int[]>() {
			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(int[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}
		});
	}

	private CollectionsTestServiceAsync getServiceAsync() {
		return this.collectionsTestService;
	}

	public void testArrayList() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(TestSetFactory.createArrayList(),
						new AsyncCallback<ArrayList<MarkerTypeArrayList>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									ArrayList<MarkerTypeArrayList> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testArrayListVoid() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoArrayListVoid(TestSetFactory.createArrayListVoid(),
						new AsyncCallback<ArrayList<Void>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(ArrayList<Void> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidArrayListVoid(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testArraysAsList() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final List<MarkerTypeArraysAsList> expected = TestSetFactory
				.createArraysAsList();

		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoArraysAsList(expected,
						new AsyncCallback<List<MarkerTypeArraysAsList>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									List<MarkerTypeArraysAsList> result) {
								assertNotNull(result);
								assertEquals(expected, result);
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testBooleanArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Boolean[] expected = TestSetFactory.createBooleanArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Boolean[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Boolean[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Boolean(false)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testByteArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Byte[] expected = TestSetFactory.createByteArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Byte[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Byte[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Byte((byte) 0)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testCharArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Character[] expected = TestSetFactory.createCharArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Character[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Character[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Character('0')));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testDateArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Date[] expected = TestSetFactory.createDateArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Date[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Date[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Date()));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testDoubleArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Double[] expected = TestSetFactory.createDoubleArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Double[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Double[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Double(0.0)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testEmptyEnumMap() {
		fail("EnumMaps not Supported on Andorid");
		final CollectionsTestServiceAsync service = getServiceAsync();
		final EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> expected = TestSetFactory
				.createEmptyEnumMap();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoEmptyEnumMap(
						expected,
						new AsyncCallback<EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testEmptyList() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(TestSetFactory.createEmptyList(),
						new AsyncCallback<List<MarkerTypeEmptyList>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									List<MarkerTypeEmptyList> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testEmptyMap() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TestSetFactory.createEmptyMap(),
						new AsyncCallback<Map<MarkerTypeEmptyKey, MarkerTypeEmptyValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									Map<MarkerTypeEmptyKey, MarkerTypeEmptyValue> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testEmptySet() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(TestSetFactory.createEmptySet(),
						new AsyncCallback<Set<MarkerTypeEmptySet>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Set<MarkerTypeEmptySet> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testEnumArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Enum<?>[] expected = TestSetFactory.createEnumArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Enum<?>[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Enum<?>[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										TestSetFactory.MarkerTypeEnum.C));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testEnumMap() {
		fail("EnumMaps not Supported on Andorid");
		final CollectionsTestServiceAsync service = getServiceAsync();
		final EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> expected = TestSetFactory
				.createEnumMap();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						new AsyncCallback<EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testEnumMapEnumKey() {
		fail("EnumMaps not Supported on Andorid");
		final CollectionsTestServiceAsync service = getServiceAsync();
		final EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> expected = TestSetFactory
				.createEnumMapEnumKey();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoEnumKey(
						expected,
						new AsyncCallback<EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValidEnumKey(
										expected, result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testFloatArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Float[] expected = TestSetFactory.createFloatArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Float[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Float[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Float(0.0)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testHashMap() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final HashMap<MarkerTypeHashMapKey, MarkerTypeHashMapValue> expected = TestSetFactory
				.createHashMap();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						new AsyncCallback<HashMap<MarkerTypeHashMapKey, MarkerTypeHashMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									HashMap<MarkerTypeHashMapKey, MarkerTypeHashMapValue> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testHashSet() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final HashSet<MarkerTypeHashSet> expected = TestSetFactory
				.createHashSet();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected,
						new AsyncCallback<HashSet<MarkerTypeHashSet>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									HashSet<MarkerTypeHashSet> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testIdentityHashMap() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final IdentityHashMap<MarkerTypeIdentityHashMapKey, MarkerTypeIdentityHashMapValue> expected = TestSetFactory
				.createIdentityHashMap();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						new AsyncCallback<IdentityHashMap<MarkerTypeIdentityHashMapKey, MarkerTypeIdentityHashMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									IdentityHashMap<MarkerTypeIdentityHashMapKey, MarkerTypeIdentityHashMapValue> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testIdentityHashMapEnumKey() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final IdentityHashMap<MarkerTypeEnum, MarkerTypeIdentityHashMapValue> expected = TestSetFactory
				.createIdentityHashMapEnumKey();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoEnumKey(
						expected,
						new AsyncCallback<IdentityHashMap<MarkerTypeEnum, MarkerTypeIdentityHashMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									IdentityHashMap<MarkerTypeEnum, MarkerTypeIdentityHashMapValue> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValidEnumKey(
										expected, result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testIntegerArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Integer[] expected = TestSetFactory.createIntegerArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Integer[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Integer[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Integer(0)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testLinkedHashMap() {
		final CollectionsTestServiceAsync service = getServiceAsync();

		final LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> expected = TestSetFactory
				.createLinkedHashMap();
		assertFalse(LinkedHashMap_CustomFieldSerializer
				.getAccessOrderNoReflection(expected));

		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						new AsyncCallback<LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> result) {
								assertNotNull(result);
								expected.get("SerializableSet");
								result.get("SerializableSet");
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testLinkedHashMapLRU() {
		final CollectionsTestServiceAsync service = getServiceAsync();

		final LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> expected = TestSetFactory
				.createLRULinkedHashMap();
		assertTrue(LinkedHashMap_CustomFieldSerializer
				.getAccessOrderNoReflection(expected));

		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						new AsyncCallback<LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									LinkedHashMap<MarkerTypeLinkedHashMapKey, MarkerTypeLinkedHashMapValue> actual) {
								assertNotNull(actual);
								expected.get("SerializableSet");
								actual.get("SerializableSet");
								assertTrue(TestSetValidator.isValid(expected,
										actual));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testLinkedHashSet() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final LinkedHashSet<MarkerTypeLinkedHashSet> expected = TestSetFactory
				.createLinkedHashSet();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						expected,
						new AsyncCallback<LinkedHashSet<MarkerTypeLinkedHashSet>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									LinkedHashSet<MarkerTypeLinkedHashSet> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testLinkedList() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final LinkedList<MarkerTypeLinkedList> expected = TestSetFactory
				.createLinkedList();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected,
						new AsyncCallback<LinkedList<MarkerTypeLinkedList>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									LinkedList<MarkerTypeLinkedList> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testLongArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Long[] expected = TestSetFactory.createLongArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Long[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Long[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Long(0L)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveBooleanArray() {
		final boolean[] expected = TestSetFactory.createPrimitiveBooleanArray();
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<boolean[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(boolean[] result) {
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveByteArray() {
		final byte[] expected = TestSetFactory.createPrimitiveByteArray();
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<byte[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(byte[] result) {
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveCharArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final char[] expected = TestSetFactory.createPrimitiveCharArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<char[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(char[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveDoubleArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final double[] expected = TestSetFactory.createPrimitiveDoubleArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<double[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(double[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveFloatArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final float[] expected = TestSetFactory.createPrimitiveFloatArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<float[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(float[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveIntegerArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final int[] expected = TestSetFactory.createPrimitiveIntegerArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<int[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(int[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveLongArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final long[] expected = TestSetFactory.createPrimitiveLongArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<long[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(long[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testPrimitiveShortArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final short[] expected = TestSetFactory.createPrimitiveShortArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<short[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(short[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testShortArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Short[] expected = TestSetFactory.createShortArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Short[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Short[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Short((short) 0)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testSingletonList() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoSingletonList(TestSetFactory.createSingletonList(),
						new AsyncCallback<List<MarkerTypeSingleton>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									List<MarkerTypeSingleton> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidSingletonList(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testSqlDateArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final java.sql.Date[] expected = TestSetFactory.createSqlDateArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<java.sql.Date[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(java.sql.Date[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new java.sql.Date(0L)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testSqlTimeArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Time[] expected = TestSetFactory.createSqlTimeArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Time[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Time[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Time(0L)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testSqlTimestampArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Timestamp[] expected = TestSetFactory.createSqlTimestampArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<Timestamp[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Timestamp[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new Timestamp(0L)));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testStringArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final String[] expected = TestSetFactory.createStringArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<String[]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(String[] result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.equals(expected, result));

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new String("")));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testStringArrayArray() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final String[][] expected = new String[][] { new String[] { "hello" },
				new String[] { "bye" } };
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<String[][]>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(String[][] result) {
						assertNotNull(result);

						// ensure result preserves meta-data for array store
						// type
						// checking
						assertTrue(TestSetValidator
								.checkObjectArrayElementAssignment(result, 0,
										new String[4]));

						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testTreeMap() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				for (boolean option : new boolean[] { true, false }) {
					final TreeMap<String, MarkerTypeTreeMap> expected = TestSetFactory
							.createTreeMap(option);
					service.echo(
							expected,
							option,
							new AsyncCallback<TreeMap<String, MarkerTypeTreeMap>>() {
								@Override
								public void onFailure(Throwable caught) {
									TestSetValidator.rethrowException(caught);
								}

								@Override
								public void onSuccess(
										TreeMap<String, MarkerTypeTreeMap> result) {
									assertNotNull(result);
									assertTrue(TestSetValidator.isValid(
											expected, result));
									finishTest();
								}
							});
				}
				return null;
			}
		});
	}

	public void testTreeSet() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				for (boolean option : new boolean[] { true, false }) {
					final TreeSet<MarkerTypeTreeSet> expected = TestSetFactory
							.createTreeSet(option);
					service.echo(expected, option,
							new AsyncCallback<TreeSet<MarkerTypeTreeSet>>() {
								@Override
								public void onFailure(Throwable caught) {
									TestSetValidator.rethrowException(caught);
								}

								@Override
								public void onSuccess(
										TreeSet<MarkerTypeTreeSet> result) {
									assertNotNull(result);
									assertTrue(TestSetValidator.isValid(
											expected, result));
									finishTest();
								}
							});
				}
				return null;
			}
		});
	}

	public void testVector() {
		final CollectionsTestServiceAsync service = getServiceAsync();
		final Vector<MarkerTypeVector> expected = TestSetFactory.createVector();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected,
						new AsyncCallback<Vector<MarkerTypeVector>>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									Vector<MarkerTypeVector> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(expected,
										result));
								finishTest();
							}
						});
				return null;
			}
		});
	}
}
