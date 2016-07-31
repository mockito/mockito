package org.mockito.internal.framework;

import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

class ThreadSafeStubbingListener implements StubbingListener {

    private final StubbingListener delegate;
    private final Object lock = new Object();

    ThreadSafeStubbingListener(StubbingListener delegate) {
        this.delegate = delegate;
    }

    public void newStubbing(Invocation stubbing) {
        synchronized (lock) {
            delegate.newStubbing(stubbing);
        }
    }

    public void usedStubbing(Invocation stubbing, Invocation actual) {
        synchronized (lock) {
            delegate.usedStubbing(stubbing, actual);
        }
    }

    public void stubbingNotFound(Invocation actual) {
        synchronized (lock) {
            delegate.stubbingNotFound(actual);
        }
    }
}
