/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.invocation.InvocationOnMock;

public interface ReturnValues {

    Object valueFor(InvocationOnMock invocation);

}