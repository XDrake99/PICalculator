package it.cavallium.warppi.util;

import it.cavallium.warppi.WarpPI;

import java.util.concurrent.atomic.AtomicLong;

public class Timer {
	public Timer(int intervalMillis, Runnable action) {
		var thread = new Thread(() -> {
			try {
				Thread.sleep(intervalMillis);

				AtomicLong lostTime = new AtomicLong();
				while (!Thread.interrupted()) {
					var time1 = System.currentTimeMillis();
					action.run();
					var time2 = System.currentTimeMillis();
					var deltaTime = time2 - time1 + lostTime.get();
					if (intervalMillis - deltaTime > 0) {
						Thread.sleep(intervalMillis);
					} else {
						lostTime.set(deltaTime - intervalMillis);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		WarpPI.getPlatform().setThreadName(thread, "Timer");
		WarpPI.getPlatform().setThreadDaemon(thread, true);
		thread.start();
	}
}
