/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.hamcrest.Matcher;

import java.util.List;

@SuppressWarnings("unchecked")
public interface ArgumentMatcherStorage {

    HandyReturnValues reportMatcher(Matcher matcher);

    List<Matcher> pullMatchers();

    HandyReturnValues reportAnd();

    HandyReturnValues reportNot();

    HandyReturnValues reportOr();

    void validateState();

    void reset();

}