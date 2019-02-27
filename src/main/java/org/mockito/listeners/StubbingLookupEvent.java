/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;

/**
 * Represent an information about the looked up stubbing
 *
 * @since 2.24.6
 */
public interface StubbingLookupEvent {

    /**
     * @return The invocation that causes stubbing lookup
     * @since 2.24.6
     */
    Invocation getInvocation();

    /**
     * @return Looked up stubbing. It can be <code>null</code>, which indicates that the invocation was not stubbed
     * @since 2.24.6
     */
    Stubbing getStubbingFound();

    /**
     * @return All stubbings declared on the mock object that we are invoking
     * @since 2.24.6
     */
    Collection<Stubbing> getAllStubbings();

    /**
     * @return Settings of the mock object that we are invoking
     * @since 2.24.6
     */
    MockCreationSettings getMockSettings();
}
