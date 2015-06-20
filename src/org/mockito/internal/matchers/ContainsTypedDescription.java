/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

public interface ContainsTypedDescription extends Serializable {
    String getTypedDescription();

    boolean typeMatches(Object object);
}