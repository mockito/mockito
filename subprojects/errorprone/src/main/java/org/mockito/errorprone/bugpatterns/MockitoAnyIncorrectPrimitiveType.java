/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import static com.google.errorprone.BugPattern.LinkType.CUSTOM;
import static com.google.errorprone.BugPattern.SeverityLevel.ERROR;
import static com.google.errorprone.matchers.method.MethodMatchers.staticMethod;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.matchers.Matcher;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.code.Type;
import java.util.regex.Pattern;

/**
 * Matches on usage of {@code Mockito.argThat(Matcher)} with a matcher that does not extend
 * ArgumentMatcher.
 */
@AutoService(BugChecker.class)
@BugPattern(
    name = "MockitoAnyIncorrectPrimitiveType",
    summary = "Matcher mismatch: incorrect use of any() or anyX() to match a primitive argument",
    severity = ERROR,
    linkType = CUSTOM,
    link = "site.mockito.org/usage/bugpattern/MockitoAnyIncorrectPrimitiveType",
    explanation =
        "Mockito relies on Java type checking to ensure that parameter matchers are"
            + " type safe but there are some discrepancies between what the Java type checker"
            + " allows and what Mockito expects. e.g. Java will allow anyInt() to be used as a"
            + " matcher for a long parameter because an int can be widened to a long. This"
            + " checker highlights those incorrect usages and suggests replacements. In Mockito"
            + " 1.x this was not really an issue because the anyX() methods did not do runtime"
            + " type checking of the arguments but in Mockito 2.x they do."
            + " Java will also allow any() to be used within a primitive but any() returns null and"
            + " the compiler wraps that call in unboxing which leads to a NPE.")
public class MockitoAnyIncorrectPrimitiveType extends AbstractMockitoAnyForPrimitiveType {

  // Match against any() or any of the any<x>() methods.
  private static final Pattern METHOD_NAME_PATTERN =
      Pattern.compile("any(Boolean|Byte|Char|Double|Float|Int|Long|Short)?");

  private static final String[] CLASS_NAMES = {
    "org.mockito.ArgumentMatchers", "org.mockito.Mockito", "org.mockito.Matchers"
  };

  private static final Matcher<ExpressionTree> METHOD_MATCHER =
      staticMethod().onClassAny(CLASS_NAMES).withNameMatching(METHOD_NAME_PATTERN).withParameters();

  @Override
  protected Matcher<? super MethodInvocationTree> matcher() {
    return METHOD_MATCHER;
  }

  @Override
  protected String formatMessage(
      String expectedTypeAsString, Type matcherType, String replacementName) {
    return String.format(
        "Matcher mismatch: expected matcher for parameter of type '%s',"
            + " found matcher for parameter of type '%s'",
        expectedTypeAsString, matcherType);
  }
}
