/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

/**
 * Listens to attempts to look up stubbing answer for given mocks. This class is internal for now.
 * <p>
 * How does it work?
 * When method is called on the mock object, Mockito looks for any answer (stubbing) declared on that mock.
 * If the stubbed answer is found, that answer is invoked (value returned, thrown exception, etc.).
 * If the answer is not found (e.g. that invocation was not stubbed on the mock), mock's default answer is used.
 * This listener implementation is notified when Mockito looked up an answer for invocation on a mock.
 * <p>
 * If we make this interface a part of public API (and we should):
 *  - make the implementation unified with InvocationListener (for example: common parent, marker interface MockObjectListener
 *  single method for adding listeners so long they inherit from the parent)
 *  - make the error handling strict
 * so that Mockito provides decent message when listener fails due to poor implementation.
 */
public interface StubbingLookupListener {

    /**
     * Called by the framework when Mockito looked up an answer for invocation on a mock.
     *
     * TODO when making this public, we should have an event object instead of 2 arguments in the listener.
     *
     * @param invocation the invocation on the mock
     * @param stubbingFound - can be null - it indicates that the invocation was not stubbed.
     */
    void onStubbingLookup(Invocation invocation, MatchableInvocation stubbingFound);

}
