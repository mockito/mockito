/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.Incubating;
import org.mockito.NotExtensible;

/**
 * Although this class is a part of public API, please don't provide your own implementations.
 * This marker interface is only used to avoid leaking internal API via public API.
 * Mockito depends on specific internal implementation of this interface.
 * If you need to provide your own implementation please reach out to us.
 * Use our issue tracker to open a ticket and open a discussion.
 *
 * @since 2.10.0
 */
@NotExtensible
@Incubating
public interface InvocationContainer {}
