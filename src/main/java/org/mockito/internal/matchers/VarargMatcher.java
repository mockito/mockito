/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

/**
 * Internal interface that informs Mockito that the matcher is intended to capture varargs.
 * This information is needed when mockito collects the arguments.
 */
public interface VarargMatcher extends Serializable {
}
