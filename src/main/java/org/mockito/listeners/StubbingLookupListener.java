/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.MockSettings;

/**
 * This listener can be notified of looking up stubbing answer for a given mock.
 *
 * For this to happen, it must be registered using {@link MockSettings#stubbingLookupListeners(StubbingLookupListener...)}.
 *
 * TODO x use case, mutability
 *
 * <p>
 * How does it work?
 * When method is called on the mock object, Mockito looks for any answer (stubbing) declared on that mock.
 * If the stubbed answer is found, that answer is invoked (value returned, thrown exception, etc.).
 * If the answer is not found (e.g. that invocation was not stubbed on the mock), mock's default answer is used.
 * This listener implementation is notified when Mockito looked up an answer for invocation on a mock.
 * <p>
 */
public interface StubbingLookupListener {

    /**
     * Called by the framework when Mockito looked up an answer for invocation on a mock.
     *
     * @param stubbingLookupEvent - Information about the looked up stubbing
     */
    void onStubbingLookup(StubbingLookupEvent stubbingLookupEvent);
}
