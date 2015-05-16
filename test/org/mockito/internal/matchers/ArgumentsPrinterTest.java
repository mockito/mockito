/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.*;
import org.junit.Test;
import org.mockito.internal.reporting.PrintSettings;
import org.mockitoutil.TestBase;

import static java.util.Collections.*;

@SuppressWarnings("unchecked")
public class ArgumentsPrinterTest extends TestBase {

    ArgumentsPrinter printer = new ArgumentsPrinter();

    private String toString(List<SelfDescribing> items) {
        return new StringDescription().appendList("(", ", ", ");", items).toString();
    }

    @Test
    public void should_get_arguments() {
        List<SelfDescribing> args = printer.describe(new Object[]{1l, 2}, EMPTY_LIST, new PrintSettings());
        assertEquals("(1, 2);", toString(args));
    }

    @Test
    public void should_describe_type_info_only_for_marked_arguments() {
        //when
        List<SelfDescribing> args = printer.describe(new Object[]{1l, 2}, EMPTY_LIST, PrintSettings.verboseMatchers(1));
        //then
        assertEquals("(1, (Integer) 2);", toString(args));
    }

    @Test
    public void should_get_verbose_arguments() {
        //when
        List<SelfDescribing> args = printer.describe(new Object[]{1l, 2}, EMPTY_LIST, PrintSettings.verboseMatchers(0, 1));
        //then
        assertEquals("((Long) 1, (Integer) 2);", toString(args));
    }

    @Test
    public void should_get_verbose_arguments_even_if_some_arguments_are_not_verbose() {
        //when
        List<SelfDescribing> args = printer.describe(new Object[]{1l, 2}, EMPTY_LIST, PrintSettings.verboseMatchers(0));
        //then
        assertEquals("((Long) 1, 2);", toString(args));
    }

    @Test
    public void use_mismatch_description_in_matcher_if_available() {
        List<Matcher> matchers = Collections.<Matcher>singletonList(new MatchesNothingDescribesMismatch());
        List<SelfDescribing> args = printer.describe(new Object[]{1}, matchers, new PrintSettings());
        assertEquals("(my description of 1);", toString(args));
    }

    @Test
    public void use_default_description_if_mismatch_description_is_not_available() {
        List<Matcher> matchers = Collections.<Matcher>singletonList(new MatchesNothing());
        List<SelfDescribing> args = printer.describe(new Object[]{1}, matchers, new PrintSettings());
        assertEquals("(1);", toString(args));
    }

    @Test
    public void use_default_description_if_no_matchers_are_given() {
        List<Matcher> matchers = EMPTY_LIST;
        List<SelfDescribing> args = printer.describe(new Object[]{1}, matchers, new PrintSettings());
        assertEquals("(1);", toString(args));
    }

    @Test
    public void some_matchers_describe_mismatch_some_dont() {
        List<Matcher> matchers = Arrays.<Matcher> asList(new MatchesNothingDescribesMismatch(), new MatchesNothing());
        List<SelfDescribing> args = printer.describe(new Object[]{1,1}, matchers, new PrintSettings());
        assertEquals("(my description of 1, 1);", toString(args));
    }

    @Test
    public void all_varargs_arguments_are_described_by_the_varargs_matcher() {
        List<Matcher> matchers = Collections.<Matcher>singletonList(new MatchesNothingVarargs());
        List<SelfDescribing> args = printer.describe(new Object[]{1, 2, 3}, matchers, new PrintSettings());
        assertEquals("(my description of 1, my description of 2, my description of 3);", toString(args));
    }

}

class MatchesNothing extends BaseMatcher<Object> {
    public boolean matches(Object o) {
        return false;
    }
    public void describeTo(Description description) {
        description.appendText("nothing");
    }
}

class MatchesNothingDescribesMismatch extends MatchesNothing {
    public void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText("my description of " + item);
    }
}

class MatchesNothingVarargs extends BaseMatcher<String[]> implements VarargMatcher {
    public boolean matches(Object o) {
        return false;
    }
    public void describeTo(Description description) {
        description.appendText("nothing");
    }
    public void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText("my description of " + item);
    }
}

