package org.rekeningsysteem.rxjavafx;

import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public final class JavaFxScheduler extends Scheduler {

	private static final JavaFxScheduler INSTANCE = new JavaFxScheduler();

	private JavaFxScheduler() {
	}

	public static JavaFxScheduler getInstance() {
		return INSTANCE;
	}

	private static void assertThatTheDelayIsValidForTheJavaFxTimer(long delay) {
		if (delay < 0 || delay > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(String.format(
					"The JavaFx timer only accepts non-negative delays up to %d milliseconds.",
					Integer.MAX_VALUE));
		}
	}

	@Override
	public Worker createWorker() {
		return new InnerJavaFxScheduler();
	}

	private static class InnerJavaFxScheduler extends Worker {

		private final CompositeSubscription innerSubscription = new CompositeSubscription();

		@Override
		public void unsubscribe() {
			this.innerSubscription.unsubscribe();
		}

		@Override
		public boolean isUnsubscribed() {
			return this.innerSubscription.isUnsubscribed();
		}

		@Override
		public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
			long delay = unit.toMillis(delayTime);
			assertThatTheDelayIsValidForTheJavaFxTimer(delay);
			BooleanSubscription s = BooleanSubscription.create();

			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delay), event -> {
				if (this.innerSubscription.isUnsubscribed() || s.isUnsubscribed()) {
					return;
				}
				action.call();
				this.innerSubscription.remove(s);
			}));

			timeline.setCycleCount(1);
			timeline.play();

			this.innerSubscription.add(s);

			// wrap for returning so it also removes it from the 'innerSubscription'
			return Subscriptions.create(() -> {
				timeline.stop();
				s.unsubscribe();
				InnerJavaFxScheduler.this.innerSubscription.remove(s);
			});
		}

		@Override
		public Subscription schedule(final Action0 action) {
			final BooleanSubscription s = BooleanSubscription.create();
			Platform.runLater(() -> {
				if (InnerJavaFxScheduler.this.innerSubscription.isUnsubscribed()
						|| s.isUnsubscribed()) {
					return;
				}
				action.call();
				InnerJavaFxScheduler.this.innerSubscription.remove(s);
			});

			this.innerSubscription.add(s);
			// wrap for returning so it also removes it from the 'innerSubscription'
			return Subscriptions.create(() -> {
				s.unsubscribe();
				InnerJavaFxScheduler.this.innerSubscription.remove(s);
			});
		}
	}
}
