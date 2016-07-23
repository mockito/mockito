package org.mockito.listeners;

import org.mockito.Incubating;
import org.mockito.invocation.Invocation;

/**
 * Listener that allows to listen on events related to stubbing.
 * Useful for advanced users, framework integrators.
 * We use it internally to detect unused stubbings with JUnit runners / rules.
 *
 * @since 2.*
 */
@Incubating
public interface StubbingListener {

    /**
     * New stubbing event, some method on a mock is being stubbed now.
     * This typically happens in the test code, before the tested behavior is invoked
     * (e.g. in the 'given' section of the test, every clean test has 3 sections: 'given', 'when' and 'then').
     */
    void newStubbing(Invocation stubbing);

    /**
     * New stubbing 'used' event, some existing stubbing on a mock is being realized now.
     * This typically happens in the production code, when tested behavior is invoked
     * (e.g. in the 'when' section of the test, every clean test has 3 sections: 'given', 'when' and 'then').
     */
    void usedStubbing(Invocation stubbing, Invocation actual);
}
