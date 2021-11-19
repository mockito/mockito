/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import static com.google.errorprone.BugPattern.LinkType.CUSTOM;
import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.matchers.Matcher;
import com.google.errorprone.matchers.Matchers;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.code.Type;

/**
 * Matches on usage of {@code Mockito.argThat(Matcher)} with a matcher that does not extend
 * ArgumentMatcher.
 */
@AutoService(BugChecker.class)
@BugPattern(
    name = "MockitoAnyClassWithPrimitiveType",
    summary = "Matcher inconsistency: use of any(Class) to match a primitive argument",
    severity = WARNING,
    linkType = CUSTOM,
    link = "site.mockito.org/usage/bugpattern/MockitoAnyClassWithPrimitiveType",
    explanation =
        "Mockito relies on Java type checking to ensure that parameter matchers are"
            + " type safe but there are some discrepancies between what the Java type checker"
            + " allows and what Mockito expects. e.g. Java will allow anyInt() to be used as a"
            + " matcher for a long parameter because an int can be widened to a long. This"
            + " checker highlights those incorrect usages and suggests replacements. In Mockito"
            + " 1.x this was not really an issue because the anyX() methods did not do runtime"
            + " type checking of the arguments but in Mockito 2.x they do.")
public class MockitoAnyClassWithPrimitiveType extends AbstractMockitoAnyForPrimitiveType {

  private static final String[] CLASS_NAMES = {
    "org.mockito.Mockito", "org.mockito.ArgumentMatchers", "org.mockito.Matchers"
  };

  // Match against the any() or any(Class) methods.
  private static final Matcher<ExpressionTree> GENERIC_ANY =
      Matchers.staticMethod().onClassAny(CLASS_NAMES).named("any");

  @Override
  protected Matcher<? super MethodInvocationTree> matcher() {
    return GENERIC_ANY;
  }

  @Override
  protected String formatMessage(
      String expectedTypeAsString, Type matcherType, String replacementName) {
    return String.format(
        "Matcher mismatch: use %s() matcher to match primitive %s arguments",
        replacementName, expectedTypeAsString);
  }
}
