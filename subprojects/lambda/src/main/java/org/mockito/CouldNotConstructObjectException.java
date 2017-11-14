/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.base.MockitoException;

import static org.mockito.internal.util.StringUtil.join;

class CouldNotConstructObjectException extends MockitoException {
    CouldNotConstructObjectException(LambdaArgumentMatcher<?> matcher) {
        super(join("Unable to construct an object for matcher ", matcher,
            "This Exception was thrown as a result of a nullness check when the method was invoked.",
            "If you are developing in a language with non-nullness by default,",
            "use concrete matchers such as `any(Object.class)` or `eq(object)`."));
    }
}
