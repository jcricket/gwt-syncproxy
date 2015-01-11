/*
 * Copyright 2008 Google Inc.
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;

/**
 * Test transfer of value types over RPC.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class ValueTypesTest extends
AndroidGWTTestCase<ValueTypesTestServiceAsync> {

	private ValueTypesTestServiceAsync primitiveTypeTestService;

	public ValueTypesTest() throws InterruptedException {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				ValueTypesTest.this.primitiveTypeTestService = SyncProxy
						.create(ValueTypesTestService.class);
				((ServiceDefTarget) ValueTypesTest.this.primitiveTypeTestService)
				.setServiceEntryPoint(getModuleBaseURL() + "valuetypes");
				arg0[0].countDown();
				return null;
			}
		});
	}

	private void assertEcho(final BigDecimal value) {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(value, new AsyncCallback<BigDecimal>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(BigDecimal result) {
						assertEquals(value, result);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	private void assertEcho(final BigInteger value) {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(value, new AsyncCallback<BigInteger>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(BigInteger result) {
						assertEquals(value, result);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	private void assertEcho(final String value) {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(value, new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(String result) {
						assertEquals(value, result);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	private ValueTypesTestServiceAsync getServiceAsync() {
		if (this.primitiveTypeTestService == null) {
			this.primitiveTypeTestService = SyncProxy.newProxyInstance(
					ValueTypesTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "valuetypes", true);
		}
		return this.primitiveTypeTestService;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
	}

	public void testBigDecimal_base() {
		assertEcho(new BigDecimal("42"));
	}

	public void testBigDecimal_exponential() {
		assertEcho(new BigDecimal("0.00000000000000000001"));
	}

	public void testBigDecimal_negative() {
		assertEcho(new BigDecimal("-42"));
	}

	public void testBigDecimal_zero() {
		assertEcho(new BigDecimal("0.0"));
	}

	public void testBigInteger_base() {
		assertEcho(new BigInteger("42"));
	}

	public void testBigInteger_exponential() {
		assertEcho(new BigInteger("100000000000000000000"));
	}

	public void testBigInteger_negative() {
		assertEcho(new BigInteger("-42"));
	}

	public void testBigInteger_zero() {
		assertEcho(new BigInteger("0"));
	}

	public void testBoolean_FALSE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_FALSE(false, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Boolean result) {
						assertNotNull("Was null", result);
						assertFalse("Should have been false",
								result.booleanValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testBoolean_TRUE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_TRUE(true, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Boolean result) {
						assertNotNull(result);
						assertTrue(result.booleanValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testByte() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo((byte) (Byte.MAX_VALUE / (byte) 2),
						new AsyncCallback<Byte>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Byte result) {
						assertNotNull(result);
						assertEquals(Byte.MAX_VALUE / 2,
								result.byteValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testByte_MAX_VALUE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Byte.MAX_VALUE,
						new AsyncCallback<Byte>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Byte result) {
						assertNotNull(result);
						assertEquals(Byte.MAX_VALUE, result.byteValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testByte_MIN_VALUE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Byte.MIN_VALUE,
						new AsyncCallback<Byte>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Byte result) {
						assertNotNull(result);
						assertEquals(Byte.MIN_VALUE, result.byteValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testChar() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				final char value = (char) (Character.MAX_VALUE / (char) 2);
				service.echo(value, new AsyncCallback<Character>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Character result) {
						assertNotNull(result);
						assertEquals(value, result.charValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testChar_MAX_VALUE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Character.MAX_VALUE,
						new AsyncCallback<Character>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Character result) {
						assertNotNull(result);
						assertEquals(Character.MAX_VALUE,
								result.charValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testChar_MIN_VALUE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Character.MIN_VALUE,
						new AsyncCallback<Character>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Character result) {
						assertNotNull(result);
						assertEquals(Character.MIN_VALUE,
								result.charValue());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testDouble() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Double.MAX_VALUE / 2, new AsyncCallback<Double>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Double result) {
						assertNotNull(result);
						assertEquals(Double.MAX_VALUE / 2,
								result.doubleValue(), 0.0);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testDouble_MAX_VALUE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MAX_VALUE(Double.MAX_VALUE,
						new AsyncCallback<Double>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Double result) {
						assertNotNull(result);
						assertEquals(Double.MAX_VALUE,
								result.doubleValue(), 0.0);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testDouble_MIN_VALUE() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_MIN_VALUE(Double.MIN_VALUE,
						new AsyncCallback<Double>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Double result) {
						assertNotNull(result);
						assertEquals(Double.MIN_VALUE,
								result.doubleValue(), 0.0);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	/**
	 * Validate that NaNs (not-a-number, such as 0/0) propagate properly via
	 * RPC.
	 */
	 public void testDouble_NaN() {
		final ValueTypesTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Double.NaN, new AsyncCallback<Double>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Double result) {
						assertNotNull(result);
						assertTrue(Double.isNaN(result.doubleValue()));
						finishTest();
					}
				});
				return null;
			}
		});
	 }

	 /**
	  * Validate that negative infinity propagates properly via RPC.
	  */
	 public void testDouble_NegInfinity() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Double.NEGATIVE_INFINITY,
						 new AsyncCallback<Double>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Double result) {
						 assertNotNull(result);
						 double doubleValue = result.doubleValue();
						 assertTrue(Double.isInfinite(doubleValue)
								 && doubleValue < 0);
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 /**
	  * Validate that positive infinity propagates properly via RPC.
	  */
	 public void testDouble_PosInfinity() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Double.POSITIVE_INFINITY,
						 new AsyncCallback<Double>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Double result) {
						 assertNotNull(result);
						 double doubleValue = result.doubleValue();
						 assertTrue(Double.isInfinite(doubleValue)
								 && doubleValue > 0);
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testFloat() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Float.MAX_VALUE / 2, new AsyncCallback<Float>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Float result) {
						 assertNotNull(result);
						 assertEquals(Float.MAX_VALUE / 2, result.floatValue(),
								 0.0);
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testFloat_MAX_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MAX_VALUE(Float.MAX_VALUE,
						 new AsyncCallback<Float>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Float result) {
						 assertNotNull(result);
						 assertEquals(Float.MAX_VALUE,
								 result.floatValue(), 0.0);
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testFloat_MIN_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MIN_VALUE(Float.MIN_VALUE,
						 new AsyncCallback<Float>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Float result) {
						 assertNotNull(result);
						 assertEquals(Float.MIN_VALUE,
								 result.floatValue(), 0.0);
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 /**
	  * Validate that NaNs (not-a-number, such as 0/0) propagate properly via
	  * RPC.
	  */
	 public void testFloat_NaN() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Float.NaN, new AsyncCallback<Float>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Float result) {
						 assertNotNull(result);
						 assertTrue(Float.isNaN(result.floatValue()));
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 /**
	  * Validate that negative infinity propagates properly via RPC.
	  */
	 public void testFloat_NegInfinity() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Float.NEGATIVE_INFINITY,
						 new AsyncCallback<Float>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Float result) {
						 assertNotNull(result);
						 float floatValue = result.floatValue();
						 assertTrue(Float.isInfinite(floatValue)
								 && floatValue < 0);
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 /**
	  * Validate that positive infinity propagates properly via RPC.
	  */
	 public void testFloat_PosInfinity() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Float.POSITIVE_INFINITY,
						 new AsyncCallback<Float>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Float result) {
						 assertNotNull(result);
						 float floatValue = result.floatValue();
						 assertTrue(Float.isInfinite(floatValue)
								 && floatValue > 0);
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testInteger() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Integer.MAX_VALUE / 2,
						 new AsyncCallback<Integer>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Integer result) {
						 assertNotNull(result);
						 assertEquals(Integer.MAX_VALUE / 2,
								 result.intValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testInteger_MAX_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MAX_VALUE(Integer.MAX_VALUE,
						 new AsyncCallback<Integer>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Integer result) {
						 assertNotNull(result);
						 assertEquals(Integer.MAX_VALUE,
								 result.intValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testInteger_MIN_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MIN_VALUE(Integer.MIN_VALUE,
						 new AsyncCallback<Integer>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Integer result) {
						 assertNotNull(result);
						 assertEquals(Integer.MIN_VALUE,
								 result.intValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testLong() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(Long.MAX_VALUE / 2, new AsyncCallback<Long>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Long result) {
						 assertNotNull(result);
						 long expected = Long.MAX_VALUE / 2;
						 assertEquals(expected, result.longValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testLong_MAX_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MAX_VALUE(Long.MAX_VALUE,
						 new AsyncCallback<Long>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Long result) {
						 assertNotNull(result);
						 assertEquals(Long.MAX_VALUE, result.longValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testLong_MIN_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MIN_VALUE(Long.MIN_VALUE,
						 new AsyncCallback<Long>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Long result) {
						 assertNotNull(result);
						 assertEquals(Long.MIN_VALUE, result.longValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testShort() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 final short value = (short) (Short.MAX_VALUE / 2);
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(value, new AsyncCallback<Short>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Short result) {
						 assertNotNull(result);
						 assertEquals(value, result.shortValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testShort_MAX_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MAX_VALUE(Short.MAX_VALUE,
						 new AsyncCallback<Short>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Short result) {
						 assertNotNull(result);
						 assertEquals(Short.MAX_VALUE,
								 result.shortValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testShort_MIN_VALUE() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo_MIN_VALUE(Short.MIN_VALUE,
						 new AsyncCallback<Short>() {

					 @Override
					 public void onFailure(Throwable caught) {
						 TestSetValidator.rethrowException(caught);
					 }

					 @Override
					 public void onSuccess(Short result) {
						 assertNotNull(result);
						 assertEquals(Short.MIN_VALUE,
								 result.shortValue());
						 finishTest();
					 }
				 });
				 return null;
			 }
		 });
	 }

	 public void testString() {
		 assertEcho("test");
	 }

	 public void testString_empty() {
		 assertEcho("");
	 }

	 public void testString_over64KB() {
		 fail("ComparisonFailure");
		 // Test a string over 64KB of a-z characters repeated.
		 String testString = "";
		 int totalChars = 0xFFFF + 0xFF;
		 for (int i = 0; i < totalChars; i++) {
			 testString += (char) ('a' + i % 26);
		 }
		 assertEcho(testString);
	 }

	 public void testString_over64KBWithUnicode() {
		 fail("ComparisonFailure");
		 // Test a string over64KB string that requires unicode escaping.
		 String testString = "";
		 int totalChars = 0xFFFF + 0xFF;
		 for (int i = 0; i < totalChars; i += 2) {
			 testString += '\u2011';
			 testString += (char) 0x08;
		 }
		 assertEcho(testString);
	 }

	 public void testVoidParameterizedType() {
		 final ValueTypesTestServiceAsync service = getServiceAsync();
		 delayTestFinishForRpc();
		 setTask(new AsyncTask<Void, Void, Void>() {

			 @Override
			 protected Void doInBackground(Void... arg0) {
				 service.echo(
						 new SerializableGenericWrapperType<Void>(),
						 new AsyncCallback<SerializableGenericWrapperType<Void>>() {

							 @Override
							 public void onFailure(Throwable caught) {
								 TestSetValidator.rethrowException(caught);
							 }

							 @Override
							 public void onSuccess(
									 SerializableGenericWrapperType<Void> result) {
								 assertNotNull(result);
								 assertNull(result.getValue());
								 finishTest();
							 }
						 });
				 return null;
			 }
		 });
	 }
}
