/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.Incubating;

import java.io.Serializable;

/**
 * Mockito handler of an invocation on a mock. This is a core part of the API, the heart of Mockito.
 * See also the {@link org.mockito.plugins.MockMaker}.
 * <p>
 * This api is work in progress, hence a marker interface.
 */
@Incubating
public interface MockitoInvocationHandler extends Serializable {}
