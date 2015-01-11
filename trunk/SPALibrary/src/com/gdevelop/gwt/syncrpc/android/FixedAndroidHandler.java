/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdevelop.gwt.syncrpc.android;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import android.util.Log;

/**
 * Solution configured based on
 * http://stackoverflow.com/questions/4561345/how-to
 * -configure-java-util-logging-on-android Taken from:
 * http://android.git.kernel.
 * org/?p=platform/frameworks/base.git;a=blob_plain;f=core
 * /java/com/android/internal
 * /logging/AndroidHandler.java;hb=c2ad241504fcaa12d4579d3b0b4038d1ca8d08c9
 *
 * @since 0.5
 */
public class FixedAndroidHandler extends Handler {

	/**
	 * Converts a {@link java.util.logging.Logger} logging level into an Android
	 * one.
	 *
	 * @param level
	 *            The {@link java.util.logging.Logger} logging level.
	 *
	 * @return The resulting Android logging level.
	 */
	static int getAndroidLevel(Level level) {
		int value = level.intValue();
		if (value >= 1000) { // SEVERE
			return Log.ERROR;
		} else if (value >= 900) { // WARNING
			return Log.WARN;
		} else if (value >= 800) { // INFO
			return Log.INFO;
		} else if (value >= 700) { // CONFIG
			return Log.DEBUG;
		} else {
			return Log.VERBOSE;
		}
	}

	/**
	 * @return the disableConsoleHandler
	 */
	public static boolean isDisableConsoleHandler() {
		return disableConsoleHandler;
	}

	/**
	 * @param disableConsoleHandler
	 *            the disableConsoleHandler to set
	 */
	public static void setDisableConsoleHandler(boolean disableConsoleHandler) {
		FixedAndroidHandler.disableConsoleHandler = disableConsoleHandler;
	}

	/**
	 * Ensures that only 1 FixedAndroidHandler exists per logger and by default
	 * removes any installed {@link ConsoleHandler}. Use
	 * {@link #setDisableConsoleHandler(boolean)} to change that option
	 *
	 * @param logger
	 */
	public static void setupLogger(Logger logger) {
		Handler[] handlers = logger.getHandlers();
		boolean newHandler = true;
		for (Handler handler : handlers) {
			if (handler instanceof FixedAndroidHandler) {
				newHandler = false;
			}
			if (handler instanceof ConsoleHandler && disableConsoleHandler) {
				logger.removeHandler(handler);
			}
		}
		if (newHandler) {
			logger.addHandler(new FixedAndroidHandler());
		}
	}

	protected static boolean disableConsoleHandler = true;

	/**
	 * Holds the formatter for all Android log handlers.
	 */
	private static final Formatter THE_FORMATTER = new Formatter() {
		@Override
		public String format(LogRecord r) {
			Throwable thrown = r.getThrown();
			if (thrown != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				sw.write(r.getMessage());
				sw.write("\n");
				thrown.printStackTrace(pw);
				pw.flush();
				return sw.toString();
			} else {
				return r.getMessage();
			}
		}
	};

	/**
	 * Constructs a new instance of the Android log handler.
	 */
	public FixedAndroidHandler() {
		setFormatter(THE_FORMATTER);
	}

	@Override
	public void close() {
		// No need to close, but must implement abstract method.
	}

	@Override
	public void flush() {
		// No need to flush, but must implement abstract method.
	}

	@Override
	public void publish(LogRecord record) {
		try {
			int level = getAndroidLevel(record.getLevel());
			String tag = record.getLoggerName();

			if (tag == null) {
				// Anonymous logger.
				tag = "null";
			} else {
				// Tags must be <= 23 characters.
				int length = tag.length();
				if (length > 23) {
					// Most loggers use the full class name. Try dropping the
					// package.
					int lastPeriod = tag.lastIndexOf(".");
					if (length - lastPeriod - 1 <= 23) {
						tag = tag.substring(lastPeriod + 1);
					} else {
						// Use last 23 chars.
						tag = tag.substring(tag.length() - 23);
					}
				}
			}

			/*
			 * ##################################################################
			 * ##########################
			 * 
			 * Instead of using the perfectly fine java.util.logging API for
			 * setting the loggable levels, this call relies on a totally
			 * obscure "local.prop" file which you have to place on your device.
			 * By default, if you do not have that file and if you do not
			 * execute some magic "setprop" commands on your device, only
			 * INFO/WARN/ERROR is loggable. So whatever you do with
			 * java.util.logging.Logger.setLevel(...) doesn't have any effect.
			 * The debug messages might arrive here but they are dropped because
			 * you _also_ have to set the Android internal logging level with
			 * the aforementioned magic switches.
			 * 
			 * Also, consider that you have to understand how a JUL logger name
			 * is mapped to the "tag" of the Android log. Basically, the whole
			 * cutting and cropping procedure above is what you have to memorize
			 * if you want to log with JUL and configure Android for debug
			 * output.
			 * 
			 * I actually admire the pure evil of this setup, even Mr. Ceki can
			 * learn something!
			 * 
			 * Commenting out these lines makes it all work as expected:
			 * 
			 * if (!Log.isLoggable(tag, level)) { return; }
			 * 
			 * ##################################################################
			 * #############################
			 */

			String message = getFormatter().format(record);
			Log.println(level, tag, message);
		} catch (RuntimeException e) {
			Log.e("AndroidHandler", "Error logging message.", e);
		}
	}

}