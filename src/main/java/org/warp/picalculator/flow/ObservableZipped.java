package org.warp.picalculator.flow;

import org.apache.commons.lang3.tuple.Pair;

public class ObservableZipped<T, U> extends Observable<Pair<T, U>> {
	private volatile boolean initialized = false;
	private Observable<T> a;
	private Observable<U> b;
	private Disposable disposableA;
	private Disposable disposableB;
	private volatile T lastA;
	private volatile U lastB;
	private volatile boolean didA;
	private volatile boolean didB;

	public ObservableZipped(Observable<T> a, Observable<U> b) {
		super();
		this.a = a;
		this.b = b;
	}

	private void initialize() {
		this.disposableA = a.subscribe((t) -> {
			lastA = t;
			didA = true;
			receivedNext();
		}, (e) -> {
			for (Subscriber<? super Pair<T, U>> sub : this.subscribers) {
				sub.onError(e);
			} ;
		}, () -> {
			for (Subscriber<? super Pair<T, U>> sub : this.subscribers) {
				sub.onComplete();
			} ;
		});
		this.disposableB = b.subscribe((t) -> {
			lastB = t;
			didB = true;
			receivedNext();
		}, (e) -> {
			for (Subscriber<? super Pair<T, U>> sub : this.subscribers) {
				sub.onError(e);
			} ;
		}, () -> {
			for (Subscriber<? super Pair<T, U>> sub : this.subscribers) {
				sub.onComplete();
			} ;
		});
	}

	private void receivedNext() {
		if (didA && didB) {
			didA = false;
			didB = false;
			for (Subscriber<? super Pair<T, U>> sub : this.subscribers) {
				sub.onNext(Pair.of(lastA, lastB));
			} ;
		}
	}

	private void chechInitialized() {
		if (!initialized) {
			initialized = true;
			initialize();
		}
	}

	@Override
	public Disposable subscribe(Subscriber<? super Pair<T, U>> sub) {
		Disposable disp = super.subscribe(sub);
		chechInitialized();
		return disp;
	}

	@Override
	public void onDisposed(Subscriber<? super Pair<T, U>> sub) {
		super.onDisposed(sub);
		this.disposableA.dispose();
		this.disposableB.dispose();
	}
}
