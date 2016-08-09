/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.invocation.Invocation;

import java.util.Collection;
import java.util.Set;

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
     *
     * @since 1.10.0
     */
    Collection<Invocation> getInvocations();

    /**
     * Returns the type that is mocked. It is the type originally passed to
     * the <code>{@link Mockito#mock(Class)}</code> or <code>{@link Mockito#spy(Class)}</code> function,
     * or the type referenced by a Mockito annotation.
     *
     * @return The mocked type
     * @since 2.0.0
     */
    Class<?> getMockedType();

    /**
     * Returns the extra-interfaces of the mock. The interfaces that were configured at the mock creation
     * with the <code>{@link MockSettings#extraInterfaces(Class[])}</code>.
     *
     * @return The extra-interfaces
     * @since 2.0.0
     */
    Set<Class<?>> getExtraInterfaces();
}
