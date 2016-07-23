package org.mockito.listeners;

import org.mockito.Incubating;
import org.mockito.invocation.Invocation;

/**
 * Created by sfaber on 5/2/16.
 */
@Incubating
public interface StubbingListener {

    void newStubbing(Invocation stubbing);

    /**
     * TODO make it thread safe so that users don't have to worry
     * TODO add javadoc
     */
    void usedStubbing(Invocation stubbing, Invocation actual);
}
