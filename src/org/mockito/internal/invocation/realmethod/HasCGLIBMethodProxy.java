/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.realmethod;

import org.mockito.internal.creation.cglib.MockitoMethodProxy;

import java.io.Serializable;

public interface HasCGLIBMethodProxy extends Serializable {

    MockitoMethodProxy getMethodProxy();
}
