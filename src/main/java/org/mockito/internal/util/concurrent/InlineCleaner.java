package org.mockito.internal.util.concurrent;

public class InlineCleaner<T> extends Cleaners<T> {
    @Override
    WeakConcurrentMap<Thread, T> createMap(DetachedThreadLocal detachedThreadLocal) {
        return new WeakConcurrentMap.WithInlinedExpunction<Thread, T>() {
            @Override
            protected T defaultValue(Thread key) {
                return (T) detachedThreadLocal.initialValue(key);
            }
        };
    }
}
