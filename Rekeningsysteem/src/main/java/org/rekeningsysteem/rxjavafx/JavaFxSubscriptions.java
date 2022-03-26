package org.rekeningsysteem.rxjavafx;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.application.Platform;

public enum JavaFxSubscriptions {
	; // no instance

	/**
	 * Create an Subscription that always runs <code>unsubscribe</code> in the event dispatch
	 * thread.
	 *
	 * @param unsubscribe the action to be performed in the ui thread at un-subscription
	 * @return an Subscription that always runs <code>unsubscribe</code> in the event dispatch
	 *         thread.
	 */
	public static Disposable unsubscribeInEventDispatchThread(Runnable unsubscribe) {
		return Disposable.fromRunnable(() -> {
			if (Platform.isFxApplicationThread()) {
				unsubscribe.run();
			}
			else {
				final Scheduler.Worker inner = JavaFxScheduler.getInstance().createWorker();
				inner.schedule(() -> {
					unsubscribe.run();
					inner.dispose();
				});
			}
		});
	}
}
