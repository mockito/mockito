package org.mockito.listeners;

import org.mockito.invocation.Invocation;

/**
 * Created by sfaber on 5/2/16.
 */
public interface StubbingListener {

    void newStubbing(Invocation stubbing);
    void usedStubbing(Invocation stubbing, Invocation actual);
}
