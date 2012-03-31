/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.api;

import org.mockito.internal.invocation.InvocationImpl;

public interface InOrderContext {

    boolean isVerified(InvocationImpl invocation);

    void markVerified(InvocationImpl i);

}
