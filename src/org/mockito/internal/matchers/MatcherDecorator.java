/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public interface MatcherDecorator extends Serializable {
    Matcher getActualMatcher();
}
