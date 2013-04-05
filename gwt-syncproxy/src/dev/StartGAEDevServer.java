package dev;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.google.appengine.tools.development.gwt.AppEngineLauncher;
import com.google.gwt.core.ext.ServletContainerLauncher;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;

/**
 * Start GAE Dev Server
 */
public class StartGAEDevServer {
	private static class AutoFlushPrintWriter extends PrintWriter {
		AutoFlushPrintWriter(OutputStream out) {
			super(out);
		}

		@Override
		public void println(String x) {
			super.println(x);
			super.flush();
		}
	}

	public static void main(String[] args) throws Exception {
		ServletContainerLauncher scl = AppEngineLauncher.class.newInstance();
		// ServletContainer server =
		scl.start(new PrintWriterTreeLogger(
				new AutoFlushPrintWriter(System.out)), 8888, new File("."));

		try {
			throw new Exception();
		} catch (Exception e) {
			if (e.getStackTrace().length != 1) {
				return;
			}
		}

		while (true) {
			Thread.sleep(300000);

			Runtime rt = Runtime.getRuntime();
			long free1 = rt.freeMemory() / 1024 / 1024;

			int N = 1;
			for (int i = 0; i < N; i++) {
				System.gc();
				System.runFinalization();
			}

			long free = rt.freeMemory() / 1024 / 1024;
			long total = rt.totalMemory() / 1024 / 1024;
			System.out.println("Mem info: Total/Free/Used (in MB): " + total
					+ "/" + free + "/" + (total - free) + ". Mem collected: "
					+ (free - free1) + " MB.");
		}
	}
}
