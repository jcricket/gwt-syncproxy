package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.ValueTypesTestService;
import com.google.gwt.user.client.rpc.ValueTypesTestServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class ValueTypesTest extends GWTTestCase {
	private static ValueTypesTestServiceAsync service;

	public ValueTypesTest() {
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	@Override
	public void gwtSetUp() {
		service = GWT.create(ValueTypesTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/valuetypes");
	}

	public void testBoolean_FALSE() {
		service.echo_FALSE(false, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Boolean result) {
				assertNotNull(result);
				assertFalse(result.booleanValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testBoolean_TRUE() {
		service.echo_TRUE(true, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Boolean result) {
				assertNotNull(result);
				assertTrue(result.booleanValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testByte() {
		service.echo((byte) (Byte.MAX_VALUE / (byte) 2),
				new AsyncCallback<Byte>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Byte result) {
						assertNotNull(result);
						assertEquals(Byte.MAX_VALUE / 2, result.byteValue());
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testByte_MAX_VALUE() {
		service.echo_MAX_VALUE(Byte.MAX_VALUE, new AsyncCallback<Byte>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Byte result) {
				assertNotNull(result);
				assertEquals(Byte.MAX_VALUE, result.byteValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testByte_MIN_VALUE() {
		service.echo_MIN_VALUE(Byte.MIN_VALUE, new AsyncCallback<Byte>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Byte result) {
				assertNotNull(result);
				assertEquals(Byte.MIN_VALUE, result.byteValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testChar() {
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testChar_MAX_VALUE() {
		service.echo_MAX_VALUE(Character.MAX_VALUE,
				new AsyncCallback<Character>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Character result) {
						assertNotNull(result);
						assertEquals(Character.MAX_VALUE, result.charValue());
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testChar_MIN_VALUE() {
		service.echo_MIN_VALUE(Character.MIN_VALUE,
				new AsyncCallback<Character>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Character result) {
						assertNotNull(result);
						assertEquals(Character.MIN_VALUE, result.charValue());
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testDouble() {
		service.echo(Double.MAX_VALUE / 2, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Double result) {
				assertNotNull(result);
				assertEquals(Double.MAX_VALUE / 2, result.doubleValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testDouble_MAX_VALUE() {
		service.echo_MAX_VALUE(Double.MAX_VALUE, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Double result) {
				assertNotNull(result);
				assertEquals(Double.MAX_VALUE, result.doubleValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testDouble_MIN_VALUE() {
		service.echo_MIN_VALUE(Double.MIN_VALUE, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Double result) {
				assertNotNull(result);
				assertEquals(Double.MIN_VALUE, result.doubleValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * Validate that NaNs (not-a-number, such as 0/0) propagate properly via
	 * RPC.
	 */
	public void testDouble_NaN() {
		service.echo(Double.NaN, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Double result) {
				assertNotNull(result);
				assertTrue(Double.isNaN(result.doubleValue()));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * Validate that negative infinity propagates properly via RPC.
	 */
	public void testDouble_NegInfinity() {
		service.echo(Double.NEGATIVE_INFINITY, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Double result) {
				assertNotNull(result);
				double doubleValue = result.doubleValue();
				assertTrue(Double.isInfinite(doubleValue) && doubleValue < 0);
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * Validate that positive infinity propagates properly via RPC.
	 */
	public void testDouble_PosInfinity() {
		service.echo(Double.POSITIVE_INFINITY, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Double result) {
				assertNotNull(result);
				double doubleValue = result.doubleValue();
				assertTrue(Double.isInfinite(doubleValue) && doubleValue > 0);
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testFloat() {
		service.echo(Float.MAX_VALUE / 2, new AsyncCallback<Float>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Float result) {
				assertNotNull(result);
				assertEquals(Float.MAX_VALUE / 2, result.floatValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testFloat_MAX_VALUE() {
		service.echo_MAX_VALUE(Float.MAX_VALUE, new AsyncCallback<Float>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Float result) {
				assertNotNull(result);
				assertEquals(Float.MAX_VALUE, result.floatValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testFloat_MIN_VALUE() {
		service.echo_MIN_VALUE(Float.MIN_VALUE, new AsyncCallback<Float>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Float result) {
				assertNotNull(result);
				assertEquals(Float.MIN_VALUE, result.floatValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * Validate that NaNs (not-a-number, such as 0/0) propagate properly via
	 * RPC.
	 */
	public void testFloat_NaN() {
		service.echo(Float.NaN, new AsyncCallback<Float>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Float result) {
				assertNotNull(result);
				assertTrue(Float.isNaN(result.floatValue()));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * Validate that negative infinity propagates properly via RPC.
	 */
	public void testFloat_NegInfinity() {
		service.echo(Float.NEGATIVE_INFINITY, new AsyncCallback<Float>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Float result) {
				assertNotNull(result);
				float floatValue = result.floatValue();
				assertTrue(Float.isInfinite(floatValue) && floatValue < 0);
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * Validate that positive infinity propagates properly via RPC.
	 */
	public void testFloat_PosInfinity() {
		service.echo(Float.POSITIVE_INFINITY, new AsyncCallback<Float>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Float result) {
				assertNotNull(result);
				float floatValue = result.floatValue();
				assertTrue(Float.isInfinite(floatValue) && floatValue > 0);
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testInteger() {
		service.echo(Integer.MAX_VALUE / 2, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer result) {
				assertNotNull(result);
				assertEquals(Integer.MAX_VALUE / 2, result.intValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testInteger_MAX_VALUE() {
		service.echo(Integer.MAX_VALUE, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer result) {
				assertNotNull(result);
				assertEquals(Integer.MAX_VALUE, result.intValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testInteger_MIN_VALUE() {
		service.echo(Integer.MIN_VALUE, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer result) {
				assertNotNull(result);
				assertEquals(Integer.MIN_VALUE, result.intValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testLong() {
		service.echo(Long.MAX_VALUE / 2, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Long result) {
				assertNotNull(result);
				assertEquals(Long.MAX_VALUE / 2, result.longValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testLong_MAX_VALUE() {
		service.echo_MAX_VALUE(Long.MAX_VALUE, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Long result) {
				assertNotNull(result);
				assertEquals(Long.MAX_VALUE, result.longValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testLong_MIN_VALUE() {
		service.echo_MIN_VALUE(Long.MIN_VALUE, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Long result) {
				assertNotNull(result);
				assertEquals(Long.MIN_VALUE, result.longValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testShort() {
		service.echo((short) (Short.MAX_VALUE / 2), new AsyncCallback<Short>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Short result) {
				assertNotNull(result);
				assertEquals(Short.MAX_VALUE / 2, result.shortValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testShort_MAX_VALUE() {
		service.echo_MAX_VALUE(Short.MAX_VALUE, new AsyncCallback<Short>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Short result) {
				assertNotNull(result);
				assertEquals(Short.MAX_VALUE, result.shortValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void testShort_MIN_VALUE() {
		service.echo_MIN_VALUE(Short.MIN_VALUE, new AsyncCallback<Short>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Short result) {
				assertNotNull(result);
				assertEquals(Short.MIN_VALUE, result.shortValue());
				finishTest();
			}

		});
		delayTestFinish(2000);
	}
}
