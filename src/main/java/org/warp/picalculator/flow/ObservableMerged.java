package org.warp.picalculator.flow;

public class ObservableMerged<T> extends Observable<T> {
	private Observable<T> originalObservableA;
	private Observable<T> originalObservableB;
	private volatile boolean initialized = false;
	private Disposable mapDisposableA;
	private Disposable mapDisposableB;

	public ObservableMerged(Observable<T> originalObservableA, Observable<T> originalObservableB) {
		super();
		this.originalObservableA = originalObservableA;
		this.originalObservableB = originalObservableB;
	}

	private void initialize() {
		this.mapDisposableA = originalObservableA.subscribe((t) -> {
			for (Subscriber<? super T> sub : this.subscribers) {
				sub.onNext(t);
			} ;
		}, (e) -> {
			for (Subscriber<? super T> sub : this.subscribers) {
				sub.onError(e);
			} ;
		}, () -> {
			for (Subscriber<? super T> sub : this.subscribers) {
				sub.onComplete();
			} ;
		});
		this.mapDisposableB = originalObservableB.subscribe((t) -> {
			for (Subscriber<? super T> sub : this.subscribers) {
				sub.onNext(t);
			} ;
		}, (e) -> {
			for (Subscriber<? super T> sub : this.subscribers) {
				sub.onError(e);
			} ;
		}, () -> {
			for (Subscriber<? super T> sub : this.subscribers) {
				sub.onComplete();
			} ;
		});
	}

	private void chechInitialized() {
		if (!initialized) {
			initialized = true;
			initialize();
		}
	}

	@Override
	public Disposable subscribe(Subscriber<? super T> sub) {
		Disposable disp = super.subscribe(sub);
		chechInitialized();
		return disp;
	}

	@Override
	public void onDisposed(Subscriber<? super T> sub) {
		super.onDisposed(sub);
		this.mapDisposableA.dispose();
		this.mapDisposableB.dispose();
	}
}
