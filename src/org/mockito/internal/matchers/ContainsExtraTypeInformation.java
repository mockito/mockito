/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.SelfDescribing;

public interface ContainsExtraTypeInformation {
    SelfDescribing withExtraTypeInfo();

    boolean typeMatches(Object object);
}