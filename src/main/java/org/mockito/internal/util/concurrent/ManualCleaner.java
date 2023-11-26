package org.mockito.internal.util.concurrent;

public class ManualCleaner<T> extends Cleaners<T> {
    @Override
    WeakConcurrentMap<Thread, T> createMap(DetachedThreadLocal detachedThreadLocal) {
        return new WeakConcurrentMap<Thread, T>(false) {
            @Override
            protected T defaultValue(Thread key) {
                return (T) detachedThreadLocal.initialValue(key);
            }
        };
    }
}
