/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

/**
 * @deprecated - please use {@link MockingDetails#printInvocations()}.
 */
@Deprecated
public interface MockitoDebugger {

    /**
     * @deprecated - please use {@link MockingDetails#printInvocations()}.
     */
    @Deprecated
    String printInvocations(Object ... mocks);
}