/**
 * Dec 24, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spaapptest.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.android.FixedAndroidHandler;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;

/**
 * @author Preethum
 * @since 0.4.4
 *
 */
public abstract class AndroidGWTTestCase<AsyncServiceIntf> extends
		ActivityInstrumentationTestCase2<MainActivity> {

	CountDownLatch signal;
	private AsyncTask<Void, Void, Void> task;
	private AsyncTask<CountDownLatch, Void, Void> serviceInitTask;

	boolean serviceTaskAvailable = false;
	private int overrideTimeout = -1;

	/**
	 * @param activityClass
	 */
	public AndroidGWTTestCase(Class<MainActivity> activityClass) {
		super(activityClass);

	}

	protected void delayTestFinishForRpc() {
		this.signal = new CountDownLatch(1);
	}

	protected void finishTest() {
		if (this.signal != null) {
			this.signal.countDown();
		}
	}

	public String getModuleBaseURL() {
		return getInstrumentation().getContext().getString(
				R.string.test_server_url);
	}

	/**
	 * Override to perform any additional teardown before the test is fully torn
	 * down
	 */
	public void gwtTearDown() {

	}

	protected void setRpcTimeout(int timeOut) {
		this.overrideTimeout = timeOut;
	}

	public void setServiceInitTask(
			AsyncTask<CountDownLatch, Void, Void> serviceInitTask) {
		this.serviceInitTask = serviceInitTask;
	}

	protected void setTask(AsyncTask<Void, Void, Void> task) {
		this.task = task;
	}

	/**
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.overrideTimeout = -1;
		for (Class<?> clazz : SyncProxy.getLoggerClasses()) {
			FixedAndroidHandler.setupLogger(Logger.getLogger(clazz.getName()));
		}
		this.task = null;
		SyncProxy.setBaseURL(getModuleBaseURL());
		// SyncProxy.setLoggingLevel(Level.FINER);
		if (!this.serviceTaskAvailable && this.serviceInitTask != null) {
			CountDownLatch initSignal = new CountDownLatch(1);
			this.serviceInitTask.execute(initSignal);
			assertTrue("Failed to init service", initSignal.await(
					SPATests.WAIT_TIME_SERVICE_INIT, TimeUnit.SECONDS));
			this.serviceTaskAvailable = true;
		}
	}

	/**
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// Execute the async task on the UI thread! THIS IS KEY!
		try {
			if (this.task != null) {
				runTestOnUiThread(new Runnable() {
					@Override
					public void run() {
						AndroidGWTTestCase.this.task.execute();
					}
				});
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (this.signal != null
				&& !this.signal.await(
						this.overrideTimeout > 0 ? this.overrideTimeout
								: SPATests.WAIT_TIME_MEDIUM, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to complete");
		}
		gwtTearDown();
		SyncProxy.suppressRelativePathWarning(false);
		super.tearDown();
	}
}
