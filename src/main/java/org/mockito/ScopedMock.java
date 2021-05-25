/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

/**
 * Represents a mock with a thread-local explicit scope. Scoped mocks must be closed by the entity
 * that activates the scoped mock.
 */
@Incubating
public interface ScopedMock extends AutoCloseable {

    /**
     * Checks if this mock is closed.
     *
     * @return {@code true} if this mock is closed.
     */
    boolean isClosed();

    /**
     * Closes this scoped mock and throws an exception if already closed.
     */
    @Override
    void close();

    /**
     * Releases this scoped mock and is non-operational if already released.
     */
    void closeOnDemand();
}
