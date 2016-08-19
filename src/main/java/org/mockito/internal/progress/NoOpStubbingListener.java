package org.mockito.internal.progress;

import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

class NoOpStubbingListener implements StubbingListener {

    static StubbingListener INSTANCE = new NoOpStubbingListener();

    public void newStubbing(Invocation stubbing) {}
    public void usedStubbing(Invocation stubbing, Invocation actual) {}
    public void stubbingNotFound(Invocation actual) {}
}
