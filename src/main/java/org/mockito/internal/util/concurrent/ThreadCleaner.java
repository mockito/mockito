package org.mockito.internal.util.concurrent;

public class ThreadCleaner<T> extends Cleaners<T> {
    @Override
    WeakConcurrentMap<Thread, T> createMap(DetachedThreadLocal detachedThreadLocal) {
        return new WeakConcurrentMap<Thread, T>(true) {
            @Override
            protected T defaultValue(Thread key) {
                return (T) detachedThreadLocal.initialValue(key);
            }
        };
    }
}
