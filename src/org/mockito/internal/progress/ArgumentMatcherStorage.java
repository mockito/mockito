package org.mockito.internal.progress;

import java.util.List;

import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public interface ArgumentMatcherStorage {

    EmptyReturnValues reportMatcher(Matcher matcher);

    List<Matcher> pullMatchers();

    EmptyReturnValues reportAnd();

    EmptyReturnValues reportNot();

    EmptyReturnValues reportOr();

    void validateState();

    void reset();

}