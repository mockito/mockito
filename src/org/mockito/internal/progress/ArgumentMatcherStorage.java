package org.mockito.internal.progress;

import java.util.List;

import org.hamcrest.Matcher;

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