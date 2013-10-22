package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.SPATests;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ValueTypesTestServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class ValueTypesTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	ValueTypesTestServiceAsync service;

	public ValueTypesTest() throws Throwable {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (ValueTypesTestServiceAsync) SyncProxy
						.newProxyInstance(ValueTypesTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"valuetypes", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(SPATests.WAIT_TIME_MEDIUM, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testBoolean_FALSE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_FALSE(false, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Boolean result) {
						assertNotNull(result);
						assertFalse(result.booleanValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testBoolean_TRUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_TRUE(true, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Boolean result) {
						assertNotNull(result);
						assertTrue(result.booleanValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testByte() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo((byte) (Byte.MAX_VALUE / (byte) 2),
						new AsyncCallback<Byte>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Byte result) {
								assertNotNull(result);
								assertEquals(Byte.MAX_VALUE / 2,
										result.byteValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testByte_MAX_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Byte.MAX_VALUE,
						new AsyncCallback<Byte>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Byte result) {
								assertNotNull(result);
								assertEquals(Byte.MAX_VALUE, result.byteValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testByte_MIN_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Byte.MIN_VALUE,
						new AsyncCallback<Byte>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Byte result) {
								assertNotNull(result);
								assertEquals(Byte.MIN_VALUE, result.byteValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testChar() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo((char) (Character.MAX_VALUE / (char) 2),
						new AsyncCallback<Character>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Character result) {
								assertNotNull(result);
								assertEquals(Character.MAX_VALUE / 2,
										result.charValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testChar_MAX_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Character.MAX_VALUE,
						new AsyncCallback<Character>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Character result) {
								assertNotNull(result);
								assertEquals(Character.MAX_VALUE,
										result.charValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testChar_MIN_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Character.MIN_VALUE,
						new AsyncCallback<Character>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Character result) {
								assertNotNull(result);
								assertEquals(Character.MIN_VALUE,
										result.charValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testDouble() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Double.MAX_VALUE / 2, new AsyncCallback<Double>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Double result) {
						assertNotNull(result);
						assertEquals(Double.MAX_VALUE / 2, result.doubleValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testDouble_MAX_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Double.MAX_VALUE,
						new AsyncCallback<Double>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Double result) {
								assertNotNull(result);
								assertEquals(Double.MAX_VALUE,
										result.doubleValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testDouble_MIN_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Double.MIN_VALUE,
						new AsyncCallback<Double>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Double result) {
								assertNotNull(result);
								assertEquals(Double.MIN_VALUE,
										result.doubleValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	/**
	 * Validate that NaNs (not-a-number, such as 0/0) propagate properly via
	 * RPC.
	 * 
	 * @throws Throwable
	 */
	public void testDouble_NaN() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Double.NaN, new AsyncCallback<Double>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Double result) {
						assertNotNull(result);
						assertTrue(Double.isNaN(result.doubleValue()));
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	/**
	 * Validate that negative infinity propagates properly via RPC.
	 * 
	 * @throws Throwable
	 */
	public void testDouble_NegInfinity() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Double.NEGATIVE_INFINITY,
						new AsyncCallback<Double>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Double result) {
								assertNotNull(result);
								double doubleValue = result.doubleValue();
								assertTrue(Double.isInfinite(doubleValue)
										&& doubleValue < 0);
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	/**
	 * Validate that positive infinity propagates properly via RPC.
	 * 
	 * @throws Throwable
	 */
	public void testDouble_PosInfinity() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Double.POSITIVE_INFINITY,
						new AsyncCallback<Double>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Double result) {
								assertNotNull(result);
								double doubleValue = result.doubleValue();
								assertTrue(Double.isInfinite(doubleValue)
										&& doubleValue > 0);
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testFloat() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Float.MAX_VALUE / 2, new AsyncCallback<Float>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Float result) {
						assertNotNull(result);
						assertEquals(Float.MAX_VALUE / 2, result.floatValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testFloat_MAX_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Float.MAX_VALUE,
						new AsyncCallback<Float>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Float result) {
								assertNotNull(result);
								assertEquals(Float.MAX_VALUE,
										result.floatValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testFloat_MIN_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Float.MIN_VALUE,
						new AsyncCallback<Float>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Float result) {
								assertNotNull(result);
								assertEquals(Float.MIN_VALUE,
										result.floatValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	/**
	 * Validate that NaNs (not-a-number, such as 0/0) propagate properly via
	 * RPC.
	 * 
	 * @throws Throwable
	 */
	public void testFloat_NaN() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Float.NaN, new AsyncCallback<Float>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Float result) {
						assertNotNull(result);
						assertTrue(Float.isNaN(result.floatValue()));
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	/**
	 * Validate that negative infinity propagates properly via RPC.
	 * 
	 * @throws Throwable
	 */
	public void testFloat_NegInfinity() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Float.NEGATIVE_INFINITY,
						new AsyncCallback<Float>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Float result) {
								assertNotNull(result);
								float floatValue = result.floatValue();
								assertTrue(Float.isInfinite(floatValue)
										&& floatValue < 0);
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	/**
	 * Validate that positive infinity propagates properly via RPC.
	 * 
	 * @throws Throwable
	 */
	public void testFloat_PosInfinity() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Float.POSITIVE_INFINITY,
						new AsyncCallback<Float>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Float result) {
								assertNotNull(result);
								float floatValue = result.floatValue();
								assertTrue(Float.isInfinite(floatValue)
										&& floatValue > 0);
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testInteger() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Integer.MAX_VALUE / 2,
						new AsyncCallback<Integer>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Integer result) {
								assertNotNull(result);
								assertEquals(Integer.MAX_VALUE / 2,
										result.intValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testInteger_MAX_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Integer.MAX_VALUE, new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Integer result) {
						assertNotNull(result);
						assertEquals(Integer.MAX_VALUE, result.intValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testInteger_MIN_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Integer.MIN_VALUE, new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Integer result) {
						assertNotNull(result);
						assertEquals(Integer.MIN_VALUE, result.intValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testLong() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Long.MAX_VALUE / 2, new AsyncCallback<Long>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Long result) {
						assertNotNull(result);
						assertEquals(Long.MAX_VALUE / 2, result.longValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testLong_MAX_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Long.MAX_VALUE,
						new AsyncCallback<Long>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Long result) {
								assertNotNull(result);
								assertEquals(Long.MAX_VALUE, result.longValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testLong_MIN_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Long.MIN_VALUE,
						new AsyncCallback<Long>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Long result) {
								assertNotNull(result);
								assertEquals(Long.MIN_VALUE, result.longValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testShort() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo((short) (Short.MAX_VALUE / 2),
						new AsyncCallback<Short>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Short result) {
								assertNotNull(result);
								assertEquals(Short.MAX_VALUE / 2,
										result.shortValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testShort_MAX_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Short.MAX_VALUE,
						new AsyncCallback<Short>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Short result) {
								assertNotNull(result);
								assertEquals(Short.MAX_VALUE,
										result.shortValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	public void testShort_MIN_VALUE() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Short.MIN_VALUE,
						new AsyncCallback<Short>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Short result) {
								assertNotNull(result);
								assertEquals(Short.MIN_VALUE,
										result.shortValue());
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}
}
