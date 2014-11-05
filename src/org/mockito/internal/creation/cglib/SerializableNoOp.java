/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.NoOp;

import java.io.Serializable;

/**
 * Offer a Serializable implementation of the NoOp CGLIB callback.
 */
class SerializableNoOp implements NoOp, Serializable {

    private static final long serialVersionUID = 7434976328690189159L;
    public static final Callback SERIALIZABLE_INSTANCE = new SerializableNoOp();

}