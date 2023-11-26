package org.mockito.internal.util.concurrent;
public abstract class Cleaners<T> {
    abstract WeakConcurrentMap<Thread, T> createMap(DetachedThreadLocal detachedThreadLocal);
}
