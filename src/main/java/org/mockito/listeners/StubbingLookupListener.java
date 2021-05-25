/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.MockSettings;
import org.mockito.mock.MockCreationSettings;

/**
 * When a method is called on a mock object Mockito looks up any stubbings recorded on that mock.
 * This listener gets notified on stubbing lookup.
 * Register listener via {@link MockSettings#stubbingLookupListeners(StubbingLookupListener...)}.
 * This API is used by Mockito to implement {@link org.mockito.exceptions.misusing.PotentialStubbingProblem}
 * (part of Mockito {@link org.mockito.quality.Strictness}).
 * <p>
 * Details: When method is called on the mock object, Mockito looks for any answer (stubbing) declared on that mock.
 * If the stubbed answer is found, that answer is then invoked (value returned, thrown exception, etc.).
 * If the answer is not found (e.g. that invocation was not stubbed on the mock), mock's default answer is used.
 * This listener implementation is notified when Mockito attempts to find an answer for invocation on a mock.
 * <p>
 * The listeners can be accessed via {@link MockCreationSettings#getStubbingLookupListeners()}.
 *
 * @since 2.24.6
 */
public interface StubbingLookupListener {

    /**
     * Called by the framework when Mockito looked up an answer for invocation on a mock.
     * For details, see {@link StubbingLookupListener}.
     *
     * @param stubbingLookupEvent - Information about the looked up stubbing
     * @since 2.24.6
     */
    void onStubbingLookup(StubbingLookupEvent stubbingLookupEvent);
}
