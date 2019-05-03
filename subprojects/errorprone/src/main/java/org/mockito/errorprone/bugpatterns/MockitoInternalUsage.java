/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import static com.google.errorprone.matchers.Matchers.packageStartsWith;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.BugPattern.ProvidesFix;
import com.google.errorprone.BugPattern.SeverityLevel;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.MemberSelectTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Symbol;

/**
 * {@link com.google.errorprone.bugpatterns.BugChecker} that detects usages of
 * org.mockito.internal.*
 */
@AutoService(BugChecker.class)
@BugPattern(
    name = "MockitoInternalUsage",
    summary = "org.mockito.internal.* is a private API and should not be used by clients",
    explanation =
        "Classes under `org.mockito.internal.*` are internal implementation details and are"
            + " not part of Mockito's public API. Mockito team does not support them, and they"
            + " may change at any time. Depending on them may break your code when you upgrade"
            + " to new versions of Mockito."
            + "This checker ensures that your code will not break with future Mockito upgrades."
            + "Mockito's public API is documented at"
            + " https://www.javadoc.io/doc/org.mockito/mockito-core/. If you believe that there"
            + " is no replacement available in the public API for your use-case, contact the"
            + " Mockito team at https://github.com/mockito/mockito/issues.",
    severity = SeverityLevel.WARNING,
    providesFix = ProvidesFix.REQUIRES_HUMAN_ATTENTION)
public class MockitoInternalUsage extends BugChecker implements MemberSelectTreeMatcher {

  private static final Matcher<Tree> INSIDE_MOCKITO = packageStartsWith("org.mockito");

  @Override
  public Description matchMemberSelect(MemberSelectTree tree, VisitorState state) {
    if (INSIDE_MOCKITO.matches(tree, state)) {
      return Description.NO_MATCH;
    }
    Symbol symbol = ASTHelpers.getSymbol(tree);
    if (symbol != null && symbol.getQualifiedName().toString().startsWith("org.mockito.internal")) {
      return describeMatch(tree);
    }
    return Description.NO_MATCH;
  }
}
