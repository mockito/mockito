/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.LocalizedMatcher;

@SuppressWarnings("unchecked")
public interface ArgumentMatcherStorage {

    void reportMatcher(ArgumentMatcher<?> matcher);

    List<LocalizedMatcher> pullLocalizedMatchers();

    void reportAnd();

    void reportNot();

    void reportOr();

    void validateState();

    void reset();
}
