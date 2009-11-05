/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import java.io.Serializable;

import org.mockito.internal.creation.MockitoMethodProxy;

public class CGLIBHacker implements Serializable {

    private static final long serialVersionUID = -4389233991416356668L;

    public void setMockitoNamingPolicy(MockitoMethodProxy methodProxy) {
        methodProxy.setNamingPolicyField(MockitoNamingPolicy.INSTANCE);
    }
}