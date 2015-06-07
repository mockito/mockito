/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.List;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.LocalizedMatcher;

@SuppressWarnings("rawtypes")
public interface ArgumentMatcherStorage {

    HandyReturnValues reportMatcher(final Matcher matcher);

    List<LocalizedMatcher> pullLocalizedMatchers();

    HandyReturnValues reportAnd();

    HandyReturnValues reportNot();

    HandyReturnValues reportOr();

    void validateState();

    void reset();

}