package org.mockito.internal.progress;

import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingListener;

/**
 * Created by sfaber on 7/22/16.
 */
class NoOpStubbingListener implements StubbingListener {

    static StubbingListener INSTANCE = new NoOpStubbingListener();

    public void newStubbing(Invocation stubbing) {}
    public void usedStubbing(Invocation stubbing, Invocation actual) {}
}
