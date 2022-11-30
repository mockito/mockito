/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

/**
 * Internal interface that informs Mockito that the matcher is intended to capture varargs.
 * This information is needed when mockito collects the arguments.
 *
 * @deprecated use of this interface is deprecated as the behaviour it promotes has limitations.
 * It is not recommended for new implementations to implement this method.
 *
 * <p>Instead, matchers should implement the {@link org.mockito.ArgumentMatcher#type()} method.
 * If this method returns the same raw type as a vararg parameter, then Mockito will treat the
 * matcher as matching the entire vararg array parameter, otherwise it will be treated as matching a single element.
 * For an example, see {@link org.mockito.ArgumentMatchers#isNull(Class)}.
 */
@Deprecated
public interface VarargMatcher extends Serializable {}
