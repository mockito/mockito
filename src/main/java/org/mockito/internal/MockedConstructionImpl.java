/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.Collections;
import java.util.List;

import org.mockito.MockedConstruction;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.invocation.Location;
import org.mockito.plugins.MockMaker;

import static org.mockito.internal.util.StringUtil.*;

public final class MockedConstructionImpl<T> implements MockedConstruction<T> {

    private final MockMaker.ConstructionMockControl<T> control;

    private boolean closed;

    private final Location location = new LocationImpl();

    protected MockedConstructionImpl(MockMaker.ConstructionMockControl<T> control) {
        this.control = control;
    }

    @Override
    public List<T> constructed() {
        return Collections.unmodifiableList(control.getMocks());
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        assertNotClosed();

        closed = true;
        control.disable();
    }

    @Override
    public void closeOnDemand() {
        if (!closed) {
            close();
        }
    }

    private void assertNotClosed() {
        if (closed) {
            throw new MockitoException(
                    join(
                            "The static mock created at",
                            location.toString(),
                            "is already resolved and cannot longer be used"));
        }
    }
}
