package org.mockito.listeners;

import org.mockito.Incubating;
import org.mockito.invocation.Invocation;

/**
 * Listener that allows to listen on events related to stubbing.
 * Useful for advanced users, framework integrators.
 * We use it internally to detect unused stubbings with JUnit runners / rules.
 * For reference, see Mockito source code on how we use this listener.
 * <p>
 * Implementations are not required to be thread safe.
 * Your implementation will be wrapped with a thread safe delegator when you register this listener.
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

    /**
     * Method is called on a mock, but there is no stubbed behavior registered for this invocation.
     * Mockito will return default answer for given invocation, typically it means a default return value.
     * <p>
     * <strong>Warning:</strong> due to the nature of when() style of stubbing,
     * 'stubbingNotFound' is also triggered during standard stubbing with when().
     * Example:
     * <pre>
     *   when(mock.foo()).thenReturn(1); // <-- 'stubbingNotFound' is triggered for 'mock.foo()'
     * </pre>
     */
    void stubbingNotFound(Invocation actual);
}
