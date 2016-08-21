/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;

import java.util.Collection;

/**
 * Provides mocking information.
 * For example, you can identify whether a particular object is either a mock or a spy.
 *
 * @since 1.9.5
 */
public interface MockingDetails {
    
    /**
     * Informs if the object is a mock. isMock() for null input returns false.
     * @return true if the object is a mock or a spy.
     *
     * @since 1.9.5
     */
    boolean isMock();

    /**
     * Informs if the object is a spy. isSpy() for null input returns false.
     * @return true if the object is a spy.
     *
     * @since 1.9.5
     */
    boolean isSpy();
    
    /**
     * All method invocations on this mock.
     * Can be empty - it means there were no interactions with the mock.
     * <p>
     * This method is useful for framework integrators and for certain edge cases.
     *
     * @since 1.10.0
     */
    Collection<Invocation> getInvocations();

    /**
     * Returns various mock settings provided when the mock was created, for example:
     *  mocked class, mock name (if any), any extra interfaces (if any), etc.
     * See also {@link MockCreationSettings}.
     * <p>
     * This method is useful for framework integrators and for certain edge cases.
     * <p>
     * If <code>null</code> or non-mock was passed to {@link Mockito#mockingDetails(Object)}
     * then this method will throw with an appropriate exception.
     * After all, non-mock objects do not have any mock creation settings.
     * @since 2.0.0
     */
    MockCreationSettings<?> getMockCreationSettings();
}
