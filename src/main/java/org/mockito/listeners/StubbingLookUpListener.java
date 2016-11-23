package org.mockito.listeners;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

/**
 * TODO strict, implements MockObjectListener marker?
 *
 * Listens to attempts to look up stubbing answer for given mocks
 * <p>
 *
 * How does it work?
 * When method is called on the mock object, Mockito looks for any answer (stubbing) declared on that mock.
 * If the stubbed answer is found, that answer is invoked (value returned, thrown exception, etc.).
 * If the answer is not found (e.g. that invocation was not stubbed on the mock), mock's default answer is used.
 * This listener implementation is notified when Mockito looked up an answer for invocation on a mock.
 */
public interface StubbingLookUpListener {

    /**
     * Called by the framework when Mockito looked up an answer for invocation on a mock.
     *
     * @param invocation the invocation on the mock
     * @param stubbingFound - can be null - it indicates that the invocation was not stubbed.
     */
    void onStubbingLookUp(Invocation invocation, MatchableInvocation stubbingFound);

}
