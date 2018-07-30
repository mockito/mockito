/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

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
     * @param stubbingLookupEvent - Information about the looked up stubbing
     */
    void onStubbingLookup(StubbingLookupEvent stubbingLookupEvent);
}
